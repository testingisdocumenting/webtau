/*
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

package org.testingisdocumenting.webtau.cli.repl

import org.testingisdocumenting.webtau.TestFile
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.runner.standalone.StandaloneTest
import org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner
import groovy.transform.PackageScope

import java.nio.file.Paths
import java.util.regex.Pattern

@PackageScope
class InteractiveTests {
    private final StandaloneTestRunner runner
    private final LinkedHashSet<String> testFilePaths

    InteractiveTests(StandaloneTestRunner runner) {
        this.runner = runner
        this.testFilePaths = new LinkedHashSet<>(runner.tests.test.filePath*.toString())
    }

    List<StandaloneTest> refreshScenarios(String filePath) {
        runner.clearRegisteredTests()
        runner.process(new TestFile(Paths.get(filePath)), this)

        def withError = runner.tests.find { it.hasError() }
        if (withError) {
            ConsoleOutputs.out(Color.RED, withError.exception)
            return []
        }

        return runner.tests
    }

    StandaloneTestRunner getRunner() {
        return runner
    }

    LinkedHashSet<String> getTestFilePaths() {
        return testFilePaths
    }

    String testFilePathByIdx(int idx) {
        return testFilePaths[idx]
    }

    String firstTestFilePathByRegexp(Pattern pattern) {
        return testFilePaths.find { pattern.matcher(it) }
    }

    List<StandaloneTest> findSelectedTests(TestsSelection testSelection) {
        return runner.tests.findAll {
            it.filePath.toString() == testSelection.testFilePath &&
                    it.scenario in testSelection.scenarios
        }
    }
}
