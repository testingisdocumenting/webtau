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

package com.twosigma.webtau.openapi

import com.twosigma.webtau.http.HttpResponse
import com.twosigma.webtau.http.validation.HttpValidationResult
import com.twosigma.webtau.utils.ResourceUtils
import org.junit.Before
import org.junit.Test

class OpenApiCoverageTest {
    private OpenApiCoverage coverage

    @Before
    void setUp() {
        def specUrl = ResourceUtils.resourceUrl('test-spec.json')
        coverage = new OpenApiCoverage(new OpenApiSpec(specUrl.toString()))
    }

    @Test
    void "should provide non covered operations"() {
        coverage.recordOperation(validationResult('GET', 'http://localhost:8080/customer/3'))
        coverage.recordOperation(validationResult('PUT', 'http://localhost:8080/customer/5'))

        coverage.nonCoveredOperations().should == ['method' | 'url'] {
                                                  _____________________________
                                                      'GET' | '/'
                                                   'DELETE' | '/customer/{id}' }
    }

    @Test
    void "should provide covered and non covered responses"() {
        coverage.recordOperation(validationResult('GET', 200, 'http://localhost:8080/customer/3'))
        coverage.recordOperation(validationResult('GET', 404, 'http://localhost:8080/customer/3'))
        coverage.recordOperation(validationResult('GET', 500, 'http://localhost:8080/customer/3'))

        def expectedCovered = [
            (new OpenApiOperation('GET', '/customer/{id}')): ['default', '200', '4XX'],
            (new OpenApiOperation('DELETE', '/customer/{id}')): [],
            (new OpenApiOperation('GET', '/')): [],
            (new OpenApiOperation('PUT', '/customer/{id}')): [],
        ]
        coverage.coveredResponses().should == expectedCovered

        def nonCovered = coverage.nonCoveredResponses()
        def expectedNonCovered = [
            (new OpenApiOperation('GET', '/customer/{id}')): [],
            (new OpenApiOperation('DELETE', '/customer/{id}')): ['200'],
            (new OpenApiOperation('GET', '/')): ['default', '200', '4XX'],
            (new OpenApiOperation('PUT', '/customer/{id}')): ['200'],
        ]
        coverage.nonCoveredResponses().should == expectedNonCovered
    }

    static HttpValidationResult validationResult(method, url) {
        return validationResult(method, 200, url)
    }

    static HttpValidationResult validationResult(method, statusCode, url) {
        def response = new HttpResponse()
        response.statusCode = statusCode

        def result = new HttpValidationResult(method, url, url, null, null)
        result.setResponse(response)
        return result
    }
}
