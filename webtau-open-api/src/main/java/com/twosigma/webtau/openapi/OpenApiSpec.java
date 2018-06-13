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

import com.atlassian.oai.validator.interaction.ApiOperationResolver;
import com.atlassian.oai.validator.model.ApiOperationMatch;
import com.atlassian.oai.validator.model.Request;
import com.twosigma.webtau.http.HttpUrl;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.util.SwaggerDeserializationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

public class OpenApiSpec {
    private final Swagger api;
    private final ApiOperationResolver apiOperationResolver;
    private final String specUrl;
    private final boolean isSpecDefined;
    private final List<OpenApiOperation> operations;

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

        operations = enumerateOperations();
    }

    public boolean isSpecDefined() {
        return isSpecDefined;
    }

    public String getSpecUrl() {
        return specUrl;
    }

    public Stream<OpenApiOperation> availableOperationsStream() {
        return operations.stream();
    }

    public Optional<OpenApiOperation> findApiOperation(String method, String path) {
        String relativePath = HttpUrl.extractPath(path);

        Request.Method requestMethod = Enum.valueOf(Request.Method.class, method);

        ApiOperationMatch apiOperation = apiOperationResolver.findApiOperation(relativePath, requestMethod);
        if (! apiOperation.isPathFound() || ! apiOperation.isOperationAllowed()) {
            return Optional.empty();
        }

        return Optional.of(new OpenApiOperation(method,
                combineWithBasePath(apiOperation.getApiOperation().getApiPath().original())));
    }

    private Stream<OpenApiOperation> createMethodAndPath(String url, Path path) {
        List<OpenApiOperation> result = new ArrayList<>();
        String fullUrl = combineWithBasePath(url);

        if (path.getGet() != null) {
            result.add(new OpenApiOperation("GET", fullUrl));
        }

        if (path.getPut() != null) {
            result.add(new OpenApiOperation("PUT", fullUrl));
        }

        if (path.getPost() != null) {
            result.add(new OpenApiOperation("POST", fullUrl));
        }

        if (path.getDelete() != null) {
            result.add(new OpenApiOperation("DELETE", fullUrl));
        }

        return result.stream();
    }

    private String combineWithBasePath(String url) {
        return api.getBasePath() != null ?
                HttpUrl.concat(api.getBasePath(), url):
                url;
    }

    private List<OpenApiOperation> enumerateOperations() {
        return api.getPaths()
                .entrySet()
                .stream()
                .flatMap(e -> createMethodAndPath(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}
