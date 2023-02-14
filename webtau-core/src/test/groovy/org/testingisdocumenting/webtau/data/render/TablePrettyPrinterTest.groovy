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

package org.testingisdocumenting.webtau.data.render

import org.junit.Test
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.data.ValuePath
import org.testingisdocumenting.webtau.data.table.TableData

import static org.testingisdocumenting.webtau.WebTauCore.*

class TablePrettyPrinterTest extends PrettyPrintableTestBase {
    @Test
    void "render empty table"() {
        def emptyTable = table("colA", "colB", "colC",
                               ________________________)

        prettyPrintTable(emptyTable, [], "colA │ colB │ colC\n" +
                "[empty]")
    }

    @Test
    void "render table of simple values"() {
        def simpleTable = table("colA", "colB", "colC",
                                 ________________________,
                                "hello", "world", 10,
                                "another", "world", 200)

        prettyPrintTable(simpleTable, [], 'colA      │ colB    │ colC\n' +
                '"hello"   │ "world" │   10\n' +
                '"another" │ "world" │  200')
    }

    @Test
    void "render table keys"() {
        def simpleTable = table("*colA", "colB", "*colC",
                                ________________________,
                                "hello", "world", 10,
                                "another", "world", 200)

        prettyPrintTable(simpleTable, [], '*colA     │ colB    │ *colC\n' +
                '"hello"   │ "world" │    10\n' +
                '"another" │ "world" │   200')
    }

    @Test
    void "render table of simple decorated values"() {
        def simpleTable = table("colA", "colB", "colC",
                               ________________________,
                               "hello", "world", 10,
                               "another", "world", 200)

        def paths = ["[0].colB", "[1].colC"]
        prettyPrintTable(simpleTable, paths, 'colA      │ colB        │ colC   \n' +
                '"hello"   │ **"world"** │      10\n' +
                '"another" │ "world"     │ **200**')
    }

    @Test
    void "render table with map and list inside"() {
        printer.setRecommendedMaxWidthForSingleLineObjects(5)

        def table = table("colA", "colB",               "colC",
                         ______________________________________,
                         "text",     100, "12",
                         "hello", map("key", "value"), [2, 5],
                       "another", "world", [3, 8])

        prettyPrintTable(table, [], 'colA      │ colB             │ colC\n' +
                '"text"    │              100 │ "12"\n' +
                '"hello"   │ {                │ [   \n' +
                '          │   "key": "value" │   2,\n' +
                '          │ }                │   5 \n' +
                '          │                  │ ]   \n' +
                '          │                  │     \n' +
                '"another" │ "world"          │ [   \n' +
                '          │                  │   3,\n' +
                '          │                  │   8 \n' +
                '          │                  │ ]   ')
    }

    @Test
    void "render table with decorated map and list inside"() {
        printer.setRecommendedMaxWidthForSingleLineObjects(5)

        def table = table("colA", "colB",               "colC",
                          ______________________________________,
                          "text",     100, "12",
                          "hello", map("key", "value"), [2, 5],
                          "another", "world", [3, 8])

        def paths = ["[1].colB.key", "[2].colC", "[2].colC[1]"]

        prettyPrintTable(table, paths, 'colA      │ colB                 │ colC   \n' +
                '"text"    │                  100 │ "12"   \n' +
                '"hello"   │ {                    │ [      \n' +
                '          │   "key": **"value"** │   2,   \n' +
                '          │ }                    │   5    \n' +
                '          │                      │ ]      \n' +
                '          │                      │        \n' +
                '"another" │ "world"              │ **[    \n' +
                '          │                      │   3,   \n' +
                '          │                      │   **8**\n' +
                '          │                      │ ]**    ')
    }

    @Test
    void "render table with decorated map and list inside single line"() {
        def table = table("colA", "colB",               "colC",
                          ______________________________________,
                          "text",     100,               "12",
                          "hello", map("key", "value"), [2, 5],
                          "another", "world",           [3, 8])

        def paths = ["[1].colB.key", "[2].colC", "[2].colC[1]"]

        prettyPrintTable(table, paths, 'colA      │ colB                 │ colC          \n' +
                '"text"    │                  100 │ "12"          \n' +
                '"hello"   │ {"key": **"value"**} │ [2, 5]        \n' +
                '"another" │ "world"              │ **[3, **8**]**')
    }

    private void prettyPrintTable(TableData tableData, List<String> paths, String expected) {
        printer.setPathsDecoration(new PrettyPrinterDecorationToken("**", Color.RED),
                paths.collect { new ValuePath(it) } as Set)

        def tablePrinter = new TablePrettyPrinter(tableData)
        tablePrinter.prettyPrint(printer, new ValuePath(""))

        expectOutput(expected)
    }
}
