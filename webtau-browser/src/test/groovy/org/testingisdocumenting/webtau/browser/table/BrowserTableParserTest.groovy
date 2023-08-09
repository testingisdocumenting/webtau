/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.table

import org.junit.Test
import org.testingisdocumenting.webtau.utils.ResourceUtils

import static org.testingisdocumenting.webtau.WebTauCore.*

class BrowserTableParserTest {
    @Test
    void "standard semantic table"() {
        def html = ResourceUtils.textContent("table/standard.html")
        def table = BrowserTableParser.parse(html)

        table.should == ["column A" | "column B"] {
                        ___________________________
                               "A1" |       "B1"
                               "A2" |       "B2" }
    }

    @Test
    void "with ignored col spans"() {
        def html = ResourceUtils.textContent("table/tax.html")
        def table = BrowserTableParser.parse(html)

        table.columnNames.should == [
                "At least",
                "But less than",
                "Single or Married filing separately",
                "Married filing jointly*",
                "Head of a household" ]

        table.row(0).values.should == ['$0', '$13', '$0', '$0', '$0']
        table.row(3).values.should == ['50', '100', '3', '3', '3']
    }
}
