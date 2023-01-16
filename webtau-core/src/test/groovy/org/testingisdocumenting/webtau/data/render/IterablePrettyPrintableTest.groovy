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

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.data.ValuePath

class IterablePrettyPrintableTest {
    def consoleOutput = new TestConsoleOutput()
    PrettyPrinter printer

    @Before
    void init() {
        consoleOutput.clear()
        printer = new PrettyPrinter(consoleOutput, 0)
    }

    @Test
    void "empty list"() {
        def prettyPrintable = new IterablePrettyPrintable([])
        prettyPrintable.prettyPrint(printer)

        expectOutput("[]")
    }

    @Test
    void "non-empty list"() {
        def prettyPrintable = new IterablePrettyPrintable([1, 2, "hello", "world"])
        prettyPrintable.prettyPrint(printer)

        expectOutput("[\n" +
                "  1,\n" +
                "  2,\n" +
                "  \"hello\",\n" +
                "  \"world\"\n" +
                "]")
    }

    @Test
    void "decorated list"() {
        printer.setPathsDecoration(new PrettyPrinterDecorationToken("*", Color.RED),
                [new ValuePath("[0]"), new ValuePath("[2]")])

        def prettyPrintable = new IterablePrettyPrintable([1, 2, "hello", "world"])
        prettyPrintable.prettyPrint(printer)

        expectOutput("[\n" +
                "  *1*,\n" +
                "  2,\n" +
                "  *\"hello\"*,\n" +
                "  \"world\"\n" +
                "]")
    }

    @Test
    void "list of lists"() {
        def prettyPrintable = new IterablePrettyPrintable([[1, 2], [3, 4], ["hello", "world"]])
        prettyPrintable.prettyPrint(printer)

        expectOutput("[\n" +
                "  [\n" +
                "    1,\n" +
                "    2\n" +
                "  ],\n" +
                "  [\n" +
                "    3,\n" +
                "    4\n" +
                "  ],\n" +
                "  [\n" +
                "    \"hello\",\n" +
                "    \"world\"\n" +
                "  ]\n" +
                "]")
    }

    void expectOutput(String expected) {
        println consoleOutput.colorOutput
        Assert.assertEquals(expected, consoleOutput.noColorOutput)
    }
}
