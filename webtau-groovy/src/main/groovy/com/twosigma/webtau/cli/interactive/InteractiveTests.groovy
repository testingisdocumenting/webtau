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

package com.twosigma.webtau.cli.interactive

import com.twosigma.webtau.TestFile
import com.twosigma.webtau.runner.standalone.StandaloneTest
import com.twosigma.webtau.runner.standalone.StandaloneTestRunner
import groovy.transform.PackageScope

import java.nio.file.Paths

@PackageScope
class InteractiveTests {
    private final StandaloneTestRunner runner
    private final LinkedHashSet<String> testFilePaths

    InteractiveTests(StandaloneTestRunner runner) {
        this.runner = runner
        this.testFilePaths = new LinkedHashSet<>(runner.tests.reportTestEntry.filePath*.toString())
    }

    List<StandaloneTest> refreshScenarios(String filePath) {
        runner.clearRegisteredTests()
        runner.process(new TestFile(Paths.get(filePath)), this)

        return runner.tests
    }

    LinkedHashSet<String> getTestFilePaths() {
        return testFilePaths
    }

    String testFilePathByIdx(int idx) {
        return testFilePaths[idx]
    }

    List<StandaloneTest> findSelectedTests(TestsSelection testSelection) {
        return runner.tests.findAll {
            it.filePath.toString() == testSelection.testFilePath &&
                    it.scenario in testSelection.scenarios
        }
    }
}
