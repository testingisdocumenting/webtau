/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.openapi;

import org.testingisdocumenting.webtau.http.validation.HttpValidationResult;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpenApiCoverage {
    private final OpenApiSpec spec;
    private final OpenApiCoveredOperations coveredOperations = new OpenApiCoveredOperations();

    public OpenApiCoverage(OpenApiSpec spec) {
        this.spec = spec;
    }

    public void recordOperation(HttpValidationResult validationResult) {
        if (!spec.isSpecDefined()) {
            return;
        }

        Optional<OpenApiOperation> apiOperation = spec.findApiOperation(
                validationResult.getRequestMethod(),
                validationResult.getFullUrl());

        apiOperation.ifPresent(openApiOperation -> coveredOperations.add(openApiOperation, validationResult.getResponseStatusCode(), validationResult.getId()));
    }

    List<Map<String, ?>> httpCallIdsByOperationAsMap() {
        return coveredOperations.httpCallIdsByOperationAsMap();
    }

    List<Map<String, ?>> httpCallsByOperationAsMap() {
        return coveredOperations.httpCallsByOperationAsMap();
    }

    Stream<OpenApiOperation> coveredOperations() {
        return coveredOperations.coveredOperations();
    }

    Stream<OpenApiOperation> nonCoveredOperations() {
        return spec.availableOperationsStream().filter(o -> !coveredOperations.contains(o));
    }

    private Map<OpenApiOperation, Map<String, Integer>> getInvocationCounts() {
        Map<OpenApiOperation, Map<String, Integer>> operationToResponseToInvocationCount = spec.availableOperationsStream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        op -> spec.getDeclaredResponses(op).stream()
                                .collect(Collectors.toMap(
                                        Function.identity(),
                                        resp -> 0))
                        )
                );

        coveredOperations.getActualCalls().forEach(entry -> {
            OpenApiOperation op = entry.getKey();
            Set<Integer> actualResponses = entry.getValue().stream().map(OpenApiCoveredOperations.Call::getStatusCode).collect(Collectors.toSet());
            Map<String, Integer> declaredResponses = operationToResponseToInvocationCount.get(op);

            for (Integer actualResponse : actualResponses) {
                String matchingKey = determineMatchingResponse(declaredResponses, actualResponse);

                if (matchingKey != null) {
                    int currentCount = declaredResponses.get(matchingKey);
                    declaredResponses.put(matchingKey, currentCount + 1);
                }
            }
        });

        return operationToResponseToInvocationCount;
    }

    /*
    Responses in the swagger spec can be any of the following:
    - an integer representing the status code
    - a status family represented as a wildcard, e.g. 2XX for success responses
    - "default"; this essentially means anything else
     */
    private String determineMatchingResponse(Map<String, Integer> declaredResponses, Integer actualResponse) {
        String matchingResponse = null;
        if (declaredResponses == null) {
            return matchingResponse;
        }

        if (declaredResponses.containsKey(actualResponse.toString())) {
            matchingResponse = actualResponse.toString();
        } else if (actualResponse < 200 && declaredResponses.containsKey("1XX")) {
            matchingResponse = "1XX";
        } else if (actualResponse < 300 && declaredResponses.containsKey("2XX")) {
            matchingResponse = "2XX";
        } else if (actualResponse < 400 && declaredResponses.containsKey("3XX")) {
            matchingResponse = "3XX";
        } else if (actualResponse < 500 && declaredResponses.containsKey("4XX")) {
            matchingResponse = "4XX";
        } else if (actualResponse < 600 && declaredResponses.containsKey("5XX")) {
            matchingResponse = "5XX";
        } else if (declaredResponses.containsKey("default")) {
            matchingResponse = "default";
        }
        return matchingResponse;
    }

    Map<OpenApiOperation, Set<String>> coveredResponses() {
        Map<OpenApiOperation, Map<String, Integer>> invocationCounts = getInvocationCounts();
        return invocationCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> filterStatusCodes(e, count -> count > 0)
                ));
    }

    Map<OpenApiOperation, Set<String>> nonCoveredResponses() {
        Map<OpenApiOperation, Map<String, Integer>> invocationCounts = getInvocationCounts();
        return invocationCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> filterStatusCodes(e, count -> count == 0)
                ));
    }

    private Set<String> filterStatusCodes(Map.Entry<OpenApiOperation, Map<String, Integer>> entry, Predicate<Integer> countFilter) {
        return entry.getValue().entrySet().stream()
                .filter(e -> countFilter.test(e.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
