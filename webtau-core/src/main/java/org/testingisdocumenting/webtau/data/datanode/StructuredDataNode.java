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

package org.testingisdocumenting.webtau.data.datanode;

import org.testingisdocumenting.webtau.data.traceable.CheckLevel;
import org.testingisdocumenting.webtau.data.traceable.TraceableValue;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

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
    public DataNode get(String nameOrPath) {
        if (isList()) {
            return getAsCollectFromList(nameOrPath);
        }

        if (nameOrPath.contains(".") || nameOrPath.contains("[")) {
            return ValueExtractorByPath.extractFromDataNode(this, nameOrPath);
        }

        return (children != null && children.containsKey(nameOrPath)) ?
                children.get(nameOrPath):
                new NullDataNode(id.child(nameOrPath));
    }

    @Override
    public boolean has(String pathOrName) {
        return !get(pathOrName).isNull();
    }

    @Override
    public DataNode get(int idx) {
        if (idx < 0) {
            idx = numberOfElements() + idx;
        }

        return (values == null || idx >= values.size()) ?
            new NullDataNode(id.peer(idx)):
            values.get(idx);
    }

    @Override
    public TraceableValue getTraceableValue() {
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E get() {
        if (!isSingleValue) {
            return (E) extractComplexValue();
        }

        if (value == null) {
            return null;
        }

        return (E) value.getValue();
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
    public Collection<DataNode> children() {
        return children == null ?
                Collections.emptyList():
                Collections.unmodifiableCollection(children.values());
    }

    @Override
    public Iterator<DataNode> iterator() {
        return elements().iterator();
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
    public DataNode find(Predicate<DataNode> predicate) {
        DataNode result = elements().stream()
                .filter(predicate)
                .findFirst()
                .orElseGet(() -> {
                    DataNodeId nullId = id.child("<find>");
                    return new NullDataNode(nullId);
                });

        if (!result.isNull()) {
            if (result.isSingleValue()) {
                result.getTraceableValue().updateCheckLevel(CheckLevel.FuzzyPassed);
            }
        }

        return result;
    }

    @Override
    public DataNode findAll(Predicate<DataNode> predicate) {
        return new StructuredDataNode(id().child("<finsAll>"),
                elements().stream().filter(predicate).collect(toList()));
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException("Use .get() to access DataNode underlying value");
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

    private DataNode getAsCollectFromList(String nameOrPath) {
        if (values.stream().noneMatch(v -> v.has(nameOrPath))) {
            return new NullDataNode(id.child(nameOrPath));
        }

        return new StructuredDataNode(id.child(nameOrPath),
                values.stream()
                        .map(n -> n.get(nameOrPath))
                        .collect(Collectors.toList()));
    }

    private Object extractComplexValue() {
        if (values != null) {
            return values.stream().map(DataNode::get).collect(toList());
        }

        return new DataNodeToMapOfValuesConverter((id, traceableValue) -> traceableValue.getValue())
                .convert(this);
    }
}
