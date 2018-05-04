package com.twosigma.webtau.expectation.contain.handlers;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.contain.ContainAnalyzer;
import com.twosigma.webtau.expectation.contain.ContainHandler;

public class StringContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof String && expected instanceof String;
    }

    @Override
    public void analyze(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        String actualString = (String) actual;
        String expectedString = (String) expected;

        if (!actualString.contains(expectedString)) {
            containAnalyzer.reportMismatch(this, actualPath,
                    "             actual: " + actualString + "\n" +
                            "expected to contain: " + expectedString);
        }
    }
}
