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

import com.twosigma.webtau.http.validation.HttpValidationHandler;
import com.twosigma.webtau.http.validation.HttpValidationResult;

public class OpenApiResponseValidator implements HttpValidationHandler {
    private static final ValidationMode DEFAULT_MODE = ValidationMode.ALL;
    private static final ThreadLocal<ValidationMode> validationMode = ThreadLocal.withInitial(() -> DEFAULT_MODE);

    static void withMode(ValidationMode mode, Runnable code) {
        validationMode.set(mode);
        try {
            code.run();
        } finally {
            validationMode.set(DEFAULT_MODE);
        }
    }

    @Override
    public void validate(HttpValidationResult validationResult) {
        if (validationMode.get().equals(ValidationMode.NONE)) {
            return;
        }

        OpenApi.validator.validateApiSpec(validationResult, validationMode.get());
    }
}
