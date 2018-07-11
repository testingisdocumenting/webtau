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

import com.twosigma.webtau.http.HttpRequestHeader
import com.twosigma.webtau.http.HttpResponse
import com.twosigma.webtau.http.json.JsonRequestBody
import com.twosigma.webtau.http.validation.HttpValidationHandlers
import com.twosigma.webtau.http.validation.HttpValidationResult
import com.twosigma.webtau.utils.ResourceUtils
import org.junit.BeforeClass
import org.junit.Test

class OpenApiResponseValidatorTest {
    @BeforeClass
    static void init() {
        OpenApiSpecConfig.openApiSpecUrl.set('test-manual', ResourceUtils.resourceUrl("test-spec.json"))
    }

    @Test
    void "should be auto registered as custom validator"() {
        HttpValidationResult validationResult = createValidationResult()

        HttpValidationHandlers.validate(validationResult)
        validationResult.mismatches.size().shouldBe > 0
    }

    @Test
    void "should allow to disable validation for a specifieid code"() {
        HttpValidationResult validationResult = createValidationResult()

        OpenApi.withoutValidation {
            HttpValidationHandlers.validate(validationResult)
        }

        validationResult.mismatches.size() == 0
    }

    private static HttpValidationResult createValidationResult() {
        def validationResult = new HttpValidationResult('GET', '/customer/2',
                new HttpRequestHeader([:]), new JsonRequestBody([:]))
        validationResult.setResponse(response())
        validationResult
    }

    private static HttpResponse response() {
        def response = new HttpResponse()
        response.setTextContent('{"key": "value"}')
        response.setStatusCode(200)
        response
    }
}
