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

package com.twosigma.webtau.http;

import com.atlassian.oai.validator.SwaggerRequestResponseValidator;
import com.atlassian.oai.validator.interaction.ApiOperationResolver;
import com.atlassian.oai.validator.model.ApiOperationMatch;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.atlassian.oai.validator.report.ValidationReport;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.util.SwaggerDeserializationResult;

import java.net.MalformedURLException;
import java.net.URL;

import static com.twosigma.webtau.cfg.WebTauConfig.getCfg;
import static java.lang.String.format;

class OpenAPISpecValidator {
    private final SwaggerRequestResponseValidator openApiValidator;
    private final ApiOperationResolver apiOperationResolver;

    OpenAPISpecValidator() {
        this(getCfg().getOpenApiSpecUrl());
    }

    OpenAPISpecValidator(String openApiSpecUrl) {
        if (openApiSpecUrl != null && !openApiSpecUrl.trim().isEmpty()) {
            openApiValidator = SwaggerRequestResponseValidator.createFor(openApiSpecUrl).build();

            final SwaggerDeserializationResult swaggerParseResult =
                    new SwaggerParser().readWithInfo(openApiSpecUrl, null, true);
            final Swagger api = swaggerParseResult.getSwagger();
            if (api == null) {
                throw new IllegalArgumentException(
                        format("Unable to load API descriptor from provided %s:\n\t%s",
                                openApiSpecUrl, swaggerParseResult.getMessages().toString().replace("\n", "\n\t")));
            }
            apiOperationResolver = new ApiOperationResolver(api, null);
        } else {
            openApiValidator = null;
            apiOperationResolver = null;
        }
    }

    void validateApiSpec(String requestMethod, String fullUrl, HttpResponse httpResponse, HttpValidationResult result) {
        if (openApiValidator == null) {
            return;
        }

        String path = extractPath(fullUrl);
        Request.Method method = Enum.valueOf(Request.Method.class, requestMethod);

        ApiOperationMatch apiOperationMatch = apiOperationResolver.findApiOperation(path, method);
        if (!apiOperationMatch.isPathFound()) {
            return;
        }

        SimpleResponse response = SimpleResponse.Builder
                .status(httpResponse.getStatusCode())
                .withBody(httpResponse.getContent())
                .build();

        ValidationReport validationReport = openApiValidator.validateResponse(path, method, response);
        validationReport.getMessages().forEach(message -> result.addMismatch("API spec validation failure: " + message.toString()));
    }

    private String extractPath(String fullUrl) {
        try {
            return new URL(fullUrl).getPath();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("URL requested for Swagger validation is not valid: " + fullUrl, e);
        }
    }
}
