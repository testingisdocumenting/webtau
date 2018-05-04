package com.twosigma.webtau.expectation.contain;

import com.twosigma.webtau.expectation.ActualPath;

public interface ContainHandler {
    boolean handle(Object actual, Object expected);

    void analyze(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected);
}
