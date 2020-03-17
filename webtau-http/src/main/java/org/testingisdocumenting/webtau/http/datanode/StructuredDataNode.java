/*
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

package org.testingisdocumenting.webtau.http.datanode;

import org.testingisdocumenting.webtau.data.traceable.TraceableValue;
import org.testingisdocumenting.webtau.http.datacoverage.DataNodeToMapOfValuesConverter;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

        int dotIdx = nameOrPath.indexOf('.');
        if (dotIdx == -1) {
            // simple name
            return getChild(nameOrPath);
        }

        String rootName = nameOrPath.substring(0, dotIdx);
        DataNode root = getChild(rootName);
        String pathUnderRoot = nameOrPath.substring(dotIdx + 1);

        return root.get(pathUnderRoot);
    }

    private DataNode getChild(String name) {
        int openBraceIdx = name.indexOf('[');
        int closeBraceIdx = name.indexOf(']');
        if (openBraceIdx != -1 && closeBraceIdx != -1) {
            return getIndexedChild(name, openBraceIdx, closeBraceIdx);
        } else if (openBraceIdx != -1 || closeBraceIdx != -1) {
            throw new IllegalArgumentException("Requested name " + name + " is not a simple name nor does it contain a properly formatted index");
        }

        // simple name
        return (children != null && children.containsKey(name)) ?
                children.get(name) :
                new NullDataNode(id.child(name));
    }

    private DataNode getIndexedChild(String name, int openBraceIdx, int closeBraceIdx) {
        int additionalOpenIdx = name.indexOf('[', openBraceIdx + 1);
        int additionalCloseId = name.indexOf(']', closeBraceIdx + 1);

        if (additionalOpenIdx != -1 || additionalCloseId != -1 || openBraceIdx > closeBraceIdx) {
            throw new IllegalArgumentException("Requested name " + name + " contains mismatched indexing brackets");
        }

        String indexStr = name.substring(openBraceIdx + 1, closeBraceIdx);
        try {
            int idx = Integer.valueOf(indexStr);
            String nameWithoutIndex = name.substring(0, openBraceIdx);
            DataNode node = get(nameWithoutIndex);

            if (idx < 0) {
                idx = node.numberOfElements() + idx;
            }

            return node.get(idx);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Requested index " + indexStr + " of name " + name.substring(0, openBraceIdx) + " is not an integer");
        }
    }

    @Override
    public boolean has(String pathOrName) {
        return !get(pathOrName).isNull();
    }

    @Override
    public DataNode get(int idx) {
        return (values == null || idx < 0 || idx >= values.size()) ?
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
