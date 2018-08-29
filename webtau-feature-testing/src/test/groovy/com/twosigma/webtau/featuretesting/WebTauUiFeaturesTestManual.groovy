/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.featuretesting

import com.twosigma.webtau.utils.FileUtils
import com.twosigma.webtau.utils.ResourceUtils
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Paths

import static com.twosigma.webtau.featuretesting.FeaturesDocArtifactsExtractor.extractHtml

// TODO: manual runs for now while figuring selenium driver auto download
class WebTauUiFeaturesTestManual {
    private static WebTauTestRunner testRunner

    @BeforeClass
    static void init() {
        testRunner = new WebTauTestRunner()

        def testServer = testRunner.testServer
        testServer.registerGet("/search", htmlResponse('search.html'))
        testServer.registerGet("/finders-and-filters", htmlResponse('finders-and-filters.html'))

        testRunner.startTestServer()
    }

    @AfterClass
    static void cleanup() {
        testRunner.stopTestServer()
    }

    @Test
    void "extract html snippets"() {
        def html = extractHtml("finders-and-filters.html", "#menu")
        FileUtils.writeTextContent(Paths.get("doc-artifacts/snippets/menu.html"), html)
    }

    @Test
    void "open browser and assert"() {
        runCli('basic.groovy', 'webtau.cfg')
    }

    @Test
    void "lazy declaration"() {
        runCli('basicDeclareFirst.groovy', 'webtau.cfg')
    }

    @Test
    void "finders and filters"() {
        runCli('findersFilters.groovy', 'webtau.cfg')
    }

    @Test
    void "finders and filters extract snippets"() {
        extractSnippets(
                'doc-artifacts/snippets/finders-filters',
                'examples/scenarios/ui/findersFilters.groovy', [
                'byCss.groovy': 'by css id',
                'byCssFirstMatched.groovy': 'by css first matched',
                'byCssAndFilterByNumber.groovy': 'by css and filter by number',
                'byCssAndFilterByText.groovy': 'by css and filter by text',
                'byCssAndFilterByRegexp.groovy': 'by css and filter by regexp',
        ])

        FeaturesDocArtifactsExtractor.extractAndSaveHtml('finders-and-filters.html', '#simple-case',
                'finders-and-filters-flat-menu')
    }

    private static void extractSnippets(String extractedPath, String inputName, Map<String, String> scenarioToOutputFile) {
        def artifactsRoot = Paths.get(extractedPath)

        def script = FileUtils.fileTextContent(Paths.get(inputName))

        scenarioToOutputFile.each { outputFileName, scenario ->
            def extracted = FeaturesDocArtifactsExtractor.extractScenarioBody(script, scenario)
            FileUtils.writeTextContent(artifactsRoot.resolve(outputFileName), extracted)
        }

    }

    private static void runCli(String restTestName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/ui/$restTestName",
                "scenarios/ui/$configFileName", additionalArgs)
    }

    private static TestServerHtmlResponse htmlResponse(String resourceName) {
        new TestServerHtmlResponse(ResourceUtils.textContent(resourceName))
    }
}
