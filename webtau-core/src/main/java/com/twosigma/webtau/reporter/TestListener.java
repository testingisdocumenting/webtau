package com.twosigma.webtau.reporter;

/**
 * Lowest common denominator for Groovy Standalone Tests, JUnit4, JUnit5, etc
 * Core usage is to print current test name
 */
public interface TestListener {
    void beforeFirstTest();

    void beforeTestRun(WebTauTest test);

    void afterTestRun(WebTauTest test);

    void afterAllTests(WebTauReport report);
}
