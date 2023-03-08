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

import org.testingisdocumenting.webtau.http.HttpResponse
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult
import org.testingisdocumenting.webtau.persona.Persona
import org.junit.Before
import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*

class OpenApiSpecValidatorTest {
    private final static String GET = "GET"
    private final static String URL = "http://myhost.com:1234/"

    private OpenApiSpecValidator validator
    private static def specLocation = OpenApiSpecLocation.fromStringValue('src/test/resources/test-spec.json')

    @Before
    void setUp() {
        validator = new OpenApiSpecValidator(new OpenApiSpec(specLocation), new OpenApiValidationConfig())
    }

    @Test
    void "invalid responses should generate mismatches"() {
        //This should generate two errors: one for missing mandatoryField and one for invalid type of intField.
        //It should generate no errors for the missing optionalField
        def testResponse = '{"intField": "abc"}'
        def result = validationResult(GET, URL, ok(testResponse))

        code {
            validator.validateApiSpec(result, OpenApiValidationMode.ALL)
        } should throwException(~/Object has missing required properties/)

        def mismatchesAsStr = result.mismatches.collect { it.toString() }
        mismatchesAsStr.size().should == 2
        mismatchesAsStr.should contain(~/does not match any allowed primitive type/)
        mismatchesAsStr.should contain(~/missing required properties/)
    }

    @Test
    void "valid response generates no mismatches"() {
        def testResponse = '{"mandatoryField": "foo"}'
        def result = validationResult(GET, URL, ok(testResponse))

        validator.validateApiSpec(result, OpenApiValidationMode.ALL)

        result.mismatches.size().should == 0
    }

    @Test
    void "should ignore additional properties when specified in a config"() {
        def config = new OpenApiValidationConfig()
        config.setIgnoreAdditionalProperties(true)
        validator = new OpenApiSpecValidator(new OpenApiSpec(specLocation), config)

        def testResponse = '{"mandatoryField": "foo", "extraField": "value"}'
        def result = validationResult(GET, URL, ok(testResponse))

        validator.validateApiSpec(result, OpenApiValidationMode.ALL)

        result.mismatches.size().should == 0
    }

    static HttpResponse ok(String body) {
        def response = new HttpResponse()
        response.setTextContent(body)
        response.setContentType("application/json")
        response.setStatusCode(200)

        return response
    }

    static HttpValidationResult validationResult(method, url, response) {
        def result = new HttpValidationResult(Persona.DEFAULT_PERSONA_ID, method, url, url, null, null)
        result.setResponse(response)

        return result
    }
}
