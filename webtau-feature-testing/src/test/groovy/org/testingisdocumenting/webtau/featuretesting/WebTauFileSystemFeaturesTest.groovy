/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.featuretesting

import org.junit.BeforeClass
import org.junit.Test

import static org.testingisdocumenting.webtau.featuretesting.FeaturesDocArtifactsExtractor.extractCodeSnippets

class WebTauFileSystemFeaturesTest {
    private static WebTauEndToEndTestRunner testRunner

    @BeforeClass
    static void init() {
        testRunner = new WebTauEndToEndTestRunner()
    }

    @Test
    void "files"() {
        runCli('files.groovy', 'webtau.cfg.groovy', "--url=${SpringBootDemoAppUrl.baseUrl}")
    }

    @Test
    void "copy"() {
        runCli('copy.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "dirs"() {
        runCli('dirs.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "archive"() {
        runCli('archive.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "temp"() {
        runCli('temp.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "extract snippets"() {
        extractCodeSnippets(
                'fsCopy', 'examples/scenarios/fs/copy.groovy', [
                'copyFileToTempDir.groovy': 'copy file to temp dir',
                'copyFileToDir.groovy': 'copy file to a dir',
                'copyFileToFile.groovy': 'copy file to a different file'
        ])

        extractCodeSnippets(
                'fsFileContent', 'examples/scenarios/fs/files.groovy', [
                'createFile.groovy': 'create file using string path',
                'readFile.groovy': 'read file using string path',
                'replaceFile.groovy': 'replace file content using regexp'
        ])
    }

    private static void runCli(String testName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/fs/$testName",
                configFileName.isEmpty() ? "" : "scenarios/fs/$configFileName", additionalArgs)
    }
}
