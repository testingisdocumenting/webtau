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

import com.twosigma.webtau.cli.WebTauCliApp
import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.driver.WebDriverCreator
import com.twosigma.webtau.reporter.StepReporter
import com.twosigma.webtau.reporter.StepReporters
import com.twosigma.webtau.reporter.TestStep
import com.twosigma.webtau.runner.standalone.StandaloneTest
import com.twosigma.webtau.runner.standalone.StandaloneTestListener
import com.twosigma.webtau.runner.standalone.StandaloneTestListeners
import com.twosigma.webtau.utils.FileUtils
import com.twosigma.webtau.utils.JsonUtils
import com.twosigma.webtau.utils.ResourceUtils
import com.twosigma.webtau.utils.StringUtils
import org.jsoup.Jsoup
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class WebtauFeaturesTesting implements StepReporter, StandaloneTestListener {
    private static final int testServerPort = 8180
    private static WebTauFeaturesTestServer testServer

    private List<String> report = new ArrayList<>()
    private String testDescription

    @BeforeClass
    static void init() {
        System.setProperty("url", "http://localhost:" + testServerPort)

        testServer = new WebTauFeaturesTestServer()
        testServer.start(testServerPort)
    }

    @AfterClass
    static void cleanup() {
        WebDriverCreator.closeAll()
        testServer.stop()
    }

    @Test
    void "waitTo"() {
        runCli("scenarios.ui/waitTo.groovy")
    }

    @Test
    void "waitToNot"() {
        runCli("scenarios.ui/waitToNot.groovy")
    }

    @Test
    void "should"() {
        runCli("scenarios.ui/should.groovy")
    }

    @Test
    void "shouldNot"() {
        runCli("scenarios.ui/shouldNot.groovy")
    }

    @Test
    void "regexp"() {
        runCli("scenarios.ui/regexp.groovy")
    }

    @Test
    void "save html snippets"() {
        saveSnippet("finders-and-filters.html", "#menu", "menu")
    }

    @Test
    void "filter by text"() {
        runCli("scenarios.ui/byText.groovy")
    }

    @Test
    void "filter by regexp"() {
        runCli("scenarios.ui/byRegexp.groovy")
    }

    @Test
    void "filter by number"() {
        runCli("scenarios.ui/byNumber.groovy")
    }

    @Test
    void "http get"() {
        runCli("rest/restGet.groovy")
    }

    @Test
    void "extract script for documentation"() {
        def testPath = Paths.get("examples/scenarios.ui/waitTo.groovy")
        def script = FileUtils.fileTextContent(testPath)

        String scope = extractScenarioBody(script)
        Assert.assertEquals("search.open()\n\n" +
                "search.submit(query: \"search this\")\n" +
                "search.numberOfResults.waitTo == 2", scope)
    }

    private static void saveSnippet(String resourceName, String css, String snippetOutName) {
        def html = ResourceUtils.textContent(resourceName)

        def snippetPath = Paths.get("test-artifacts/snippets").resolve(snippetOutName + ".html")
        FileUtils.writeTextContent(snippetPath, Jsoup.parse(html).select(css).toString())
    }

    private static String extractScenarioBody(String script) {
        def scopeStartIdx = script.indexOf("{")
        def scopeEndIdx = script.lastIndexOf("}")

        return StringUtils.stripIndentation(script.substring(scopeStartIdx + 1, scopeEndIdx))
    }

    void runCli(String testFileName) {
        def testPath = Paths.get("examples/" + testFileName)
        def script = FileUtils.fileTextContent(testPath)
        def example = extractScenarioBody(script)

        StepReporters.add(this)
        StandaloneTestListeners.add(this)
        report.clear()

        try {
            def cliApp = new WebTauCliApp("--url=http://localhost:" + testServerPort, testPath.toString())
            cliApp.start(false)
        } finally {
            def testArtifact = [description: testDescription, example: example, report: report]

            saveTestArtifact(testFileName, testArtifact)

            StepReporters.remove(this)
            StandaloneTestListeners.remove(this)
        }
    }

    private static void saveTestArtifact(String testFileName, artifact) {
        def json = JsonUtils.serializePrettyPrint(artifact)
        def expectedPath = Paths.get("test-artifacts").resolve(testFileName + ".result")
        def actualPath = Paths.get("test-artifacts").resolve(testFileName + ".result.actual")

        if (! Files.exists(expectedPath)) {
            FileUtils.writeTextContent(expectedPath, json)

            throw new AssertionError("make sure " + expectedPath + " is correct. and commit it as a baseline. " +
                    "test will not fail next time unless output of the test is changed")
        }

        def expectedReport = JsonUtils.deserialize(FileUtils.fileTextContent(expectedPath)).report


        if (! expectedReport.equals(artifact.report)) {
            ConsoleOutputs.out("reports are different, you can use IDE to compare files: ", Color.PURPLE, actualPath,
                    Color.BLUE, " and ", Color.PURPLE, expectedPath)
            FileUtils.writeTextContent(actualPath, json)
            Assert.assertEquals(expectedReport.join("\n"), artifact.report.join("\n"))
        } else {
            FileUtils.writeTextContent(expectedPath, json)
            Files.deleteIfExists(actualPath)
        }
    }

    @Override
    void onStepStart(TestStep step) {
        def stepAsText = "> " + StringUtils.createIndentation(step.numberOfParents * 2) + step.getInProgressMessage().toString()
        report.add(stepAsText)
    }

    @Override
    void onStepSuccess(TestStep step) {
        def stepAsText = ". " + StringUtils.createIndentation(step.numberOfParents * 2)
        report.add(stepAsText + step.getCompletionMessage().toString())
    }

    @Override
    void onStepFailure(TestStep step) {
        def stepAsText = "X " + StringUtils.createIndentation(step.numberOfParents * 2) + step.getCompletionMessage().toString()
        report.add(stepAsText)
    }

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
        testDescription = test.description
    }

    @Override
    void afterAllTests() {
    }
}
