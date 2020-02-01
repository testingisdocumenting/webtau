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

package com.twosigma.webtau.openapi;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class OpenApiCoveredOperations {
    private Map<OpenApiOperation, Set<Call>> actualCallsIdsByOperation;

    OpenApiCoveredOperations() {
        actualCallsIdsByOperation = new LinkedHashMap<>();
    }

    void add(OpenApiOperation openApiOperation, int statusCode, String id) {
        Set<Call> calls =
                actualCallsIdsByOperation.computeIfAbsent(openApiOperation, k -> new LinkedHashSet<>());

        calls.add(new Call(statusCode, id));
    }

    Stream<OpenApiOperation> coveredOperations() {
        return actualCallsIdsByOperation.keySet().stream();
    }

    boolean contains(OpenApiOperation openApiOperation) {
        return actualCallsIdsByOperation.containsKey(openApiOperation);
    }

    List<Map<String, ?>> httpCallIdsByOperationAsMap() {
        return actualCallsIdsByOperation.entrySet().stream()
                .map(entry ->
                        createOperationEntryAsMap(
                                entry.getKey().getMethod(),
                                entry.getKey().getUrl(),
                                allIds(entry.getValue())))
                .collect(toList());
    }

    List<Map<String, ?>> httpCallsByOperationAsMap() {
        return actualCallsIdsByOperation.entrySet().stream()
                .map(entry ->
                        createOperationEntryWithStatusCodeAsMap(
                                entry.getKey().getMethod(),
                                entry.getKey().getUrl(),
                                entry.getValue()
                        ))
                .collect(toList());
    }

    private Set<String> allIds(Set<Call> statusCodeToIds) {
        return statusCodeToIds.stream().map(Call::getId).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Map<String, ?> createOperationEntryAsMap(String method, String url, Set<String> callIds) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("method", method);
        result.put("url", url);
        result.put("httpCallIds", callIds);

        return result;
    }

    private Map<String, ?> createOperationEntryWithStatusCodeAsMap(String method, String url, Set<Call> calls) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("method", method);
        result.put("url", url);
        result.put("httpCalls", calls.stream().map(this::createCallAsMap).collect(Collectors.toCollection(LinkedHashSet::new)));

        return result;
    }

    private Map<String, ?> createCallAsMap(Call call) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("statusCode", call.statusCode);
        result.put("httpCallId", call.id);

        return result;
    }

    private static class Call {
        private final int statusCode;
        private final String id;

        private Call(int statusCode, String id) {
            this.statusCode = statusCode;
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
