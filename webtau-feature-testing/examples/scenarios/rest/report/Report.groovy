package scenarios.rest.report

import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.reporter.WebTauReport

import static org.testingisdocumenting.webtau.WebTauDsl.cfg

class Report {
    static void generateReport(WebTauReport report) {
        def reportPath = cfg.workingDir.resolve('report.txt')

        ConsoleOutputs.out('generating report: ', Color.PURPLE, reportPath)
        reportPath.toFile().text = report.tests.size()
    }
}
