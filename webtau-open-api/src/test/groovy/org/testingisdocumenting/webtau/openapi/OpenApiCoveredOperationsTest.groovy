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

import org.junit.Test

class OpenApiCoveredOperationsTest {
    @Test
    void "collect http calls ids per operation and expose as map for a report"() {
        def covered = new OpenApiCoveredOperations()
        covered.add(new OpenApiOperation('get', '/url/{param}'), 200, 'id1')
        covered.add(new OpenApiOperation('get', '/url/{param}'), 404, 'id2')
        covered.add(new OpenApiOperation('get', '/url/{param}'), 404, 'id3')
        covered.add(new OpenApiOperation('post', '/url/{param}'), 201, 'id4')
        covered.add(new OpenApiOperation('delete', '/url/{param}'), 204, 'id5')

        covered.httpCallIdsByOperationAsMap().should == ['method' | 'url'          | 'httpCallIds'] {
                                                       _____________________________________________
                                                            'get' | '/url/{param}' | ['id1', 'id2', 'id3']
                                                           'post' | '/url/{param}' | ['id4']
                                                         'delete' | '/url/{param}' | ['id5']     }

        def getCalls = [
            ['statusCode': 200, 'httpCallId': 'id1'],
            ['statusCode': 404, 'httpCallId': 'id2'],
            ['statusCode': 404, 'httpCallId': 'id3']
        ]
        def postCalls = [['statusCode': 201, 'httpCallId': 'id4']]
        def deleteCalls = [['statusCode': 204, 'httpCallId': 'id5']]
        covered.httpCallsByOperationAsMap().should == ['method' | 'url'          | 'httpCalls'] {
                                                    _____________________________________________
                                                          'get' | '/url/{param}' | getCalls
                                                         'post' | '/url/{param}' | postCalls
                                                       'delete' | '/url/{param}' | deleteCalls     }

    }
}
