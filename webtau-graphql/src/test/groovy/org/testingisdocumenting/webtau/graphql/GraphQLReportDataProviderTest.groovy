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
import org.testgisdocumenting.webtau.graphql.GraphQLReportDataProvider
import org.testgisdocumenting.webtau.graphql.GraphQLSchema
import org.testingisdocumenting.webtau.utils.ResourceUtils

import static org.testingisdocumenting.webtau.graphql.TestUtils.validationResult

class GraphQLReportDataProviderTest {
    def schemaUrl = ResourceUtils.resourceUrl('test-schema.graphql')
    def coverage = new GraphQLCoverage(new GraphQLSchema(schemaUrl.toString()))
    def reportDataProvider = new GraphQLReportDataProvider(coverage)

    @Test
    void "computes timing per operation"() {
        coverage.recordOperation(validationResult('allTasks', GraphQLOperationType.QUERY, 1))
        coverage.recordOperation(validationResult('allTasks', GraphQLOperationType.QUERY, 2))
        coverage.recordOperation(validationResult('allTasks', GraphQLOperationType.QUERY, 3))

        coverage.recordOperation(validationResult('complete', GraphQLOperationType.MUTATION, 2))
        coverage.recordOperation(validationResult('complete', GraphQLOperationType.MUTATION, 4))
        coverage.recordOperation(validationResult('complete', GraphQLOperationType.MUTATION, 6))

        def timeStats = reportDataProvider.provide(null)
            .find {data -> data.getId() == "graphQLOperationTimeStatistics" }
            .getData()
        def expectedStats = [
            [
                name: 'allTasks',
                type: 'query',
                statistics: [
                    mean: 2,
                    min: 1,
                    max: 3,
                    p95: 3,
                    p99: 3
                ]
            ],
            [
                name: 'complete',
                type: 'mutation',
                statistics: [
                    mean: 4,
                    min: 2,
                    max: 6,
                    p95: 6,
                    p99: 6
                ]
            ]
        ]
        timeStats.should == expectedStats
    }
}
