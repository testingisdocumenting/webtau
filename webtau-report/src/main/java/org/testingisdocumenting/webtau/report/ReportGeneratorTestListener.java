package com.twosigma.webtau.report;

import com.twosigma.webtau.reporter.TestListener;
import com.twosigma.webtau.reporter.WebTauReport;

public class ReportGeneratorTestListener implements TestListener {
    @Override
    public void afterAllTests(WebTauReport report) {
        ReportGenerators.generate(report);
    }
}
