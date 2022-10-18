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
import org.testingisdocumenting.webtau.browser.page.path.finder.ByCssFinderPage
import org.testingisdocumenting.webtau.reporter.StepReporter
import org.testingisdocumenting.webtau.reporter.StepReporters
import org.testingisdocumenting.webtau.reporter.WebTauStep

class GenericPageElementTest implements StepReporter {
    static PageElementPath elementPath
    static FakeWebDriver driver
    List<String> stepMessages = []

    @BeforeClass
    static void setupDriver() {
        driver = new FakeWebDriver()
        driver.registerFakeElement(".element", new FakeWebElement("div", "abc" , [:]))

        elementPath = new PageElementPath()
        elementPath.addFinder(new ByCssFinderPage(".element"))
    }

    @Before
    void setupReporter() {
        stepMessages.clear()
        StepReporters.add(this)
    }

    @After
    void cleanupReporter() {
        StepReporters.remove(this)
    }

    @Test
    void "should hide sent keys when using no log method"() {
        def pageElement = new GenericPageElement(driver, new FakeAdditionalBrowserInteractions(), elementPath, false)
        pageElement.sendKeysNoLog("secret password")

        stepMessages.should == ["sending keys ***** to by css .element", "sent keys ***** to by css .element"]
    }

    @Test
    void "should show sent keys when using regular method"() {
        def pageElement = new GenericPageElement(driver, new FakeAdditionalBrowserInteractions(), elementPath, false)
        pageElement.sendKeys("secret password")

        stepMessages.should == ["sending keys secret password to by css .element", "sent keys secret password to by css .element"]
    }

    @Test
    void "should hide set value when using no log method"() {
        def pageElement = new GenericPageElement(driver, new FakeAdditionalBrowserInteractions(), elementPath, false)
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
        def pageElement = new GenericPageElement(driver, new FakeAdditionalBrowserInteractions(), elementPath, false)
        pageElement.setValue("another password")

        stepMessages.should == ["setting value another password to by css .element",
                                "clearing by css .element",
                                "cleared by css .element",
                                "sending keys another password to by css .element",
                                "sent keys another password to by css .element",
                                "set value another password to by css .element"]
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
}
