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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class ListDataNode implements DataNode {
    private final DataNodeId id;
    private final List<DataNode> values;

    ListDataNode(DataNodeId id, List<DataNode> values) {
        Objects.requireNonNull(values);

        this.id = id;
        this.values = values;
    }

    @Override
    public DataNodeId id() {
        return id;
    }

    @Override
    public DataNode get(String nameOrPath) {
        if (values.stream().noneMatch(v -> v.has(nameOrPath))) {
            return new NullDataNode(id.child(nameOrPath));
        }

        return new ListDataNode(id.child(nameOrPath),
                values.stream()
                        .map(n -> n.get(nameOrPath))
                        .collect(Collectors.toList()));
    }

    @Override
    public DataNode get(int idx) {
        return (idx < 0 || idx >= values.size()) ?
                new NullDataNode(id.peer(idx)):
                values.get(idx);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E get() {
        return (E) values.stream().map(DataNode::get).collect(toList());
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public List<DataNode> elements() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public int numberOfElements() {
        return values.size();
    }

    @Override
    public String toString() {
        return "[" + values.stream().map(DataNode::toString).collect(joining(", ")) + "]";
    }
}
