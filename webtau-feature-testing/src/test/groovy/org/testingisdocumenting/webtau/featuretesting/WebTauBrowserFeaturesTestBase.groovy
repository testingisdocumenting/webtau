/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.featuretesting

import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.webtau.cfg.GroovyConfigBasedBrowserPageNavigationHandler
import org.testingisdocumenting.webtau.browser.driver.WebDriverCreator
import org.testingisdocumenting.webtau.documentation.DocumentationArtifacts
import org.testingisdocumenting.webtau.http.testserver.FixedResponsesHandler

import static org.testingisdocumenting.webtau.featuretesting.FeaturesDocArtifactsExtractor.extractCodeSnippets
import static org.testingisdocumenting.webtau.featuretesting.FeaturesDocArtifactsExtractor.extractHtmlSnippets

class WebTauBrowserFeaturesTestBase {
    protected static WebTauEndToEndTestRunner testRunner
    protected String browser

    WebTauBrowserFeaturesTestBase() {
        this.browser = "chrome"
    }

    @BeforeClass
    static void init() {
        WebDriverCreator.quitAll()
        DocumentationArtifacts.clearRegisteredNames()

        FixedResponsesHandler handler = new FixedResponsesHandler()
        testRunner = new WebTauEndToEndTestRunner(handler)

        WebTauBrowserFeaturesTestData.registerEndPoints(handler)

        testRunner.startTestServer()
    }

    @AfterClass
    static void cleanup() {
        testRunner.stopTestServer()
    }

    @Before
    void cleanBeforeTest() {
        GroovyConfigBasedBrowserPageNavigationHandler.handler = null
        testRunner.setClassifier(browser)
    }

    @Test
    void "open browser and assert"() {
        runCli("basic.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "open browser and assert no scenario"() {
        runCli("basicNoScenario.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "open browser using browser url"() {
        runCliWithBrowserUrlOverride("basic.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "lazy declaration"() {
        runCli("basicDeclareFirst.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "title and url"() {
        runCli("titleAndUrl.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "basic element actions"() {
        runCli("basicElementActions.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "drag and drop"() {
        runCli("dragAndDrop.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "element actions snippets"() {
        def root = "element-actions"

        extractCodeSnippets(
                root, "examples/scenarios/ui/basicElementActions.groovy", [
                "click.groovy": "click",
                "shiftClick.groovy": "shift click",
                "altClick.groovy": "alt click",
                "controlClick.groovy": "control click",
                "commandClick.groovy": "command click",
                "controlOrCommandClick.groovy": "control or command click",
                "hover.groovy": "hover",
                "sendKeys.groovy": "send keys",
                "sendKeysNoLog.groovy": "send keys no log",
                "clear.groovy": "clear",
                "setValue.groovy": "setValue",
                "setValueNoLog.groovy": "setValue noLog",
                "rightClick.groovy": "right click",
                "doubleClick.groovy": "double click",
        ])
    }

    @Test
    void "finders and filters"() {
        runCli("findersFilters.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "finders and filters extract snippets"() {
        def root = "finders-filters"

        extractCodeSnippets(
                root, "examples/scenarios/ui/findersFilters.groovy", [
                "byCss.groovy": "by css id",
                "byCssFirstMatched.groovy": "by css first matched",
                "byCssAllMatched.groovy": "by css all matched",
                "byCssAndFilterByNumber.groovy": "by css and filter by number",
                "byCssAndFilterByText.groovy": "by css and filter by text",
                "byCssAndFilterByRegexp.groovy": "by css and filter by regexp",
                "byCssAndFilterByNumberNestedFind.groovy": "by css and filter by number and nested css",
        ])

        extractHtmlSnippets(root, "finders-and-filters.html", [
                "welcome.html": "#welcome-wrapper",
                "flat-menu.html": "#menu"
        ])
    }

    @Test
    void "navigation"() {
        runCli("navigation.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "navigation extract snippets"() {
        extractCodeSnippets(
                "navigation", "examples/scenarios/ui/navigation.groovy", [
                "open.groovy": "open",
                "reopen.groovy": "reopen",
                "refresh.groovy": "refresh",
                "restart.groovy": "restart",
                "saveUrl.groovy": "save url",
                "loadUrl.groovy": "load url",
                "waitOnUrl.groovy": "wait on url",
                "backForward.groovy": "back and forward",
        ])
    }

    @Test
    void "matchers"() {
        runCli("matchers.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "matchers extract snippets"() {
        def root = "matchers"

        extractCodeSnippets(
                root, "examples/scenarios/ui/matchers.groovy", [
                "equalText.groovy": "equal text",
                "equalTextRegexp.groovy": "equal text regexp",
                "equalListOfText.groovy": "equal list of text",
                "equalListOfTextAndRegexp.groovy": "equal list of text and regexp",
                "containTextInList.groovy": "contain full text in list",
                "containTextInFirstElement.groovy": "contain text in first matching element",
                "equalNumber.groovy": "equal number",
                "equalListOfNumbers.groovy": "equal list of numbers",
                "greaterNumber.groovy": "greater number",
                "greaterEqualNumber.groovy": "greater equal number",
                "lessEqualListMixOfNumbers.groovy": "less equal list mix of numbers",
                "enabledDisabled.groovy": "enable state",
                "visibleHidden.groovy": "visible state",
        ])

        extractHtmlSnippets(root, "matchers.html", [
                "numbers.html": "#numbers",
                "texts.html": "#texts",
                "state.html": "#state"])
    }

    @Test
    void "forms"() {
        runCli("forms.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "special forms"() {
        runCli("specialForms.groovy", "specialForms.cfg.groovy")
    }

    @Test
    void "forms extract snippets"() {
        def root = "forms"

        extractCodeSnippets(
                root, "examples/scenarios/ui/forms.groovy", [
                "inputDefault.groovy": "input type default",
                "inputDate.groovy": "input type date",
                "checkBox.groovy": "select checkbox",
                "radioButton.groovy": "select radio button",
                "selectOptions.groovy": "select options",
                "validation.groovy": "values validation",
        ])

        extractHtmlSnippets(root, "forms.html", [
                "form-element.html": "#form"
        ])
    }

    @Test
    void "special forms extract snippets"() {
        def root = "special-forms"

        extractCodeSnippets(
                root, "examples/scenarios/ui/specialForms.groovy", [
                "customGetSet.groovy": "get set custom based on registered handler",
        ])

        extractHtmlSnippets(root, "special-forms.html", [
                "form-custom-element.html": "#special-form"
        ])
    }

    @Test
    void "local storage"() {
        runCli("localStorage.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "local storage extract snippets"() {
        def root = "local-storage"

        extractCodeSnippets(
                root, "examples/scenarios/ui/localStorage.groovy", [
                "localStorageApi.groovy": "local storage api",
        ])

        extractHtmlSnippets(root, "local-storage.html", [
                "body-only.html": "body"])
    }

    @Test
    void "cookies"() {
        runCli("cookies.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "cookies extract snippets"() {
        def root = "cookies"

        extractCodeSnippets(
                root, "examples/scenarios/ui/cookies.groovy", [
                "addCookies.groovy": "add cookies",
                "getCookies.groovy": "get all cookies",
                "deleteCookie.groovy": "delete named cookie",
                "deleteAllCookies.groovy": "delete all cookies",
        ])
    }

    @Test
    void "open handlers"() {
        runCli("openHandler.groovy", "openHandler.cfg.groovy")
    }

    @Test
    void "wait sync"() {
        runCli("waitSync.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "wait sync extract snippets"() {
        extractCodeSnippets(
                "wait-sync",
                "examples/scenarios/ui/waitSync.groovy", [
                "waitForAppear.groovy": "wait for element to appear",
                "waitForMatch.groovy": "wait for match",
                "waitForEnabled.groovy": "wait for element to be enabled",
                "waitForEnabledAndVisible.groovy": "wait for element to be hidden",
        ])
    }

    @Test
    void "flicking element"() {
        runCli("flickingElement.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "doc capture"() {
        runCli("docCapture.groovy", "docCapture.cfg.groovy")
    }

    @Test
    void "doc capture extract snippets"() {
        extractCodeSnippets(
                "doc-capture",
                "examples/scenarios/ui/docCapture.groovy", [
                "captureBadges.groovy": "search and capture with badges",
                "captureBadgesDefault.groovy": "search and capture with badges shortcut",
                "captureSpecificElement.groovy": "search and capture results area",
                "captureBadgesPlacement.groovy": "search and capture with badges placed in non center position",
                "captureRectangles.groovy": "capture with rectangles",
                "captureArrow.groovy": "capture with arrow",
        ])
    }

    @Test
    void "scrolling"() {
        runCli("scrolls.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "scrolling no element"() {
        runCli("scrollsNoElement.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "personas searching"() {
        runCli("searchWithPersonas.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "ag grid multi select"() {
        runCli("agGridMultiSelect.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "screenshot of currently failed page"() {
        runCli("failedAssertion.groovy", "webtau.cfg.groovy")
    }

    private void runCli(String uiTestName, String configFileName) {
        runCliWithArgs(uiTestName, configFileName, "--url=${testRunner.testServer.uri}",
                "--browserId=" + browser)
    }

    private void runCliWithBrowserUrlOverride(String uiTestName, String configFileName) {
        runCliWithArgs(uiTestName, configFileName, "--url=http://localhost:-1",
                "--browserUrl=${testRunner.testServer.uri}", "--browserId=" + browser)
    }

    private static void runCliWithArgs(String uiTestName, String configFileName, String... args) {
        testRunner.runCli("scenarios/ui/$uiTestName",
                "scenarios/ui/$configFileName", args)
    }
}
