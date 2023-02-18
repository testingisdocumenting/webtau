/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.expectation

import org.testingisdocumenting.webtau.FakeAdditionalBrowserInteractions
import org.testingisdocumenting.webtau.FakeWebDriver
import org.testingisdocumenting.webtau.FakeWebElement
import org.testingisdocumenting.webtau.browser.page.path.PageElementPath
import org.testingisdocumenting.webtau.browser.page.GenericPageElement
import org.testingisdocumenting.webtau.browser.page.path.finder.ByCssFinderPage
import org.junit.Before
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runAndValidateOutput
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class PageElementContainHandlerTest {
    FakeWebDriver driver
    def handler = new PageElementCompareToHandler()

    @Before
    void init() {
        driver = new FakeWebDriver()
    }

    @Test
    void "handles page element and any other value"() {
        def pageElement = new GenericPageElement(driver, new FakeAdditionalBrowserInteractions(), new PageElementPath(), false)

        handler.handleEquality(pageElement, "hello").should == true
        handler.handleEquality(pageElement, 100).should == true

        handler.handleEquality(100, 100).should == false
    }

    @Test
    void "delegates to contain handler of first element value of page element by default"() {
        def path = new PageElementPath()
        path.addFinder(new ByCssFinderPage(".element"))

        driver.registerFakeElement(".element", new FakeWebElement("div", "abcdefgh" , [:]))
        driver.registerFakeElement(".element", new FakeWebElement("div", "test" , [:]))

        def pageElement = new GenericPageElement(driver,
                new FakeAdditionalBrowserInteractions(), path, false)

        pageElement.should contain("cde")
        pageElement.shouldNot contain("fff")

        runExpectExceptionAndValidateOutput(AssertionError, contain("no match found")) {
            pageElement.should contain("bbb")
        }
    }

    @Test
    void "delegates to contain handler of array of page element values when element marked as all"() {
        def path = new PageElementPath()
        path.addFinder(new ByCssFinderPage(".element"))

        driver.registerFakeElement(".element", new FakeWebElement("div", "abc" , [:]))
        driver.registerFakeElement(".element", new FakeWebElement("div", "test" , [:]))

        def pageElement = new GenericPageElement(driver,
                new FakeAdditionalBrowserInteractions(), path, false).all()

        pageElement.should contain("test")
        pageElement.shouldNot contain("test2")
    }
}
