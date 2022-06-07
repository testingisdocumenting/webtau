/*
 * Copyright 2020 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.webtau.http.validation;

import org.testingisdocumenting.webtau.data.traceable.TraceableValue;
import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.HttpResponse;
import org.testingisdocumenting.webtau.http.datanode.DataNode;
import org.testingisdocumenting.webtau.http.datanode.DataNodeBuilder;
import org.testingisdocumenting.webtau.http.datanode.DataNodeId;
import org.testingisdocumenting.webtau.http.datanode.NullDataNode;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HeaderDataNode implements DataNode {
    private static final Set<CamelCaseTranslation> translations = setOf(
            new CamelCaseTranslation("Content-Location", "contentLocation"),
            new CamelCaseTranslation("Content-Length", "contentLength", Integer::valueOf),
            new CamelCaseTranslation("Content-Encoding", "contentEncoding")
    );

    private final DataNode dataNode;
    private final HttpHeader responseHeader;

    public final DataNode statusCode;
    public final DataNode location;
    public final DataNode contentType;
    public final DataNode contentLength;
    public final DataNode contentLocation;
    public final DataNode contentEncoding;

    public HeaderDataNode(HttpResponse response) {
        Map<String, Object> headerData = new HashMap<>();

        headerData.put("statusCode", response.getStatusCode());
        headerData.put("contentType", response.getContentType());

        response.getHeader().forEachProperty(headerData::put);

        translations.forEach(translation -> addCamelCaseVersion(headerData, translation));

        this.dataNode = DataNodeBuilder.fromMap(new DataNodeId("header"), headerData);
        this.responseHeader = response.getHeader();

        statusCode = get("statusCode");
        contentType = get("contentType");
        location = get("location");
        contentLocation = get("contentLocation");
        contentLength = get("contentLength");
        contentEncoding = get("contentEncoding");
    }

    public HttpHeader getResponseHeader() {
        return responseHeader;
    }

    @Override
    public DataNodeId id() {
        return dataNode.id();
    }

    @Override
    public DataNode get(String name) {
        Optional<String> matchingKey = findMatchingCaseInsensitiveKey(name);

        return matchingKey
                .map(dataNode::get)
                .orElse(new NullDataNode(id().child(name)));
    }

    @Override
    public boolean has(String name) {
        Optional<String> matchingKey = findMatchingCaseInsensitiveKey(name);
        return matchingKey.isPresent();
    }

    @Override
    public DataNode get(int idx) {
        return dataNode.get(idx);
    }

    @Override
    public TraceableValue getTraceableValue() {
        return dataNode.getTraceableValue();
    }

    @Override
    public <E> E get() {
        return dataNode.get();
    }

    @Override
    public boolean isList() {
        return dataNode.isList();
    }

    @Override
    public boolean isSingleValue() {
        return false;
    }

    @Override
    public List<DataNode> elements() {
        return dataNode.elements();
    }

    @Override
    public Collection<DataNode> children() {
        return dataNode.children();
    }

    @Override
    public Iterator<DataNode> iterator() {
        return dataNode.iterator();
    }

    @Override
    public int numberOfChildren() {
        return dataNode.numberOfChildren();
    }

    @Override
    public int numberOfElements() {
        return dataNode.numberOfElements();
    }

    @Override
    public DataNode find(Predicate<DataNode> predicate) {
        return dataNode.find(predicate);
    }

    @Override
    public DataNode findAll(Predicate<DataNode> predicate) {
        return dataNode.findAll(predicate);
    }

    @Override
    public String toString() {
        return dataNode.toString();
    }

    /**
     * @deprecated see {@link HeaderDataNode#statusCode}
     * @return status code data node
     */
    public DataNode statusCode() {
        return dataNode.get("statusCode");
    }

    private Optional<String> findMatchingCaseInsensitiveKey(String name) {
        return findMatchingCaseInsensitiveKey(name,
                dataNode.children().stream()
                        .map(node -> node.id().getName()));
    }

    private static Optional<String> findMatchingCaseInsensitiveKey(String name, Stream<String> keys) {
        String lowerCaseName = name.toLowerCase();
        return keys
                .filter(k -> k != null && k.toLowerCase().equals(lowerCaseName))
                .findFirst();
    }

    private static void addCamelCaseVersion(Map<String, Object> headerData, CamelCaseTranslation translation) {
        Optional<String> existingHeaderName = findMatchingCaseInsensitiveKey(translation.originalName, headerData.keySet().stream());
        if (existingHeaderName.isPresent()) {
            Object converted = translation.conversion.apply((String) headerData.get(existingHeaderName.get()));

            headerData.put(translation.camelCaseName, converted);
            headerData.put(translation.originalName, converted);
        }
    }

    private static <T> Set<T> setOf(T... things) {
        return Arrays.stream(things).collect(Collectors.toSet());
    }

    private static class CamelCaseTranslation {
        private final String originalName;
        private final String camelCaseName;
        private final Function<String, Object> conversion;

        private CamelCaseTranslation(String originalName, String camelCaseName) {
            this.originalName = originalName;
            this.camelCaseName = camelCaseName;
            this.conversion = (v) -> v;
        }

        private CamelCaseTranslation(String originalName, String camelCaseName, Function<String, Object> conversion) {
            this.originalName = originalName;
            this.camelCaseName = camelCaseName;
            this.conversion = conversion;
        }
    }
}
