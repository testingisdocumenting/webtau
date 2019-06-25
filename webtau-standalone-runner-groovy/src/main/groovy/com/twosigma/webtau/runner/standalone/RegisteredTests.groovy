package com.twosigma.webtau.runner.standalone

import java.nio.file.Path

class RegisteredTests {
    private List<StandaloneTest> tests = []
    private List<StandaloneTest> exclusiveTests = []

    void add(StandaloneTest test) {
        tests.add(test)
    }

    void addExclusive(StandaloneTest test) {
        exclusiveTests.add(test)
    }

    void clear() {
        tests = []
        exclusiveTests = []
    }

    int count(Closure closure) {
        return tests.count(closure)
    }

    boolean hasExclusiveTests() {
        return !exclusiveTests.isEmpty()
    }

    List<StandaloneTest> getTests() {
        return tests.asImmutable()
    }

    private List<StandaloneTest> testsToRun() {
        return exclusiveTests.isEmpty() ? tests : exclusiveTests
    }

    Map<Path, List<StandaloneTest>> testsByFile() {
        return testsToRun().groupBy { it.filePath }
    }

    List<StandaloneTest> testsToSkip() {
        return (exclusiveTests.isEmpty() ? [] : tests).asImmutable()
    }
}
