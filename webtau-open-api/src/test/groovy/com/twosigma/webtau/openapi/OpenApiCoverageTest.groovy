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

    static HttpValidationResult validationResult(method, url) {
        return new HttpValidationResult(method, url, null , null)
    }
}
