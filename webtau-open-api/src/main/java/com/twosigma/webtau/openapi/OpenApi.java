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

public class OpenApi {
    public static final OpenApiSpec spec = new OpenApiSpec(OpenApiSpecConfig.specFullPath());
    public static final OpenApiSpecValidator validator = new OpenApiSpecValidator(spec, validationConfig());

    public static final OpenApiCoverage coverage = new OpenApiCoverage(spec);

    public static void withoutValidation(Runnable code) {
        OpenApiResponseValidator.withMode(ValidationMode.NONE, code);
    }

    public static void responseOnlyValidation(Runnable code) {
        OpenApiResponseValidator.withMode(ValidationMode.RESPONSE_ONLY, code);
    }

    public static void requestOnlyValidation(Runnable code) {
        OpenApiResponseValidator.withMode(ValidationMode.REQUEST_ONLY, code);
    }

    private static OpenApiValidationConfig validationConfig() {
        OpenApiValidationConfig config = new OpenApiValidationConfig();
        config.setIgnoreAdditionalProperties(OpenApiSpecConfig.ignoreAdditionalProperties.getAsBoolean());

        return config;
    }
}
