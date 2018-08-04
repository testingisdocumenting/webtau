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

    private static void runCli(String restTestName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/ui/$restTestName",
                "scenarios/ui/$configFileName", additionalArgs)
    }

    private static TestServerHtmlResponse htmlResponse(String resourceName) {
        new TestServerHtmlResponse(ResourceUtils.textContent(resourceName))
    }
}
