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

package org.testingisdocumenting.webtau.openapi

import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.http.HttpHeader
import org.testingisdocumenting.webtau.http.HttpResponse
import org.testingisdocumenting.webtau.http.json.JsonRequestBody
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandlers
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult
import org.testingisdocumenting.webtau.persona.Persona

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*

class OpenApiResponseValidatorTest {
    @BeforeClass
    static void init() {
        def specLocation = OpenApiSpecLocation.fromStringValue('src/test/resources/test-spec.json')

        OpenApiSpecConfig.specUrl.set('test-manual', specLocation.getAsString())
        WebTauConfig.cfg.get('openApiSpecUrl').toString().should contain('test-spec.json')
    }

    @Test
    void "should be auto registered as custom validator"() {
        HttpValidationResult validationResult = createValidationResult()

        def expectedError = "schema is not valid"
        def messageOne = "GET /customer/2: No request body is expected but one was found"
        def messageTwo = "GET /customer/2: Object instance has properties which are not allowed by the schema: [\"key\"]"
        def messageThree = "GET /customer/2: Object has missing required properties ([\"mandatoryField\"])"

        runExpectExceptionAndValidateOutput(AssertionError, containAll(expectedError, messageOne, messageTwo, messageThree)) {
            HttpValidationHandlers.validate(validationResult)
        }

        validationResult.mismatches.size().should == 3
    }

    @Test
    void "should allow to disable validation for a code block"() {
        HttpValidationResult validationResult = createValidationResult()

        runAndValidateOutput("") {
            OpenApi.withoutValidation {
                HttpValidationHandlers.validate(validationResult)
            }
        }

        validationResult.mismatches.size().should == 0
    }

    @Test
    void "should allow to validate only responses for a code block"() {
        HttpValidationResult validationResult = createValidationResult()

        def expectedError = "schema is not valid:"
        def messageOne = "GET /customer/2: Object instance has properties which are not allowed by the schema: [\"key\"]"
        def messageTwo = "GET /customer/2: Object has missing required properties ([\"mandatoryField\"])"

        runExpectExceptionAndValidateOutput(AssertionError, containAll(expectedError, messageOne, messageTwo)) {
            OpenApi.responseOnlyValidation {
                HttpValidationHandlers.validate(validationResult)
            }
        }

        validationResult.mismatches.size().should == 2
    }

    @Test
    void "should allow to validate only requests for a code block"() {
        HttpValidationResult validationResult = createValidationResult()

        def expectedError = "GET /customer/2: No request body is expected but one was found"

        runExpectExceptionAndValidateOutput(AssertionError, contain(expectedError)) {
            OpenApi.requestOnlyValidation() {
                HttpValidationHandlers.validate(validationResult)
            }
        }

        validationResult.mismatches.size().should == 1
    }

    private static HttpValidationResult createValidationResult() {
        def validationResult = new HttpValidationResult(Persona.DEFAULT_PERSONA_ID,
                'GET', '/customer/2', '/customer/2',
                new HttpHeader([:]), new JsonRequestBody([:]))
        validationResult.setResponse(response())
        validationResult
    }

    private static HttpResponse response() {
        def response = new HttpResponse()
        response.setTextContent('{"key": "value"}')
        response.setContentType('application/json')
        response.setStatusCode(200)
        response
    }
}
