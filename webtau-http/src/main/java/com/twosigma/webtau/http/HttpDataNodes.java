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
import com.twosigma.webtau.utils.JsonParseException;
import com.twosigma.webtau.utils.JsonUtils;

import java.util.List;
import java.util.Map;

class HttpDataNodes {
    private final HttpResponse response;
    private final DataNode body;

    HttpDataNodes(HttpResponse response) {
        this.response = response;
        this.body = createBodyDataNode();
    }

    public DataNode getBody() {
        return body;
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
