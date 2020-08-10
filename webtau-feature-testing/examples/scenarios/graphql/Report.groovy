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

import org.testingisdocumenting.webtau.report.ReportDataProviders
import org.testingisdocumenting.webtau.reporter.WebTauReport

class Report {
    static void generateReport(WebTauReport report) {
        def additionalData = [:]
        ReportDataProviders.provide(report.tests)
            .map { it.toMap() }
            .forEach { additionalData.putAll(it) }

        validateSkippedOps(additionalData.graphQLSkippedOperations as Set)
        validateCoveredOps(additionalData.graphQLCoveredOperations as Set)
        validateOperationStatistics(additionalData.graphQLOperationTimeStatistics)
        validateCoverageSummary(additionalData.graphQLCoverageSummary)
    }

    static void validateSkippedOps(Set skippedOps) {
        skippedOps.should == [
            [
                name: 'uncomplete',
                type: 'mutation'
            ]
        ] as Set
    }

    static void validateCoveredOps(Set coveredOps) {
        coveredOps.should == [
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

    static void validateOperationStatistics(opStats) {
        opStats.size().should == 3

        def allTasksQueryStats = opStats.find { it.name == 'allTasks' }
        allTasksQueryStats.shouldNot == null
        allTasksQueryStats.type.should == 'query'
        allTasksQueryStats.statistics.size().shouldBe > 0

        def completeMutationStats = opStats.find { it.name == 'complete' }
        completeMutationStats.shouldNot == null
        completeMutationStats.type.should == 'mutation'
        completeMutationStats.statistics.size().shouldBe > 0

        def taskByIdQueryStats = opStats.find { it.name == 'taskById' }
        taskByIdQueryStats.shouldNot == null
        taskByIdQueryStats.type.should == 'query'
        taskByIdQueryStats.statistics.size().shouldBe > 0
    }

    static void validateCoverageSummary(summary) {
        summary.should == [
            types: [
                mutation: [
                    declaredOperations: 2,
                    coveredOperations: 1,
                    coverage: 0.5
                ],
                query: [
                    declaredOperations: 2,
                    coveredOperations: 2,
                    coverage: 1.0
                ]
            ],
            totalDeclaredOperations: 4,
            totalCoveredOperations: 3,
            coverage: 0.75
        ]
    }
}
