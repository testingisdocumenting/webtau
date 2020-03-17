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

package org.testingisdocumenting.webtau.featuretesting

import org.testingisdocumenting.webtau.cfg.GroovyConfigBasedBrowserPageNavigationHandler
import org.testingisdocumenting.webtau.http.testserver.TestServer
import org.testingisdocumenting.webtau.utils.ResourceUtils
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import static org.testingisdocumenting.webtau.featuretesting.FeaturesDocArtifactsExtractor.*

class WebTauBrowserFeaturesTest {
    private static WebTauEndToEndTestRunner testRunner

    static void registerEndPoints(TestServer testServer) {
        testServer.registerGet("/search", htmlResponse('search.html'))
        testServer.registerGet("/forms", htmlResponse('forms.html'))
        testServer.registerGet("/special-forms", htmlResponse('special-forms.html'))
        testServer.registerGet("/calculation", htmlResponse('calculation.html'))
        testServer.registerGet("/finders-and-filters", htmlResponse('finders-and-filters.html'))
        testServer.registerGet("/matchers", htmlResponse('matchers.html'))
        testServer.registerGet("/local-storage", htmlResponse('local-storage.html'))
        testServer.registerGet("/logged-in-user", htmlResponse('logged-in-user.html'))
        testServer.registerGet("/flicking-element", htmlResponse('flicking-element.html'))
        testServer.registerGet("/resource-creation", htmlResponse('resource-creation.html'))
    }

    @BeforeClass
    static void init() {
        testRunner = new WebTauEndToEndTestRunner()

        def testServer = testRunner.testServer
        registerEndPoints(testServer)

        testRunner.startTestServer()
    }

    @AfterClass
    static void cleanup() {
        testRunner.stopTestServer()
    }

    @Before
    void cleanBeforeTest() {
        GroovyConfigBasedBrowserPageNavigationHandler.handler = null
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
        def root = 'finders-filters'

        extractCodeSnippets(
                root, 'examples/scenarios/ui/findersFilters.groovy', [
                'byCss.groovy': 'by css id',
                'byCssFirstMatched.groovy': 'by css first matched',
                'byCssAllMatched.groovy': 'by css all matched',
                'byCssAndFilterByNumber.groovy': 'by css and filter by number',
                'byCssAndFilterByText.groovy': 'by css and filter by text',
                'byCssAndFilterByRegexp.groovy': 'by css and filter by regexp',
                'byCssAndFilterByNumberNestedFind.groovy': 'by css and filter by number and nested css',
        ])

        extractHtmlSnippets(root, 'finders-and-filters.html', [
             'flat-menu.html': '#menu'
        ])
    }

    @Test
    void "navigation"() {
        runCli('navigation.groovy', 'webtau.cfg')
    }

    @Test
    void "navigation extract snippets"() {
        extractCodeSnippets(
                'navigation', 'examples/scenarios/ui/navigation.groovy', [
                'open.groovy': 'open',
                'reopen.groovy': 'reopen',
                'refresh.groovy': 'refresh',
                'restart.groovy': 'restart',
                'saveUrl.groovy': 'save url',
                'loadUrl.groovy': 'load url',
                'waitOnUrl.groovy': 'wait on url',
        ])
    }

    @Test
    void "matchers"() {
        runCli('matchers.groovy', 'webtau.cfg')
    }

    @Test
    void "matchers extract snippets"() {
        def root = 'matchers'

        extractCodeSnippets(
                root, 'examples/scenarios/ui/matchers.groovy', [
                'equalText.groovy': 'equal text',
                'equalTextRegexp.groovy': 'equal text regexp',
                'equalListOfText.groovy': 'equal list of text',
                'equalListOfTextAndRegexp.groovy': 'equal list of text and regexp',
                'containTextInList.groovy': 'contain text in list',
                'equalNumber.groovy': 'equal number',
                'equalListOfNumbers.groovy': 'equal list of numbers',
                'greaterNumber.groovy': 'greater number',
                'greaterEqualNumber.groovy': 'greater equal number',
                'lessEqualListMixOfNumbers.groovy': 'less equal list mix of numbers',
                'enabledDisabled.groovy': 'enable state',
                'visibleHidden.groovy': 'visible state',
        ])

        extractHtmlSnippets(root, 'matchers.html', [
                'numbers.html': '#numbers',
                'texts.html': '#texts',
                'state.html': '#state'])
    }

    @Test
    void "forms"() {
        runCli('forms.groovy', 'webtau.cfg')
    }

    @Test
    void "special forms"() {
        runCli('specialForms.groovy', 'specialForms.cfg')
    }

    @Test
    void "forms extract snippets"() {
        def root = 'forms'

        extractCodeSnippets(
                root, 'examples/scenarios/ui/forms.groovy', [
                'inputDefault.groovy': 'input type default',
                'inputDate.groovy': 'input type date',
                'selectOptions.groovy': 'select options',
                'validation.groovy': 'values validation',
        ])

        extractHtmlSnippets(root, 'forms.html', [
                'form-element.html': '#form'
        ])
    }

    @Test
    void "special forms extract snippets"() {
        def root = 'special-forms'

        extractCodeSnippets(
                root, 'examples/scenarios/ui/specialForms.groovy', [
                'customGetSet.groovy': 'get set custom based on registered handler',
        ])

        extractHtmlSnippets(root, 'special-forms.html', [
                'form-custom-element.html': '#special-form'
        ])
    }

    @Test
    void "local storage"() {
        runCli('localStorage.groovy', 'webtau.cfg')
    }

    @Test
    void "local storage extract snippets"() {
        def root = 'local-storage'

        extractCodeSnippets(
                root, 'examples/scenarios/ui/localStorage.groovy', [
                'localStorageApi.groovy': 'local storage api',
        ])

        extractHtmlSnippets(root, 'local-storage.html', [
                'body-only.html': 'body'])
    }

    @Test
    void "open handlers"() {
        runCli('openHandler.groovy', 'openHandler.cfg')
    }

    @Test
    void "wait sync"() {
        runCli('waitSync.groovy', 'webtau.cfg')
    }

    @Test
    void "wait sync extract snippets"() {
        extractCodeSnippets(
                'wait-sync',
                'examples/scenarios/ui/waitSync.groovy', [
                'waitForAppear.groovy': 'wait for element to appear',
                'waitForMatch.groovy': 'wait for match',
                'waitForEnabled.groovy': 'wait for element to be enabled',
                'waitForEnabledAndVisible.groovy': 'wait for element to be hidden',
        ])
    }

    @Test
    void "flicking element"() {
        runCli('flickingElement.groovy', 'webtau.cfg')
    }

    @Test
    void "doc capture"() {
        runCli('docCapture.groovy', 'docCapture.cfg')
    }

    @Test
    void "doc capture extract snippets"() {
        extractCodeSnippets(
                'doc-capture',
                'examples/scenarios/ui/docCapture.groovy', [
                'captureBadges.groovy': 'search and capture with badges',
                'captureHighlightCover.groovy': 'capture with highlight and cover',
                'captureArrow.groovy': 'capture with arrow',
        ])
    }

    private static void runCli(String uiTestName, String configFileName) {
        testRunner.runCli("scenarios/ui/$uiTestName",
                "scenarios/ui/$configFileName", "--url=${testRunner.testServer.uri}")
    }

    private static TestServerHtmlResponse htmlResponse(String resourceName) {
        new TestServerHtmlResponse(ResourceUtils.textContent(resourceName))
    }
}
