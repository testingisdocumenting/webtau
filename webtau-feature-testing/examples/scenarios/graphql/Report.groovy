package scenarios.graphql

import org.testgisdocumenting.webtau.graphql.GraphQLOperationType
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
    }

    static void validateSkippedOps(Set skippedOps) {
        skippedOps.should == [
            [
                name: 'uncomplete',
                type: GraphQLOperationType.MUTATION
            ],
            [
                name: 'allTasks',
                type: GraphQLOperationType.QUERY
            ]
        ] as Set
    }

    static void validateCoveredOps(Set coveredOps) {
        coveredOps.should == [
            [
                name: 'complete',
                type: GraphQLOperationType.MUTATION
            ],
            [
                name: 'taskById',
                type: GraphQLOperationType.QUERY
            ]
        ] as Set
    }

    static void validateOperationStatistics(opStats) {
        opStats.size().should == 2

        def completeMutationStats = opStats.find { it.name == 'complete' }
        completeMutationStats.shouldNot == null
        completeMutationStats.type.should == 'mutation'
        completeMutationStats.statistics.size().shouldBe > 0

        def taskByIdQueryStats = opStats.find { it.name == 'taskById' }
        taskByIdQueryStats.shouldNot == null
        taskByIdQueryStats.type.should == 'query'
        taskByIdQueryStats.statistics.size().shouldBe > 0
    }
}
