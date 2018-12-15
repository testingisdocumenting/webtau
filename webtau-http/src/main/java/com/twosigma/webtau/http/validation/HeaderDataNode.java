/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.http.validation;

import com.twosigma.webtau.data.traceable.TraceableValue;
import com.twosigma.webtau.http.HttpResponse;
import com.twosigma.webtau.http.datanode.DataNode;
import com.twosigma.webtau.http.datanode.DataNodeBuilder;
import com.twosigma.webtau.http.datanode.DataNodeId;
import com.twosigma.webtau.http.datanode.NullDataNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HeaderDataNode implements DataNode {
    private static final Set<CamelCaseTranslation> translations = setOf(
            new CamelCaseTranslation("Content-Location", "contentLocation"),
            new CamelCaseTranslation("Content-Length", "contentLength", Integer::valueOf),
            new CamelCaseTranslation("Content-Encoding", "contentEncoding")
    );

    private final DataNode dataNode;

    public HeaderDataNode(HttpResponse response) {
        Map<String, Object> headerData = new HashMap<>();

        headerData.put("statusCode", response.getStatusCode());
        headerData.put("contentType", response.getContentType());

        response.getHeader().forEachProperty(headerData::put);

        translations.forEach(translation -> addCamelCaseVersion(headerData, translation));

        this.dataNode = DataNodeBuilder.fromMap(new DataNodeId("header"), headerData);
    }

    private static void addCamelCaseVersion(Map<String, Object> headerData, CamelCaseTranslation translation) {
        Optional<String> existingHeaderName = findMatchingCaseInsensitiveKey(translation.originalName, headerData.keySet().stream());
        if (existingHeaderName.isPresent()) {
            Object converted = translation.conversion.apply((String) headerData.get(existingHeaderName.get()));

            headerData.put(translation.camelCaseName, converted);
            headerData.put(translation.originalName, converted);
        }
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
    public int numberOfChildren() {
        return dataNode.numberOfChildren();
    }

    @Override
    public int numberOfElements() {
        return dataNode.numberOfElements();
    }

    @Override
    public Map<String, DataNode> asMap() {
        return dataNode.asMap();
    }

    @Override
    public String toString() {
        return dataNode.toString();
    }

    public DataNode statusCode() {
        return dataNode.get("statusCode");
    }

    private Optional<String> findMatchingCaseInsensitiveKey(String name) {
        return findMatchingCaseInsensitiveKey(name, dataNode.asMap().keySet().stream());
    }

    private static Optional<String> findMatchingCaseInsensitiveKey(String name, Stream<String> keys) {
        String lowerCaseName = name.toLowerCase();
        return keys
                .filter(k -> k != null && k.toLowerCase().equals(lowerCaseName))
                .findFirst();
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
