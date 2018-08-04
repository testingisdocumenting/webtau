package scenarios.rest.report

import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.report.ReportTestEntries

import static com.twosigma.webtau.WebTauDsl.cfg

class Report {
    static void generateReport(ReportTestEntries entries) {
        def reportPath = cfg.workingDir.resolve('report.txt')

        ConsoleOutputs.out('generating report: ', Color.PURPLE, reportPath)
        reportPath.toFile().text = entries.size()
    }
}
