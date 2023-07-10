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

import org.testingisdocumenting.webtau.FakeWebDriver
import org.testingisdocumenting.webtau.browser.page.PageElement
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.browser.page.path.PageElementPath

class PageElementFamilyCompareToHandlerTest {
    FakeWebDriver driver
    def handler = new PageElementFamilyCompareToHandler()

    @Before
    void init() {
        driver = new FakeWebDriver()
    }

    @Test
    void "handles page element and any other value"() {
        def pageElement = new PageElement(driver, null, new PageElementPath(), false)

        handler.handleEquality(pageElement, "hello").should == true
        handler.handleEquality(pageElement, 100).should == true

        handler.handleEquality(100, 100).should == false
    }
}
