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

package com.twosigma.webtau.http.datanode;

import com.twosigma.webtau.data.traceable.TraceableValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class StructuredDataNode implements DataNode {
    private final DataNodeId id;

    private Map<String, DataNode> children;
    private TraceableValue value;
    private List<DataNode> values;

    private boolean isSingleValue;

    public StructuredDataNode(DataNodeId id, TraceableValue value) {
        this.id = id;
        this.value = value;
        this.isSingleValue = true;
    }

    public StructuredDataNode(DataNodeId id, List<DataNode> values) {
        this.id = id;
        this.values = values;
    }

    public StructuredDataNode(DataNodeId id, Map<String, DataNode> children) {
        this.id = id;
        this.children = children;
    }

    @Override
    public DataNodeId id() {
        return id;
    }

    @Override
    public DataNode get(String name) {
        if (isList()) {
            return getAsCollectFromList(name);
        }

        return (children != null && children.containsKey(name)) ?
                children.get(name):
                new NullDataNode(id.child(name));
    }

    @Override
    public DataNode get(int idx) {
        return (values == null || idx < 0 || idx >= values.size()) ?
            new NullDataNode(id.peer(idx)):
            values.get(idx);
    }

    @Override
    public TraceableValue get() {
        return value;
    }

    @Override
    public boolean isList() {
        return values != null;
    }

    @Override
    public boolean isSingleValue() {
        return isSingleValue;
    }

    @Override
    public List<DataNode> elements() {
        return values == null ?
                Collections.emptyList() :
                Collections.unmodifiableList(values);
    }

    @Override
    public int numberOfChildren() {
        return isSingleValue ? 0 :
            isList() ? 0 :
                children != null ? children.size() : 0;
    }

    @Override
    public int numberOfElements() {
        return isList() ? values.size() : 0;
    }

    @Override
    public Map<String, DataNode> asMap() {
        return children != null ? Collections.unmodifiableMap(children) : Collections.emptyMap();
    }

    @Override
    public String toString() {
        if (isSingleValue) {
            return value == null ? "null" : value.toString();
        }

        if (values != null) {
            return "[" + values.stream().map(DataNode::toString).collect(joining(", ")) + "]";
        }

        return "{" + children.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(joining(", "))  + "}";
    }

    private DataNode getAsCollectFromList(String name) {
        return new StructuredDataNode(id.child(name),
                values.stream()
                        .map(n -> n.get(name))
                        .collect(Collectors.toList()));
    }
}
