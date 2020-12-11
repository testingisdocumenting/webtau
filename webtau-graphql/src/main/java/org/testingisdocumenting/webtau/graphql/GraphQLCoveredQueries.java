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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GraphQLCoveredQueries {
    private final Map<GraphQLQuery, Set<Call>> actualCallsIdsByQuery;

    GraphQLCoveredQueries() {
        actualCallsIdsByQuery = new ConcurrentHashMap<>();
    }

    public void add(GraphQLQuery query, String id, long elapsedTime, boolean isError) {
        Set<Call> calls = actualCallsIdsByQuery.computeIfAbsent(query, k -> ConcurrentHashMap.newKeySet());

        calls.add(new Call(id, elapsedTime, isError));
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

    public Stream<GraphQLQuery> coveredErrorBranches() {
        return coveredBranches(Call::isErrorResult);
    }

    public Stream<GraphQLQuery> coveredSuccessBranches() {
        return coveredBranches(Call::isSuccessResult);
    }

    private Stream<GraphQLQuery> coveredBranches(Predicate<Call> callPredicate) {
    return actualCallsIdsByQuery.entrySet().stream()
        .filter(
            graphQLQuerySetEntry ->
                graphQLQuerySetEntry.getValue().stream().anyMatch(callPredicate))
        .map(Entry::getKey);
    }

    public static class Call {
        private final String id;
        private final long elapsedTime;
        private final boolean errorResult;

        private Call(String id, long elapsedTime, boolean isError) {
            this.id = id;
            this.elapsedTime = elapsedTime;
            this.errorResult = isError;
        }

        public String getId() {
            return id;
        }

        public long getElapsedTime() {
            return elapsedTime;
        }

        public boolean isErrorResult() {
           return errorResult;
        }

        public boolean isSuccessResult() {
            return !errorResult;
        }
    }
}
