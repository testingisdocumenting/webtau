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

import com.twosigma.webtau.http.validation.HttpValidationHandler;
import com.twosigma.webtau.http.validation.HttpValidationResult;
import com.twosigma.webtau.reporter.TestStep;

import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.classifier;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class OpenApiRequestResponseValidator implements HttpValidationHandler {
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
        ValidationMode mode = OpenApiRequestResponseValidator.validationMode.get();
        if (mode.equals(ValidationMode.NONE)) {
            return;
        }

        if (!OpenApi.validator.isSpecDefined()) {
            return;
        }

        String modeLabel = validationModeLabel(mode);
        TestStep.createAndExecuteStep(null,
                tokenizedMessage(action("validating"), classifier(modeLabel)),
                () -> tokenizedMessage(action("validated"), classifier(modeLabel)),
                () -> OpenApi.validator.validateApiSpec(validationResult, mode));
    }

    private static String validationModeLabel(ValidationMode mode) {
        switch (mode) {
            case ALL:
                return "request and response";
            case REQUEST_ONLY:
                return "request";
            case RESPONSE_ONLY:
                return "response";
            case NONE:
            default:
                return "NA";
        }
    }
}
