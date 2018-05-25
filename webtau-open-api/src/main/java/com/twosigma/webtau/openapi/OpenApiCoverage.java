/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

import com.twosigma.webtau.http.validation.HttpValidationResult;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class OpenApiCoverage {
    private OpenApiSpec spec;

    private Set<OpenApiOperation> coveredOperations = new HashSet<>();

    public OpenApiCoverage(OpenApiSpec spec) {
        this.spec = spec;
    }

    public void recordOperation(HttpValidationResult validationResult) {
        Optional<OpenApiOperation> apiOperation = spec.findApiOperation(
                validationResult.getRequestMethod(),
                validationResult.getFullUrl());
        apiOperation.map(coveredOperations::add);
    }

    public Stream<OpenApiOperation> nonCoveredOperations() {
        return spec.availableOperationsStream().filter(o -> !coveredOperations.contains(o));
    }
}
