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
import com.twosigma.webtau.http.HttpHeader;
import com.twosigma.webtau.http.HttpResponse;
import com.twosigma.webtau.http.datanode.DataNode;
import com.twosigma.webtau.http.datanode.DataNodeBuilder;
import com.twosigma.webtau.http.datanode.DataNodeId;
import com.twosigma.webtau.http.datanode.NullDataNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class HeaderDataNode implements DataNode {
    private static final Function<String, Object> asIs = (v) -> v;

    private final DataNode dataNode;

    public HeaderDataNode(HttpResponse response) {
        Map<String, Object> headerData = new HashMap<>();

        headerData.put("statusCode", response.getStatusCode());
        headerData.put("contentType", response.getContentType());

        response.getHeader().forEachProperty(headerData::put);

        addCamelCaseVersion(response.getHeader(), headerData, "Content-Location", "contentLocation", asIs);
        addCamelCaseVersion(response.getHeader(), headerData, "Content-Length", "contentLength", Integer::valueOf);
        addCamelCaseVersion(response.getHeader(), headerData, "Content-Encoding", "contentEncoding", asIs);

        this.dataNode = DataNodeBuilder.fromMap(new DataNodeId("header"), headerData);
    }

    private static void addCamelCaseVersion(HttpHeader responseHeader, Map<String, Object> headerData, String original, String camelCase, Function<String, Object> conversion) {
        if (responseHeader.containsKey(original)) {
            Object converted = conversion.apply(responseHeader.get(original));

            headerData.put(camelCase, converted);
            headerData.put(original, converted);
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
    public TraceableValue get() {
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
        String lowerCaseName = name.toLowerCase();
        return dataNode.asMap().keySet().stream()
                .filter(k -> k != null && k.toLowerCase().equals(lowerCaseName))
                .findFirst();
    }
}
