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
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.util.SwaggerDeserializationResult;

import static java.lang.String.format;

public class OpenApiSpec {
    private final Swagger api;
    private final ApiOperationResolver apiOperationResolver;
    private final String specUrl;
    private final boolean isSpecDefined;

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
    }

    public boolean isSpecDefined() {
        return isSpecDefined;
    }

    public String getSpecUrl() {
        return specUrl;
    }

    public ApiOperationMatch findApiOperation(String path, Request.Method method) {
        return apiOperationResolver.findApiOperation(path, method);
    }
}
