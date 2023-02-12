package scenarios.graphql


import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.reporter.WebTauReport
import org.testingisdocumenting.webtau.utils.JsonUtils

import static org.testingisdocumenting.webtau.WebTauDsl.cfg

class Report {
    static void generateReport(WebTauReport report) {
        def additionalData = [:]
        report.customDataStream.each { additionalData.putAll(it.toMap()) }

        def reportData = [:]
        reportData.graphQLSkippedQueries = additionalData.graphQLSkippedQueries // All queries present in the GraphQL schema but not tested
        reportData.graphQLCoveredQueries = additionalData.graphQLCoveredQueries // All queries present in the GraphQL schema and tested
        reportData.graphQLCoverageSummary = additionalData.graphQLCoverageSummary // Summary of test coverage compared to the GraphQL schema
        reportData.graphQLQueryTimeStatistics = additionalData.graphQLQueryTimeStatistics // Summary of timing by query
        reportData.graphQLCoveredSuccessBranches = additionalData.graphQLCoveredSuccessBranches // All queries present in the GraphQL schema that were hit with a success result
        reportData.graphQLSkippedSuccessBranches = additionalData.graphQLSkippedSuccessBranches // All queries present in the GraphQL schema but not hit with a success result
        reportData.graphQLCoveredErrorBranches = additionalData.graphQLCoveredErrorBranches // All queries present in the GraphQL schema that were hit with an error result
        reportData.graphQLSkippedErrorBranches = additionalData.graphQLSkippedErrorBranches // All queries present in the GraphQL schema but not hit with an error result

        def reportPath = cfg.workingDir.resolve('webtau.graphql-report.json')
        ConsoleOutputs.out('generating report: ', Color.PURPLE, reportPath)

        reportPath.toFile().text = JsonUtils.serializePrettyPrint(reportData)
    }
}
