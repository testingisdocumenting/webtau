package org.testingisdocumenting.webtau.report;

import org.testingisdocumenting.webtau.reporter.TestListener;
import org.testingisdocumenting.webtau.reporter.WebTauReport;

public class ReportGeneratorTestListener implements TestListener {
    @Override
    public void afterAllTests(WebTauReport report) {
        ReportGenerators.generate(report);
    }
}
