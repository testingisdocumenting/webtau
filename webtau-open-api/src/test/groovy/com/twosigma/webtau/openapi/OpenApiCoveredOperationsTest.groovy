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

import org.junit.Test

class OpenApiCoveredOperationsTest {
    @Test
    void "collect http calls ids per operation and expose as map for a report"() {
        def covered = new OpenApiCoveredOperations()
        covered.add(new OpenApiOperation('get', '/url/{param}'), 'id1')
        covered.add(new OpenApiOperation('get', '/url/{param}'), 'id2')
        covered.add(new OpenApiOperation('post', '/url/{param}'), 'id3')
        covered.add(new OpenApiOperation('delete', '/url/{param}'), 'id4')

        covered.httpCallIdsByOperationAsMap().should == ['method' | 'url'          | 'httpCallIds'] {
                                                       _____________________________________________
                                                            'get' | '/url/{param}' | ['id1', 'id2']
                                                           'post' | '/url/{param}' | ['id3']
                                                         'delete' | '/url/{param}' | ['id4']     }
    }
}
