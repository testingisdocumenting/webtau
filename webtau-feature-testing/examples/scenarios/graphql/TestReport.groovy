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

package scenarios.graphql

import org.testingisdocumenting.webtau.reporter.WebTauReport

class TestReport {
    static void generateReport(WebTauReport report) {
        def additionalData = report.customDataStream.collect { it.toMap() }

        validateSkippedQueries(additionalData.graphQLSkippedQueries as Set)
        validateCoveredQueries(additionalData.graphQLCoveredQueries as Set)
        validateCoveredSuccessBranches(additionalData.graphQLCoveredSuccessBranches as Set)
        validateSkippedSuccessBranches(additionalData.graphQLSkippedSuccessBranches as Set)
        validateCoveredErrorBranches(additionalData.graphQLCoveredErrorBranches as Set)
        validateSkippedErrorBranches(additionalData.graphQLSkippedErrorBranches as Set)
        validateQueryStatistics(additionalData.graphQLQueryTimeStatistics)
        validateCoverageSummary(additionalData.graphQLCoverageSummary)
    }

    static void validateSkippedQueries(Set skippedQueries) {
        skippedQueries.should == [
            [
                name: 'uncomplete',
                type: 'mutation'
            ],
            [
                name: 'weather',
                type: 'query'
            ]
        ] as Set
    }

    static void validateCoveredQueries(Set coveredQueries) {
        coveredQueries.should == [
            [
                name: 'allTasks',
                type: 'query'
            ],
            [
                name: 'complete',
                type: 'mutation'
            ],
            [
                name: 'taskById',
                type: 'query'
            ]
        ] as Set
    }

    static void validateCoveredSuccessBranches(Set coveredQueries) {
        coveredQueries.should == [
            [
                name: 'allTasks',
                type: 'query'
            ],
            [
                name: 'complete',
                type: 'mutation'
            ],
            [
                name: 'taskById',
                type: 'query'
            ]
        ] as Set
    }

    static void validateSkippedSuccessBranches(Set skippedQueries) {
        skippedQueries.should == [
            [
                name: 'weather',
                type: 'query'
            ],
            [
                name: 'uncomplete',
                type: 'mutation'
            ]
        ] as Set
    }

    static void validateCoveredErrorBranches(Set coveredQueries) {
        coveredQueries.should == [
                [
                        name: 'complete',
                        type: 'mutation'
                ]
        ] as Set
    }

    static void validateSkippedErrorBranches(Set skippedQueries) {
        skippedQueries.should == [
            [
                name: 'allTasks',
                type: 'query'
            ],
            [
                name: 'uncomplete',
                type: 'mutation'
            ],
            [
                name: 'taskById',
                type: 'query'
            ],
            [
                name: 'weather',
                type: 'query'
            ]
        ] as Set
    }

    static void validateQueryStatistics(queryStats) {
        queryStats.size().should == 3

        def allTasksQueryStats = queryStats.find { it.name == 'allTasks' }
        allTasksQueryStats.shouldNot == null
        allTasksQueryStats.type.should == 'query'
        allTasksQueryStats.statistics.size().shouldBe > 0

        def completeMutationStats = queryStats.find { it.name == 'complete' }
        completeMutationStats.shouldNot == null
        completeMutationStats.type.should == 'mutation'
        completeMutationStats.statistics.size().shouldBe > 0

        def taskByIdQueryStats = queryStats.find { it.name == 'taskById' }
        taskByIdQueryStats.shouldNot == null
        taskByIdQueryStats.type.should == 'query'
        taskByIdQueryStats.statistics.size().shouldBe > 0
    }

    static void validateCoverageSummary(summary) {
        summary.should == [
            types: [
                mutation: [
                    declaredQueries: 2,
                    coveredQueries: 1,
                    coverage: 0.5
                ],
                query: [
                    declaredQueries: 3,
                    coveredQueries: 2,
                    coverage: (double) 2.0 / 3.0
                ]
            ],
            totalDeclaredQueries: 5,
            totalCoveredQueries: 3,
            coverage: 0.6,
            successBranchCoverage: 0.6,
            errorBranchCoverage: 0.2,
            branchCoverage: 0.4,
        ]
    }
}
