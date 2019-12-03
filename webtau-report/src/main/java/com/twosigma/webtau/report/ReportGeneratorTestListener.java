package com.twosigma.webtau.report;

import com.twosigma.webtau.reporter.TestListener;
import com.twosigma.webtau.reporter.WebTauReport;
import com.twosigma.webtau.reporter.WebTauTest;

public class ReportGeneratorTestListener implements TestListener {
    @Override
    public void beforeFirstTest() {
    }

    @Override
    public void beforeTestRun(WebTauTest test) {
    }

    @Override
    public void afterTestRun(WebTauTest test) {
    }

    @Override
    public void afterAllTests(WebTauReport report) {
        ReportGenerators.generate(report);
    }
}
