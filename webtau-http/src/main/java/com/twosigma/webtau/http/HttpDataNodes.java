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

package com.twosigma.webtau.http;

import com.twosigma.webtau.data.traceable.TraceableValue;
import com.twosigma.webtau.http.datanode.DataNode;
import com.twosigma.webtau.http.datanode.DataNodeBuilder;
import com.twosigma.webtau.http.datanode.DataNodeId;
import com.twosigma.webtau.http.datanode.StructuredDataNode;
import com.twosigma.webtau.http.validation.HeaderDataNode;
import com.twosigma.webtau.utils.JsonParseException;
import com.twosigma.webtau.utils.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

class HttpDataNodes {
    private static final Function<String, Object> asIs = (v) -> v;

    private final HttpResponse response;
    private final HttpHeader responseHeader;

    private Map<String, Object> headerData;

    private final HeaderDataNode header;
    private final DataNode body;

    HttpDataNodes(HttpResponse response) {
        this.response = response;

        responseHeader = response.getHeader();

        this.headerData = new HashMap<>();
        populateHeaderData();

        this.header = createHeaderDataNode();
        this.body = createBodyDataNode();
    }

    public HeaderDataNode getHeader() {
        return header;
    }

    public DataNode getBody() {
        return body;
    }

    private void populateHeaderData() {
        headerData.put("statusCode", response.getStatusCode());
        headerData.put("contentType", response.getContentType());

        responseHeader.forEachProperty(headerData::put);

        addCamelCaseVersion("Location", "location", asIs);
        addCamelCaseVersion("Content-Location", "contentLocation", asIs);
        addCamelCaseVersion("Content-Length", "contentLength", Integer::valueOf);
        addCamelCaseVersion("Content-Encoding", "contentEncoding", asIs);
    }

    private void addCamelCaseVersion(String original, String camelCase, Function<String, Object> conversion) {
        if (responseHeader.containsKey(original)) {
            Object converted = conversion.apply(responseHeader.get(original));

            headerData.put(camelCase, converted);
            headerData.put(original, converted);
        }
    }

    private HeaderDataNode createHeaderDataNode() {
        return new HeaderDataNode(DataNodeBuilder.fromMap(new DataNodeId("header"), headerData));
    }

    @SuppressWarnings("unchecked")
    private DataNode createBodyDataNode() {
        try {
            DataNodeId id = new DataNodeId("body");

            if (!response.isBinary() && response.nullOrEmptyTextContent()) {
                return new StructuredDataNode(id, new TraceableValue(null));
            }

            if (response.isText()) {
                return new StructuredDataNode(id, new TraceableValue(response.getTextContent()));
            }

            if (response.isJson()) {
                Object mapOrList = JsonUtils.deserialize(response.getTextContent());

                return mapOrList instanceof List ?
                        DataNodeBuilder.fromList(id, (List<Object>) mapOrList) :
                        DataNodeBuilder.fromMap(id, (Map<String, Object>) mapOrList);
            }

            return new StructuredDataNode(id, new TraceableValue(response.getBinaryContent()));
        } catch (JsonParseException e) {
            throw new RuntimeException("error parsing body: " + response.getTextContent(), e);
        }
    }
}
