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

class WebTauDataFeaturesTest {
    private static WebTauEndToEndTestRunner testRunner

    @BeforeClass
    static void init() {
        testRunner = new WebTauEndToEndTestRunner()
    }

    @Test
    void "reading data"() {
        runCli('readingData.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "extract snippets"() {
        extractCodeSnippets(
                'readingData', 'examples/scenarios/data/readingData.groovy', [
                'csvTable.groovy': 'csv table data',
                'csvTableAutoConverted.groovy': 'csv table data auto converted',
                'listOfMaps.groovy': 'csv list of maps data',
                'listOfMapsAutoConverted.groovy': 'csv list of maps data auto converted',
                'jsonList.groovy': 'json list',
                'jsonMap.groovy': 'json map'
        ])
    }

    private static void runCli(String testName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/data/$testName",
                configFileName.isEmpty() ? "" : "scenarios/data/$configFileName", additionalArgs)
    }
}
