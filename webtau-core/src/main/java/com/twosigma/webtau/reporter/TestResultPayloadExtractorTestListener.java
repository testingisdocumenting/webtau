package com.twosigma.webtau.reporter;

/**
 * Extracts {@link TestStep} payloads from all test steps at the end of the test and re-attaches it as a test payload.
 */
public class TestResultPayloadExtractorTestListener implements TestListener {
    @Override
    public void beforeFirstTest() {
    }

    @Override
    public void beforeTestRun(WebTauTest test) {
    }

    @Override
    public void afterTestRun(WebTauTest test) {
        TestResultPayloadExtractors.extract(test.getSteps().stream()).forEach(test::addTestResultPayload);
    }

    @Override
    public void afterAllTests(WebTauReport report) {
    }
}
