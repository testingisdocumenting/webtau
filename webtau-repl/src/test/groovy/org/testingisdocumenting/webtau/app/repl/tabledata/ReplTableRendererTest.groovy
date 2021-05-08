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

package org.testingisdocumenting.webtau.app.repl.tabledata

import org.junit.Test
import org.testingisdocumenting.webtau.repl.tabledata.ReplTableRenderer

class ReplTableRendererTest {
    @Test
    void "should render table with colors and using commas as delimiter"() {
        def table = ["column A" | "column B"] {
                    ____________________________
                        10      | "hello"
                        20      | "world"
                        30      | null    }

        def rendered = ReplTableRenderer.render(table)

        rendered.should == "\u001B[33mcolumn A\u001B[33m, \u001B[0m\u001B[33mcolumn B\u001B[0m\n" +
                "\u001B[36m      10\u001B[0m\u001B[33m, \u001B[0m\"hello\" \n" +
                "\u001B[36m      20\u001B[0m\u001B[33m, \u001B[0m\"world\" \n" +
                "\u001B[36m      30\u001B[0m\u001B[33m, \u001B[0m\u001B[33m[null]  \u001B[0m\n"
    }
}
