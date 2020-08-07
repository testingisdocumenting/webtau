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

package org.testingisdocumenting.webtau.graphql;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class GraphQLCoveredOperations {
    private final Map<GraphQLOperation, Set<Call>> actualCallsIdsByOperation;

    GraphQLCoveredOperations() {
        actualCallsIdsByOperation = new LinkedHashMap<>();
    }

    public void add(GraphQLOperation operation, String id, long elapsedTime) {
        Set<Call> calls = actualCallsIdsByOperation.computeIfAbsent(operation, k -> new LinkedHashSet<>());

        calls.add(new Call(id, elapsedTime));
    }

    public boolean contains(GraphQLOperation operation) {
        return actualCallsIdsByOperation.containsKey(operation);
    }

    Stream<GraphQLOperation> coveredOperations() {
        return actualCallsIdsByOperation.keySet().stream();
    }

    public Stream<Map.Entry<GraphQLOperation, Set<Call>>> getActualCalls() {
        return actualCallsIdsByOperation.entrySet().stream();
    }

    public static class Call {
        private final String id;
        private final long elapsedTime;

        private Call(String id, long elapsedTime) {
            this.id = id;
            this.elapsedTime = elapsedTime;
        }

        public String getId() {
            return id;
        }

        public long getElapsedTime() {
            return elapsedTime;
        }
    }
}
