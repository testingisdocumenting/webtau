package org.testingisdocumenting.webtau.reporter;

/**
 * Extracts {@link TestStep} payloads from all test steps at the end of the test and re-attaches it as a test payload.
 */
public class TestResultPayloadExtractorTestListener implements TestListener {
    @Override
    public void afterTestRun(WebTauTest test) {
        TestResultPayloadExtractors.extract(test.getSteps().stream()).forEach(test::addTestResultPayload);
    }
}
