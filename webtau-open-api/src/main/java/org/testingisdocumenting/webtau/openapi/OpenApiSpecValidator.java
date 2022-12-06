/*
 * Copyright 2020 webtau maintainers
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

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.atlassian.oai.validator.report.LevelResolver;
import com.atlassian.oai.validator.report.ValidationReport;
import org.testingisdocumenting.webtau.WebTauCore;
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult;

import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static org.testingisdocumenting.webtau.utils.UrlUtils.extractPath;
import static org.testingisdocumenting.webtau.utils.UrlUtils.extractQueryParams;

public class OpenApiSpecValidator {
    private final OpenApiInteractionValidator openApiValidator;
    private final OpenApiSpec openAPISpec;

    public OpenApiSpecValidator(OpenApiSpec openApiSpec, OpenApiValidationConfig validationConfig) {
        this.openAPISpec = openApiSpec;
        this.openApiValidator = openApiSpec.isSpecDefined() ?
                OpenApiInteractionValidator
                        .createForInlineApiSpecification(openApiSpec.getSpecContent())
                        .withLevelResolver(createLevelResolver(validationConfig))
                        .build() :
                null;
    }

    public boolean isSpecDefined() {
        return openAPISpec.isSpecDefined();
    }

    public void validateApiSpec(HttpValidationResult result, OpenApiValidationMode openApiValidationMode) {
        Optional<OpenApiOperation> apiOperation = openAPISpec.findApiOperation(result.getRequestMethod(), result.getFullUrl());
        if (!apiOperation.isPresent()) {
            WebTauCore.warning("HTTP url does not match any defined Open API operation", "url", result.getFullUrl());

            return;
        }

        SimpleRequest request = buildRequest(result);
        SimpleResponse response = buildResponse(result);

        ValidationReport validationReport = validate(openApiValidationMode, request, response);
        validationReport.getMessages().forEach(message ->
                result.addMismatch("API spec validation failure: " + renderMessage(message)));

        if (!validationReport.getMessages().isEmpty()) {
            throw new AssertionError("schema is not valid:\n" + validationReport
                    .getMessages().stream()
                    .map(OpenApiSpecValidator::renderMessage)
                    .collect(joining("\n")));
        }
    }

    private ValidationReport validate(OpenApiValidationMode openApiValidationMode, SimpleRequest request, SimpleResponse response) {
        switch (openApiValidationMode) {
            case RESPONSE_ONLY:
                return openApiValidator.validateResponse(request.getPath(), request.getMethod(), response);
            case REQUEST_ONLY:
                return openApiValidator.validateRequest(request);
            case NONE:
                return ValidationReport.empty();
            case ALL:
            default:
                return openApiValidator.validate(request, response);
        }
    }

    private static String renderMessage(ValidationReport.Message message) {
        return message.getContext().map(c ->
                (c.getRequestPath().isPresent() && c.getRequestMethod().isPresent() ?
                        (c.getRequestMethod().get() + " " + c.getRequestPath().get() + ": ") : "") +
                        message.getMessage()).orElse("");
    }

    private SimpleResponse buildResponse(HttpValidationResult result) {
        SimpleResponse.Builder builder = SimpleResponse.Builder
                .status(result.getResponseStatusCode());

        if (!result.getResponseType().isEmpty()) {
            builder.withContentType(result.getResponseType());
        }

        return builder
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

    private LevelResolver createLevelResolver(OpenApiValidationConfig validationConfig) {
        LevelResolver.Builder builder = LevelResolver.create();

        if (validationConfig.isIgnoreAdditionalProperties()) {
            builder.withLevel("validation.schema.additionalProperties", ValidationReport.Level.IGNORE);
        }

        return builder.build();
    }
}
