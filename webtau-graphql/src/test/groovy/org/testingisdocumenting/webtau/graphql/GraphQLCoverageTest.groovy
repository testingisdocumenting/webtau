/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.graphql

import org.junit.Test
import org.testgisdocumenting.webtau.graphql.GraphQLCoverage
import org.testgisdocumenting.webtau.graphql.GraphQLOperationType
import org.testgisdocumenting.webtau.graphql.GraphQLSchema
import org.testingisdocumenting.webtau.http.HttpResponse
import org.testingisdocumenting.webtau.http.json.JsonRequestBody
import org.testingisdocumenting.webtau.http.request.HttpRequestBody
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult
import org.testingisdocumenting.webtau.utils.ResourceUtils
import org.junit.Before

class GraphQLCoverageTest {
    private GraphQLCoverage coverage

    @Before
    void setUp() {
        def schemaUrl = ResourceUtils.resourceUrl('test-schema.graphql')
        coverage = new GraphQLCoverage(new GraphQLSchema(schemaUrl.toString()))
    }

    @Test
    void "should provide non covered operations"() {
        coverage.recordOperation(validationResult('allTasks', GraphQLOperationType.QUERY))
        coverage.recordOperation(validationResult('complete', GraphQLOperationType.MUTATION))

        coverage.nonCoveredOperations().should == ['name' | 'type'    ] {
                                                   _____________________________
                                                    'taskById' | GraphQLOperationType.QUERY
                                                  'uncomplete' | GraphQLOperationType.MUTATION
        }
    }

    static HttpValidationResult validationResult(operationName, operationType) {
        def response = new HttpResponse()
        response.statusCode = 200

        def result = new HttpValidationResult('POST', '/graphql', '/graphql', null, body(operationName, operationType))
        result.setResponse(response)
        return result
    }

    static HttpRequestBody body(operationName, operationType) {
        def type = operationType.name().toLowerCase()
        def payload = [
            query: """
                    $type $operationName {
                      id
                    }
                    """.toString()
        ]
        return new JsonRequestBody(payload)
    }
}
