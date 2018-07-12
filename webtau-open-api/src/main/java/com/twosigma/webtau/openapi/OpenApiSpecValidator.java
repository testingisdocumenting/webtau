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
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.atlassian.oai.validator.report.ValidationReport;
import com.twosigma.webtau.console.ConsoleOutputs;
import com.twosigma.webtau.console.ansi.Color;
import com.twosigma.webtau.http.validation.HttpValidationResult;

import java.util.Optional;

import static com.twosigma.webtau.http.HttpUrl.extractPath;
import static com.twosigma.webtau.http.HttpUrl.extractQueryParams;

public class OpenApiSpecValidator {
    private final SwaggerRequestResponseValidator openApiValidator;
    private final OpenApiSpec openAPISpec;

    public OpenApiSpecValidator(OpenApiSpec openApiSpec) {
        this.openAPISpec = openApiSpec;
        this.openApiValidator = openApiSpec.isSpecDefined() ?
                SwaggerRequestResponseValidator.createFor(openApiSpec.getSpecUrl()).build():
                null;
    }

    public void validateApiSpec(HttpValidationResult result, ValidationMode validationMode) {
        if (! openAPISpec.isSpecDefined()) {
            return;
        }

        Optional<OpenApiOperation> apiOperation = openAPISpec.findApiOperation(result.getRequestMethod(), result.getFullUrl());
        if (! apiOperation.isPresent()) {
            ConsoleOutputs.out(Color.YELLOW, "Path, ", result.getFullUrl(), " not found in OpenAPI spec");
            return;
        }

        SimpleRequest request = buildRequest(result);
        SimpleResponse response = buildResponse(result);

        ValidationReport validationReport;
        switch (validationMode) {
            case RESPONSE_ONLY:
                validationReport = openApiValidator.validateResponse(request.getPath(), request.getMethod(), response);
                break;
            case REQUEST_ONLY:
                validationReport = openApiValidator.validateRequest(request);
                break;
            case NONE:
                validationReport = ValidationReport.empty();
                break;
            case ALL:
            default:
                validationReport = openApiValidator.validate(request, response);
                break;
        }

        validationReport.getMessages().forEach(message -> result.addMismatch("API spec validation failure: " + message.toString()));
    }

    private SimpleResponse buildResponse(HttpValidationResult result) {
        return SimpleResponse.Builder
                    .status(result.getResponseStatusCode())
                    .withBody(result.getResponseTextContent())
                    .build();
    }

    private SimpleRequest buildRequest(HttpValidationResult result) {
        String relativePath = extractPath(result.getFullUrl());

        SimpleRequest.Builder builder = new SimpleRequest.Builder(result.getRequestMethod(), relativePath);
        if (result.getRequestContent() != null) {
            builder.withBody(result.getRequestContent());
        }
        if (result.getRequestHeader() != null) {
            result.getRequestHeader().forEachProperty(builder::withHeader);
        }

        extractQueryParams(result.getFullUrl()).forEach(builder::withQueryParam);

        return builder.build();
    }
}
