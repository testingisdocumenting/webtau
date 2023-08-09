/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.page

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.webtau.FakeAdditionalBrowserInteractions
import org.testingisdocumenting.webtau.FakeWebDriver
import org.testingisdocumenting.webtau.FakeWebElement
import org.testingisdocumenting.webtau.browser.page.path.PageElementPath
import org.testingisdocumenting.webtau.browser.page.path.finder.ByCssPageElementFinder
import org.testingisdocumenting.webtau.data.render.PrettyPrinter
import org.testingisdocumenting.webtau.reporter.StepReporter
import org.testingisdocumenting.webtau.reporter.StepReporters
import org.testingisdocumenting.webtau.reporter.WebTauStep
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import static org.testingisdocumenting.webtau.Matchers.actual
import static org.testingisdocumenting.webtau.Matchers.equal

class PageElementTest implements StepReporter {
    static PageElementPath elementPath
    static FakeWebDriver driver
    List<String> stepMessages = []

    @BeforeClass
    static void setupDriver() {
        elementPath = new PageElementPath()
        elementPath.addFinder(new ByCssPageElementFinder(".element"))
    }

    @Before
    void setupReporter() {
        driver = new FakeWebDriver()
        driver.registerFakeElement(".element", new FakeWebElement("div", "abc" , [:]))

        stepMessages.clear()
        StepReporters.add(this)
    }

    @After
    void cleanupReporter() {
        StepReporters.remove(this)
    }

    @Test
    void "should hide sent keys when using no log method"() {
        def pageElement = new PageElement(driver, new FakeAdditionalBrowserInteractions(), elementPath, false)
        pageElement.sendKeysNoLog("secret password")

        stepMessages.should == ["sending keys ***** to by css .element", "sent keys ***** to by css .element"]
    }

    @Test
    void "should show sent keys when using regular method"() {
        def pageElement = new PageElement(driver, new FakeAdditionalBrowserInteractions(), elementPath, false)
        pageElement.sendKeys("secret password")

        stepMessages.should == ["sending keys secret password to by css .element", "sent keys secret password to by css .element"]
    }

    @Test
    void "should hide set value when using no log method"() {
        def pageElement = new PageElement(driver, new FakeAdditionalBrowserInteractions(), elementPath, false)
        pageElement.setValueNoLog("another password")

        stepMessages.should == ["setting value ***** to by css .element",
                                "clearing by css .element",
                                "cleared by css .element",
                                "sending keys ***** to by css .element",
                                "sent keys ***** to by css .element",
                                "set value ***** to by css .element"]
    }

    @Test
    void "should show set value when using regular method"() {
        def pageElement = new PageElement(driver, new FakeAdditionalBrowserInteractions(), elementPath, false)
        pageElement.setValue("another password")

        stepMessages.should == ["setting value another password to by css .element",
                                "clearing by css .element",
                                "cleared by css .element",
                                "sending keys another password to by css .element",
                                "sent keys another password to by css .element",
                                "set value another password to by css .element"]
    }

    @Test
    void "pretty print html content of single element"() {
        def el1 = FakeWebElement.tagTextValueAndOuterHtml("div", "abc", "", "<div class=\"button\"></div>")
        expectPrettyPrinted(Arrays.asList(el1), 'found single element using by css my-divs\n' +
                '  innerText: abc\n' +
                '  <div class="button"/>')
    }

    @Test
    void "pretty print html content of multiple elements"() {
        def el1 = FakeWebElement.tagTextValueAndOuterHtml("div", "abc", "", "<div class=\"button\"></div>")
        def el2 = FakeWebElement.tagTextValueAndOuterHtml("div", "xyz", "", "<div class=\"attention\"></div>")
        expectPrettyPrinted(Arrays.asList(el1, el2), 'found 2 elements using by css my-divs\n' +
                'element #1\n' +
                '  innerText: abc\n' +
                '  <div class="button"/>\n' +
                '\n' +
                'element #2\n' +
                '  innerText: xyz\n' +
                '  <div class="attention"/>')
    }

    @Test
    void "pretty print html content of more elements than fit"() {
        def el1 = FakeWebElement.tagTextValueAndOuterHtml("div", "abc", "", "<div class=\"button\"></div>")
        def el2 = FakeWebElement.tagTextValueAndOuterHtml("div", "xyz", "", "<div class=\"attention\"></div>")
        def el3 = FakeWebElement.tagTextValueAndOuterHtml("p", "123", "", "<p class=\"hello\"></p>")
        expectPrettyPrinted(Arrays.asList(el1, el2, el3), 'found 3 elements using by css my-divs\n' +
                'element #1\n' +
                '  innerText: abc\n' +
                '  <div class="button"/>\n' +
                '\n' +
                'element #2\n' +
                '  innerText: xyz\n' +
                '  <div class="attention"/>\n' +
                '\n' +
                '...1 more', 2)
    }

    @Test
    void "pretty print incomplete html"() {
        def el1 = FakeWebElement.tagTextValueAndOuterHtml("input", "abc", "", "<input class=\"button\" type=\"ty1\">")
        expectPrettyPrinted(Arrays.asList(el1), 'found single element using by css my-divs\n' +
                '  innerText: abc\n' +
                '  value: <empty>\n' +
                '  <input class="button" type="ty1"/>')
    }

    @Override
    void onStepStart(WebTauStep step) {
        stepMessages.add(step.inProgressMessage.toString())
    }

    @Override
    void onStepSuccess(WebTauStep step) {
        stepMessages.add(step.completionMessage.toString())
    }

    @Override
    void onStepFailure(WebTauStep step) {

    }

    private static void expectPrettyPrinted(List<FakeWebElement> elements, String expectedOutput, int maxElementsToPrint = 5) {
        driver.registerFakeElements("my-divs", elements)

        def elementPath = new PageElementPath()
        elementPath.addFinder(new ByCssPageElementFinder("my-divs"))

        def pageElement = new PageElement(driver, new FakeAdditionalBrowserInteractions(), elementPath, false)

        def console = new TestConsoleOutput()
        def printer = new PrettyPrinter(0)

        new PageElementPrettyPrinter(pageElement, maxElementsToPrint).prettyPrint(printer)
        printer.renderToConsole(console)

        println console.colorOutput
        TestConsoleOutput.withConsoleReporters {
            actual(console.noColorOutput, "prettyPrinted").should(equal(expectedOutput))
        }
    }
}