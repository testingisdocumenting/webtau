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

import org.testingisdocumenting.webtau.browser.page.PageElementValue
import org.testingisdocumenting.webtau.browser.page.GenericPageElement
import org.junit.Test
import org.testingisdocumenting.webtau.browser.page.path.PageElementPath

class PagePageElementValueCompareToHandlerTest {
    def pageElement = new GenericPageElement(null, null, PageElementPath.css("#id"))

    @Test
    void "automatically converts text to a number if the expected value is a number"() {
        def elementValue = new PageElementValue(pageElement, "element", { -> "100.6543"})
        elementValue.should == 100.6543
    }
}
