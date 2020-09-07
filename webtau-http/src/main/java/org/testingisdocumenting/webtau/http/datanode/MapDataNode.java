/*
 * Copyright 2020 webtau maintainers
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

import org.testingisdocumenting.webtau.http.datacoverage.DataNodeToMapOfValuesConverter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

class MapDataNode implements DataNode {
    private final DataNodeId id;
    private final Map<String, DataNode> children;
    private final Map<DataNodeId, DataNode> queriedNonExistentNodes = new LinkedHashMap<>();

    MapDataNode(DataNodeId id, Map<String, DataNode> children) {
        Objects.requireNonNull(children);

        this.id = id;
        this.children = children;
    }

    @Override
    public DataNodeId id() {
        return id;
    }

    @Override
    public DataNode get(String nameOrPath) {
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
        return (children.containsKey(name)) ?
                children.get(name) :
                queriedNonExistentNodes.computeIfAbsent(id.child(name), NullDataNode::new);
    }

    private DataNode getIndexedChild(String name, int openBraceIdx, int closeBraceIdx) {
        int additionalOpenIdx = name.indexOf('[', openBraceIdx + 1);
        int additionalCloseId = name.indexOf(']', closeBraceIdx + 1);

        if (additionalOpenIdx != -1 || additionalCloseId != -1 || openBraceIdx > closeBraceIdx) {
            throw new IllegalArgumentException("Requested name " + name + " contains mismatched indexing brackets");
        }

        String indexStr = name.substring(openBraceIdx + 1, closeBraceIdx);
        try {
            int idx = Integer.parseInt(indexStr);
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
    public DataNode get(int idx) {
        return queriedNonExistentNodes.computeIfAbsent(id.peer(idx), NullDataNode::new);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E get() {
        return (E) new DataNodeToMapOfValuesConverter((id, traceableValue) -> traceableValue.getValue())
                .convert(this);
    }

    @Override
    public int numberOfChildren() {
        return children.size();
    }

    @Override
    public Map<String, DataNode> asMap() {
        return Collections.unmodifiableMap(children);
    }

    @Override
    public String toString() {
        return "{" + children.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(joining(", "))  + "}";
    }
}
