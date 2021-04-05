/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.openapi

import org.junit.Test
import org.testingisdocumenting.webtau.http.HttpTestBase
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandler
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandlers
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.webtau.http.Http.*

class OpenApiHttpOperationProviderTest extends HttpTestBase {
    @Test
    void "should provide operation id based on spec"() {
        def specLocation = OpenApiSpecLocation.fromStringValue('src/test/resources/test-spec.json')
        OpenApiSpecConfig.specUrl.set('test-manual', specLocation.getAsString())

        def validationHandler = new TestHttpValidationHandler()

        HttpValidationHandlers.withAdditionalHandler(validationHandler) {
            code {
                http.get("/customer/123") {
                }
            } should throwException(~/schema is not valid/)
        }

        validationHandler.operationId.should == 'GET /customer/{id}'
    }

    private static class TestHttpValidationHandler implements HttpValidationHandler {
        String operationId

        @Override
        void validate(HttpValidationResult validationResult) {
            operationId = validationResult.getOperationId()
        }
    }
}
