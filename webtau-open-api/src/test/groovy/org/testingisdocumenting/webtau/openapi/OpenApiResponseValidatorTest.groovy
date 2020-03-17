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

package org.testingisdocumenting.webtau.openapi

import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.http.HttpHeader
import org.testingisdocumenting.webtau.http.HttpResponse
import org.testingisdocumenting.webtau.http.json.JsonRequestBody
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandlers
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult
import org.testingisdocumenting.webtau.reporter.StepReporter
import org.testingisdocumenting.webtau.reporter.StepReporters
import org.testingisdocumenting.webtau.reporter.TestStep
import org.testingisdocumenting.webtau.utils.ResourceUtils
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.WebTauCore.*

class OpenApiResponseValidatorTest implements StepReporter {
    List<String> stepMessages = []

    @BeforeClass
    static void init() {
        OpenApiSpecConfig.specUrl.set('test-manual', Paths.get(
                ResourceUtils.resourceUrl("test-spec.json").toURI()))
        WebTauConfig.cfg.get('openApiSpecUrl').toString().should contain('test-spec.json')
    }

    @Before
    void initiateStepReporter() {
        stepMessages.clear()
        StepReporters.add(this)
    }

    @After
    void unregisterStepReporter() {
        StepReporters.remove(this)
    }

    @Test
    void "should be auto registered as custom validator"() {
        HttpValidationResult validationResult = createValidationResult()

        def expectedError = "schema is not valid:\n" +
                "ERROR - No request body is expected for GET on path '/customer/{id}'.: []\n" +
                "ERROR - Object instance has properties which are not allowed by the schema: [\"key\"]: []\n" +
                "ERROR - Object has missing required properties ([\"mandatoryField\"]): []"

        code {
            HttpValidationHandlers.validate(validationResult)
        } should throwException(expectedError)

        validationResult.mismatches.size().should == 3

        stepMessages.should == ["failed validating request and response : " + expectedError]
    }

    @Test
    void "should allow to disable validation for a code block"() {
        HttpValidationResult validationResult = createValidationResult()

        OpenApi.withoutValidation {
            HttpValidationHandlers.validate(validationResult)
        }

        validationResult.mismatches.size().should == 0
        stepMessages.should == []
    }

    @Test
    void "should allow to validate only responses for a code block"() {
        HttpValidationResult validationResult = createValidationResult()

        def expectedError = "schema is not valid:\n" +
                "ERROR - Object instance has properties which are not allowed by the schema: [\"key\"]: []\n" +
                "ERROR - Object has missing required properties ([\"mandatoryField\"]): []"

        code {
            OpenApi.responseOnlyValidation {
                HttpValidationHandlers.validate(validationResult)
            }
        } should throwException(expectedError)

        validationResult.mismatches.size().should == 2
        stepMessages.should == ["failed validating response : " + expectedError]
    }

    @Test
    void "should allow to validate only requests for a code block"() {
        HttpValidationResult validationResult = createValidationResult()

        def expectedError = "schema is not valid:\n" +
                "ERROR - No request body is expected for GET on path '/customer/{id}'.: []"
        code {
            OpenApi.requestOnlyValidation() {
                HttpValidationHandlers.validate(validationResult)
            }
        } should throwException(expectedError)

        validationResult.mismatches.size().should == 1
        stepMessages.should == ["failed validating request : " + expectedError]
    }

    private static HttpValidationResult createValidationResult() {
        def validationResult = new HttpValidationResult('GET', '/customer/2', '/customer/2',
                new HttpHeader([:]), new JsonRequestBody([:]))
        validationResult.setResponse(response())
        validationResult
    }

    private static HttpResponse response() {
        def response = new HttpResponse()
        response.setTextContent('{"key": "value"}')
        response.setStatusCode(200)
        response
    }

    @Override
    void onStepStart(TestStep step) {
    }

    @Override
    void onStepSuccess(TestStep step) {
        stepMessages << step.completionMessage.toString()
    }

    @Override
    void onStepFailure(TestStep step) {
        stepMessages << step.completionMessage.toString()
    }
}
