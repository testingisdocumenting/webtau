/*
 * Copyright 2021 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.webtau.runner.standalone

import java.nio.file.Path
import java.util.stream.Stream

class RegisteredTests {
    private List<StandaloneTest> tests = []
    private List<StandaloneTest> exclusiveTests = []

    void add(StandaloneTest test) {
        tests.add(test)
    }

    void addAsFirstTestWithinFile(StandaloneTest test) {
        def idx = tests.findIndexOf {it.filePath == test.filePath }
        if (idx == -1) {
            tests.add(test)
        } else {
            tests.add(idx, test)
        }
    }

    void addExclusive(StandaloneTest test) {
        exclusiveTests.add(test)
    }

    void clear() {
        tests = []
        exclusiveTests = []
    }

    int size() {
        return tests.size() + exclusiveTests.size()
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

    Stream<StandaloneTest> getTestsAndExclusiveTestsStream() {
        return Stream.concat(exclusiveTests.stream(), tests.stream())
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
