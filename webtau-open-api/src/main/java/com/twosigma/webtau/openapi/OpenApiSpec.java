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

import com.atlassian.oai.validator.interaction.ApiOperationResolver;
import com.atlassian.oai.validator.model.ApiOperationMatch;
import com.atlassian.oai.validator.model.Request;
import com.twosigma.webtau.utils.UrlUtils;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.util.SwaggerDeserializationResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.String.format;

public class OpenApiSpec {
    private final Swagger api;
    private final ApiOperationResolver apiOperationResolver;
    private final String specUrl;
    private final boolean isSpecDefined;
    private final Map<OpenApiOperation, Set<String>> operationsAndResponses;

    public OpenApiSpec(String specUrl) {
        isSpecDefined = !specUrl.isEmpty();

        this.specUrl = specUrl;
        SwaggerDeserializationResult swaggerParseResult =
                new SwaggerParser().readWithInfo(specUrl, null, true);

        api = swaggerParseResult.getSwagger();
        apiOperationResolver = api != null ? new ApiOperationResolver(api, null) : null;

        if (api == null && isSpecDefined) {
            throw new IllegalArgumentException(
                    format("Unable to load API descriptor from provided %s:\n\t%s",
                            specUrl, swaggerParseResult.getMessages().toString().replace("\n", "\n\t")));
        }

        operationsAndResponses = isSpecDefined ? enumerateOperations() : Collections.emptyMap();
    }

    public boolean isSpecDefined() {
        return isSpecDefined;
    }

    public String getSpecUrl() {
        return specUrl;
    }

    public Stream<OpenApiOperation> availableOperationsStream() {
        return operationsAndResponses.keySet().stream();
    }

    public Optional<OpenApiOperation> findApiOperation(String method, String path) {
        String relativePath = UrlUtils.extractPath(path);

        Request.Method requestMethod = Enum.valueOf(Request.Method.class, method);

        ApiOperationMatch apiOperation = apiOperationResolver.findApiOperation(relativePath, requestMethod);
        if (! apiOperation.isPathFound() || ! apiOperation.isOperationAllowed()) {
            return Optional.empty();
        }

        return Optional.of(new OpenApiOperation(method,
                combineWithBasePath(apiOperation.getApiOperation().getApiPath().original())));
    }

    public Set<String> getDeclaredResponses(OpenApiOperation op) {
        return operationsAndResponses.getOrDefault(op, Collections.emptySet());
    }

    private String combineWithBasePath(String url) {
        return api.getBasePath() != null ?
                UrlUtils.concat(api.getBasePath(), url):
                url;
    }

    private Map<OpenApiOperation, Set<String>> enumerateOperations() {
        Map<OpenApiOperation, Set<String>> operationsAndResponses = new LinkedHashMap<>();

        api.getPaths().forEach((url, path) -> {
            String fullUrl = combineWithBasePath(url);

            if (path.getGet() != null) {
                addOperation(operationsAndResponses, "GET", fullUrl, path.getGet());
            }

            if (path.getPut() != null) {
                addOperation(operationsAndResponses, "PUT", fullUrl, path.getPut());
            }

            if (path.getPost() != null) {
                addOperation(operationsAndResponses, "POST", fullUrl, path.getPost());
            }

            if (path.getDelete() != null) {
                addOperation(operationsAndResponses, "DELETE", fullUrl, path.getDelete());
            }
        });

        return operationsAndResponses;
    }

    private static void addOperation(Map<OpenApiOperation, Set<String>> operationsAndResponses, String method, String fullUrl, Operation operation) {
        Set<String> responseCodes = operation.getResponses() == null ? Collections.emptySet() : operation.getResponses().keySet();
        operationsAndResponses.put(new OpenApiOperation(method, fullUrl), responseCodes);
    }
}
