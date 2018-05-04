package com.twosigma.webtau.runner.standalone

import java.nio.file.Path

class StandaloneTestListenerAdapter implements StandaloneTestListener {
    @Override
    void beforeFirstTest() {
    }

    @Override
    void beforeScriptParse(Path scriptPath) {
    }

    @Override
    void beforeTestRun(StandaloneTest test) {
    }

    @Override
    void afterTestRun(StandaloneTest test) {
    }

    @Override
    void afterAllTests() {
    }
}
