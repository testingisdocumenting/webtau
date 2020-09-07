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

import org.testingisdocumenting.webtau.data.traceable.TraceableValue;

import java.util.Objects;

class ValueDataNode implements DataNode {
    private final DataNodeId id;
    private final TraceableValue value;
    private final NonExistentNodesTracker nonExistentNodesTracker;

    ValueDataNode(DataNodeId id, TraceableValue value) {
        Objects.requireNonNull(value);

        this.id = id;
        this.value = value;
        this.nonExistentNodesTracker = new NonExistentNodesTracker(id);
    }

    @Override
    public DataNodeId id() {
        return id;
    }

    @Override
    public DataNode get(String pathOrName) {
        return nonExistentNodesTracker.register(pathOrName);
    }

    @Override
    public DataNode get(int idx) {
        return nonExistentNodesTracker.register(idx);
    }

    @Override
    public TraceableValue getTraceableValue() {
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E get() {
        return (E) value.getValue();
    }

    @Override
    public boolean isSingleValue() {
        return true;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
