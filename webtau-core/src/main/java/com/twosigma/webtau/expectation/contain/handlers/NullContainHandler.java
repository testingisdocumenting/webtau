package com.twosigma.webtau.expectation.contain.handlers;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.contain.ContainAnalyzer;
import com.twosigma.webtau.expectation.contain.ContainHandler;

public class NullContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual == null || expected == null;
    }

    @Override
    public void analyze(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        containAnalyzer.reportMismatch(this, actualPath, actual + " doesn't contain " + expected);
    }
}
