package com.twosigma.webtau.reporter;

/**
 * Lowest common denominator for Groovy Standalone Tests, JUnit4, JUnit5
 */
public interface TestListener {
    /**
     * before any test is ran
     */
    default void beforeFirstTest() {}

    /**
     * before test code is invoked
     * @param test test
     */
    default void beforeTestRun(WebTauTest test) {}

    /**
     * after test code is invoked
     * @param test test
     */
    default void afterTestRun(WebTauTest test) {}

    /**
     * after all the tests are invoked
     * @param report report
     */
    default void afterAllTests(WebTauReport report) {}

    /**
     * after test is ran but before its first statement.
     * executed code in this listener is considered to be part of a test.
     * @param test test
     */
    default void beforeFirstTestStatement(WebTauTest test) {}

    /**
     * right before test considered to be complete. May not be executed if the test didn't reach the last statement.
     * executed code in this listener is considered to be part of a test.
     * @param test test
     */
    default void afterLastTestStatement(WebTauTest test) {}
}
