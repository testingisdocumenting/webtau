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

import com.twosigma.webtau.utils.ResourceUtils
import org.junit.Test

class OpenApiSpecTest {
    @Test
    void "should find operation by method and path"() {
        OpenApiSpec apiSpec = createSpec('test-spec.json')

        def optionalOperation = apiSpec.findApiOperation('GET', '/customer/10')
        optionalOperation.isPresent().should == true

        optionalOperation.get().method.should == 'GET'
        optionalOperation.get().url.should == '/customer/{id}'
    }

    @Test
    void "correctly handles basepath as slash"() {
        OpenApiSpec apiSpec = createSpec('test-spec-with-slash-base-path.json')

        def optionalOperation = apiSpec.findApiOperation('GET', '/')
        optionalOperation.isPresent().should == true

        optionalOperation.get().url.should == '/'
    }

    private static OpenApiSpec createSpec(String specPath) {
        def specUrl = ResourceUtils.resourceUrl(specPath)
        def apiSpec = new OpenApiSpec(specUrl.toString())
        apiSpec
    }
}
