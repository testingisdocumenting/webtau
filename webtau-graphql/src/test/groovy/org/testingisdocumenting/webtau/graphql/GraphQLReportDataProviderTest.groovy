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

import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.utils.ResourceUtils

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.graphql.TestUtils.validationResult

class GraphQLReportDataProviderTest {
    def schemaUrl = ResourceUtils.resourceUrl('test-schema.graphql')
    def coverage = new GraphQLCoverage(new GraphQLSchema(Paths.get(schemaUrl.toURI()).toString()))
    def reportDataProvider = new GraphQLReportDataProvider({ coverage })

    @Before
    void injectDummyData() {
        coverage.recordQuery(validationResult('allTasks', GraphQLQueryType.QUERY, 1))
        coverage.recordQuery(validationResult('allTasks', GraphQLQueryType.QUERY, 2))
        coverage.recordQuery(validationResult('allTasks', GraphQLQueryType.QUERY, 3))

        coverage.recordQuery(validationResult('complete', GraphQLQueryType.MUTATION, 2))
        coverage.recordQuery(validationResult('complete', GraphQLQueryType.MUTATION, 4))
        coverage.recordQuery(validationResult('complete', GraphQLQueryType.MUTATION, 6))
    }

    @Test
    void "computes timing per query"() {
        def timeStats = reportDataProvider.provide(null)
            .find {it.getId() == "graphQLQueryTimeStatistics" }
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

    @Test
    void "computes coverage summary"() {
        def summary = reportDataProvider.provide(null)
        .find { it.getId() == "graphQLCoverageSummary" }
        .getData()

        summary.should == [
            types: [
                mutation: [
                    declaredQueries: 2,
                    coveredQueries: 1,
                    coverage: 0.5
                ],
                query: [
                    declaredQueries: 2,
                    coveredQueries: 1,
                    coverage: 0.5
                ]
            ],
            totalDeclaredQueries: 4,
            totalCoveredQueries: 2,
            coverage: 0.5
        ]
    }
}
