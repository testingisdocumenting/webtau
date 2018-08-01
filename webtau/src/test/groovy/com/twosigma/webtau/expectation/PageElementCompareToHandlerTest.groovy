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

package com.twosigma.webtau.expectation

import com.twosigma.webtau.FakeWebDriver
import com.twosigma.webtau.FakeWebElement
import com.twosigma.webtau.browser.expectation.PageElementCompareToHandler
import com.twosigma.webtau.browser.page.path.ElementPath
import com.twosigma.webtau.browser.page.path.GenericPageElement
import org.junit.Before
import org.junit.Test

class PageElementCompareToHandlerTest {
    FakeWebDriver driver
    def handler = new PageElementCompareToHandler()

    @Before
    void init() {
        driver = new FakeWebDriver()
    }

    @Test
    void "handles page element and any other value"() {
        def pageElement = new GenericPageElement(driver, new ElementPath())

        handler.handleEquality(pageElement, "hello").should == true
        handler.handleEquality(pageElement, 100).should == true

        handler.handleEquality(100, 100).should == false
    }

    @Test
    void "extracts underlying value from element for comparison"() {
        driver.registerFakeElement("fakecss1", FakeWebElement.tagAndText("div", "hello"))
        def pageElement = new GenericPageElement(driver, ElementPath.css("fakecss1"))

        pageElement.should == "hello"
    }
}
