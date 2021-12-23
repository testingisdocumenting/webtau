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

import java.util.concurrent.atomic.AtomicReference;

public class OpenApi {
    private static final OpenApiValidationMode DEFAULT_MODE = OpenApiValidationMode.ALL;

    private static final AtomicReference<OpenApiSpec> spec = new AtomicReference<>();
    private static final AtomicReference<OpenApiSpecValidator> validator = new AtomicReference<>();
    private static final AtomicReference<OpenApiCoverage> coverage = new AtomicReference<>();

    static final ThreadLocal<OpenApiValidationMode> validationMode = ThreadLocal.withInitial(() -> DEFAULT_MODE);

    synchronized static OpenApiSpecValidator getValidator() {
        if (validator.get() == null) {
            initialize();
        }

        return validator.get();
    }

    synchronized static boolean isCoverageUninitialized() {
        return coverage.get() == null;
    }

    synchronized static OpenApiCoverage getCoverage() {
        if (isCoverageUninitialized()) {
            initialize();
        }

        return coverage.get();
    }

    synchronized static OpenApiSpec getSpec() {
        if (spec.get() == null) {
            initialize();
        }

        return spec.get();
    }

    public static void withoutValidation(Runnable code) {
        withMode(OpenApiValidationMode.NONE, code);
    }

    public static void responseOnlyValidation(Runnable code) {
        withMode(OpenApiValidationMode.RESPONSE_ONLY, code);
    }

    public static void requestOnlyValidation(Runnable code) {
        withMode(OpenApiValidationMode.REQUEST_ONLY, code);
    }

    static void withMode(OpenApiValidationMode mode, Runnable code) {
        validationMode.set(mode);
        try {
            code.run();
        } finally {
            validationMode.set(DEFAULT_MODE);
        }
    }

    static void reset() {
        spec.set(null);
        validator.set(null);
        coverage.set(null);
    }

    static void initialize() {
        if (validationMode.get() == OpenApiValidationMode.NONE) {
            return;
        }

        spec.set(new OpenApiSpec(OpenApiSpecConfig.determineSpecFullPathOrUrl()));
        validator.set(new OpenApiSpecValidator(spec.get(), validationConfig()));
        coverage.set(new OpenApiCoverage(spec.get()));
    }

    private static OpenApiValidationConfig validationConfig() {
        OpenApiValidationConfig config = new OpenApiValidationConfig();
        config.setIgnoreAdditionalProperties(OpenApiSpecConfig.ignoreAdditionalProperties.getAsBoolean());

        return config;
    }
}
