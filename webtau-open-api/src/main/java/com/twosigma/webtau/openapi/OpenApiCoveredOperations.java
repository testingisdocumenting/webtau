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
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class OpenApiCoveredOperations {
    private Map<OpenApiOperation, Set<String>> actualCallsIdsByOperation;

    OpenApiCoveredOperations() {
        actualCallsIdsByOperation = new LinkedHashMap<>();
    }

    void add(OpenApiOperation openApiOperation, String id) {
        Set<String> methodAndUrls =
                actualCallsIdsByOperation.computeIfAbsent(openApiOperation, k -> new LinkedHashSet<>());

        methodAndUrls.add(id);
    }

    Stream<OpenApiOperation> coveredOperations() {
        return actualCallsIdsByOperation.keySet().stream();
    }

    boolean contains(OpenApiOperation openApiOperation) {
        return actualCallsIdsByOperation.containsKey(openApiOperation);
    }

    List<Map<String, ?>> httpCallIdsByOperationAsMap() {
        return actualCallsIdsByOperation.entrySet().stream().map(entry ->
                createOperationEntryAsMap(
                        entry.getKey().getMethod(),
                        entry.getKey().getUrl(),
                        entry.getValue())).collect(toList());
    }

    private Map<String, ?> createOperationEntryAsMap(String method, String url, Set<String> callIds) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("method", method);
        result.put("url", url);
        result.put("httpCallIds", callIds);

        return result;
    }
}
