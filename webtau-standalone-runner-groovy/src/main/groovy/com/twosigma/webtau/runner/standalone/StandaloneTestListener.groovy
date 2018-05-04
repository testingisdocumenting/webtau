package com.twosigma.webtau.runner.standalone

import java.nio.file.Path

interface StandaloneTestListener {
    void beforeFirstTest()
    void beforeScriptParse(Path scriptPath)
    void beforeTestRun(StandaloneTest test)
    void afterTestRun(StandaloneTest test)
    void afterAllTests()
}
