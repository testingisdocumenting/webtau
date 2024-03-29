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

import org.testingisdocumenting.webtau.http.validation.HttpValidationHandler;
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class OpenApiResponseValidator implements HttpValidationHandler {
    @Override
    public void validate(HttpValidationResult validationResult) {
        OpenApiValidationMode mode = OpenApi.validationMode.get();
        if (mode.equals(OpenApiValidationMode.NONE)) {
            return;
        }

        OpenApiSpecValidator validator = OpenApi.getValidator();

        if (!validator.isSpecDefined()) {
            return;
        }

        String modeLabel = validationModeLabel(mode);
        WebTauStep.createAndExecuteStep(
                tokenizedMessage().action("validating").classifier(modeLabel),
                () -> tokenizedMessage().action("validated").classifier(modeLabel),
                () -> validator.validateApiSpec(validationResult, mode));
    }

    private static String validationModeLabel(OpenApiValidationMode mode) {
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
