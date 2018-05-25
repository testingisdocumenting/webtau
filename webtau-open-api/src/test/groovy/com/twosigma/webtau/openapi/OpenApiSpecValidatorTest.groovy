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

package com.twosigma.webtau.openapi

import com.twosigma.webtau.http.HttpResponse
import com.twosigma.webtau.http.validation.HttpValidationResult
import com.twosigma.webtau.utils.ResourceUtils
import org.junit.Before
import org.junit.Test

import static com.twosigma.webtau.Ddjt.contain

class OpenApiSpecValidatorTest {
    private final static String GET = "GET"
    private final static String URL = "http://myhost.com:1234/"

    private OpenApiSpecValidator validator

    @Before
    void setUp() {
        def specUrl = ResourceUtils.resourceUrl("test-spec.json")
        validator = new OpenApiSpecValidator(new OpenApiSpec(specUrl.toString()))
    }

    @Test
    void "invalid responses should generate mismatches"() {
        //This should generate two errors: one for missing mandatoryField and one for invalid type of intField.
        //It should generate no errors for the missing optionalField
        def testResponse = "{\"intField\": \"abc\"}"
        def result = validationResult(GET, URL, ok(testResponse))

        validator.validateApiSpec(result)

        result.mismatches.size().should == 2
        result.mismatches.should contain(~/does not match any allowed primitive type/)
        result.mismatches.should contain(~/missing required properties/)
    }

    @Test
    void "valid response generates no mismatches"() {
        def testResponse = "{\"mandatoryField\": \"foo\"}"
        def result = validationResult(GET, URL, ok(testResponse))

        validator.validateApiSpec(result)

        result.mismatches.size().should == 0
    }

    static HttpResponse ok(String body) {
        def response = new HttpResponse()
        response.setContent(body)
        response.setStatusCode(200)

        return response
    }

    static HttpValidationResult validationResult(method, url, response) {
        def result = new HttpValidationResult(method, url, null)
        result.setResponse(response)

        return result
    }
}
