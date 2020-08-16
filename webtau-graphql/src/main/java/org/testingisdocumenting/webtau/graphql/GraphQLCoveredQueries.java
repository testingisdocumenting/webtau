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

public class GraphQLCoveredQueries {
    private final Map<GraphQLQuery, Set<Call>> actualCallsIdsByQuery;

    GraphQLCoveredQueries() {
        actualCallsIdsByQuery = new LinkedHashMap<>();
    }

    public void add(GraphQLQuery query, String id, long elapsedTime) {
        Set<Call> calls = actualCallsIdsByQuery.computeIfAbsent(query, k -> new LinkedHashSet<>());

        calls.add(new Call(id, elapsedTime));
    }

    public boolean contains(GraphQLQuery query) {
        return actualCallsIdsByQuery.containsKey(query);
    }

    Stream<GraphQLQuery> coveredQueries() {
        return actualCallsIdsByQuery.keySet().stream();
    }

    public Stream<Map.Entry<GraphQLQuery, Set<Call>>> getActualCalls() {
        return actualCallsIdsByQuery.entrySet().stream();
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
