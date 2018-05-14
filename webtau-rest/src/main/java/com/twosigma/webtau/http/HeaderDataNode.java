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

import java.util.List;
import java.util.Map;

import com.twosigma.webtau.data.traceable.TraceableValue;
import com.twosigma.webtau.http.datanode.DataNode;
import com.twosigma.webtau.http.datanode.DataNodeId;

public class HeaderDataNode implements DataNode {
    private final DataNode dataNode;

    public HeaderDataNode(DataNode dataNode) {
        this.dataNode = dataNode;
    }

    @Override
    public DataNodeId id() {
        return dataNode.id();
    }

    @Override
    public DataNode get(String name) {
        return dataNode.get(name);
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
}
