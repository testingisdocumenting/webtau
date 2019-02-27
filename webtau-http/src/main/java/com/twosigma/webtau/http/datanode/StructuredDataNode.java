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

package com.twosigma.webtau.http.datanode;

import com.twosigma.webtau.data.traceable.TraceableValue;
import com.twosigma.webtau.http.datacoverage.DataNodeToMapOfValuesConverter;

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
        int idxIdx = nameOrPath.indexOf('[');

        if (dotIdx == -1 && idxIdx == -1) {
            // simple name
            return (children != null && children.containsKey(nameOrPath)) ?
                    children.get(nameOrPath):
                    new NullDataNode(id.child(nameOrPath));
        } else if (idxIdx == -1 || (dotIdx != -1 &&dotIdx < idxIdx)) {
            // first part of path is a field name
            String name = nameOrPath.substring(0, dotIdx);
            DataNode rootNode = get(name);

            String pathUnderRoot = nameOrPath.substring(dotIdx + 1);
            return rootNode.get(pathUnderRoot);
        } else {
            //TODO validate [] match

            int nameAndIdxEnd = dotIdx != -1 ? dotIdx : nameOrPath.length();
            String nameAndIdx = nameOrPath.substring(0, nameAndIdxEnd);
            String name = nameAndIdx.substring(0, idxIdx);
            String idxStr = nameAndIdx.substring(idxIdx + 1, nameAndIdxEnd - 1);
            int idx = Integer.valueOf(idxStr);

            DataNode node = get(name);

            if (idx < 0) {
                idx = node.numberOfElements() + idx;
            }

            DataNode indexedNode = node.get(idx);

            if (dotIdx == -1) {
                return indexedNode;
            } else {
                return indexedNode.get(nameOrPath.substring(dotIdx + 1));
            }
        }
    }

    @Override
    public boolean has(String pathOrName) {
        // TODO should this check subpaths for their has?  Not sure this was every "correct" for the list projection case btw.
        int dotIdx = pathOrName.indexOf('.');
        String firstPart = dotIdx == -1 ? pathOrName : pathOrName.substring(0, dotIdx);

        int idxIdx = firstPart.indexOf('[');
        String name = idxIdx == -1 ? firstPart : firstPart.substring(0, idxIdx);

        return children.containsKey(name);
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

    private DataNode getAsCollectFromList(String name) {
        if (values.stream().noneMatch(v -> v.has(name))) {
            return new NullDataNode(id.child(name));
        }

        return new StructuredDataNode(id.child(name),
                values.stream()
                        .map(n -> n.get(name))
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
