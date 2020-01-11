package listeners

import com.twosigma.webtau.reporter.TestListener
import com.twosigma.webtau.reporter.WebTauReport
import com.twosigma.webtau.reporter.WebTauTest

class CustomTestListener implements TestListener {
    @Override
    void beforeFirstTest() {
    }

    @Override
    void beforeTestRun(WebTauTest test) {
    }

    @Override
    void afterTestRun(WebTauTest test) {
        println("custom test listner for: ${test.scenario}")
    }

    @Override
    void afterAllTests(WebTauReport report) {
    }
}
