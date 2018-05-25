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

import com.atlassian.oai.validator.SwaggerRequestResponseValidator;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.atlassian.oai.validator.report.ValidationReport;
import com.twosigma.webtau.console.ConsoleOutputs;
import com.twosigma.webtau.console.ansi.Color;
import com.twosigma.webtau.http.validation.HttpValidationResult;

import java.util.Optional;

import static com.twosigma.webtau.http.HttpUrl.extractPath;

public class OpenApiSpecValidator {
    private final SwaggerRequestResponseValidator openApiValidator;
    private final OpenApiSpec openAPISpec;

    public OpenApiSpecValidator(OpenApiSpec openApiSpec) {
        this.openAPISpec = openApiSpec;
        this.openApiValidator = openApiSpec.isSpecDefined() ?
                SwaggerRequestResponseValidator.createFor(openApiSpec.getSpecUrl()).build():
                null;
    }

    public void validateApiSpec(HttpValidationResult result) {
        if (! openAPISpec.isSpecDefined()) {
            return;
        }

        Optional<OpenApiOperation> apiOperation = openAPISpec.findApiOperation(result.getRequestMethod(), result.getFullUrl());
        if (! apiOperation.isPresent()) {
            ConsoleOutputs.out(Color.YELLOW, "Path, ", result.getFullUrl(), " not found in OpenAPI spec");
            return;
        }

        SimpleResponse response = SimpleResponse.Builder
                .status(result.getResponseStatusCode())
                .withBody(result.getResponseContent())
                .build();

        Request.Method method = Enum.valueOf(Request.Method.class, result.getRequestMethod());
        String relativePath = extractPath(result.getFullUrl());

        ValidationReport validationReport = openApiValidator.validateResponse(relativePath, method, response);
        validationReport.getMessages().forEach(message -> result.addMismatch("API spec validation failure: " + message.toString()));
    }
}
