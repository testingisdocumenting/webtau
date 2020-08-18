package scenarios.graphql


import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.report.ReportDataProviders
import org.testingisdocumenting.webtau.reporter.WebTauReport
import org.testingisdocumenting.webtau.utils.JsonUtils

import static org.testingisdocumenting.webtau.WebTauDsl.cfg

class Report {
    static void generateReport(WebTauReport report) {
        def additionalData = [:]
        ReportDataProviders.provide(report.tests)
            .map { it.toMap() }
            .forEach { additionalData.putAll(it) }

        def reportData = [:]
        reportData.graphQLSkippedQueries = additionalData.graphQLSkippedQueries // All queries present in the GraphQL schema but not tested
        reportData.graphQLCoveredQueries = additionalData.graphQLCoveredQueries // All queries present in the GraphQL schema and tested
        reportData.graphQLCoverageSummary = additionalData.graphQLCoverageSummary // Summary of test coverage compared to the GraphQL schema
        reportData.graphQLQueryTimeStatistics = additionalData.graphQLQueryTimeStatistics // Summary of timing by query

        def reportPath = cfg.workingDir.resolve('webtau.graphql-report.json')
        ConsoleOutputs.out('generating report: ', Color.PURPLE, reportPath)

        reportPath.toFile().text = JsonUtils.serializePrettyPrint(reportData)
    }
}
