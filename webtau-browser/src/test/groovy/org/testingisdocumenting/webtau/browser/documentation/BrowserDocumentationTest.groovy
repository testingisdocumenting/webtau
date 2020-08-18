/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.documentation

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.webtau.browser.BrowserConfig

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.webtau.browser.Browser.browser
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg

class BrowserDocumentationTest {
    @BeforeClass
    static void init() {
        BrowserConfig.setHeadless(true)
        cfg.getDocArtifactsPathConfigValue().set("test", "doc-artifacts")
    }

    @AfterClass
    static void cleanUp() {
        cfg.getDocArtifactsPathConfigValue().reset()
    }

    @Test
    void "should make sure artifact name is unique"() {
        def message = browser.$("#message")

        browser.open("file://" + Paths.get("src/test/resources/test.html").toAbsolutePath())
        message.should == "hello"

        def artifactName = "message-with-badge"

        browser.doc.withAnnotations(browser.doc.badge(message))
                .capture(artifactName)

        code {
            browser.doc.withAnnotations(browser.doc.badge(message))
                    .capture(artifactName)
        } should throwException("doc artifact name <${artifactName}> was already used")
    }
}
