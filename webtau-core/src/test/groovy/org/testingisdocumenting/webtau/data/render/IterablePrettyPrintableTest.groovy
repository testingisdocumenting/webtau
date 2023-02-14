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

class IterablePrettyPrintableTest extends PrettyPrintableTestBase {
    @Test
    void "empty list"() {
        def prettyPrintable = new IterablePrettyPrintable([])
        prettyPrintable.prettyPrint(printer)

        expectOutput("[]")
    }

    @Test
    void "non-empty list single line"() {
        def prettyPrintable = new IterablePrettyPrintable([1, 2, "hello", "world"])
        prettyPrintable.prettyPrint(printer)

        expectOutput('[1, 2, "hello", "world"]')
    }

    @Test
    void "non-empty list multi line"() {
        printer.setRecommendedMaxWidthForSingleLineObjects(10)

        def prettyPrintable = new IterablePrettyPrintable([1, 2, "hello", "world"])
        prettyPrintable.prettyPrint(printer)

        expectOutput('[\n' +
                '  1,\n' +
                '  2,\n' +
                '  "hello",\n' +
                '  "world"\n' +
                ']')
    }

    @Test
    void "decorated list single line"() {
        printer.setPathsDecoration(new PrettyPrinterDecorationToken("*", Color.RED),
                [new ValuePath("[0]"), new ValuePath("[2]")])

        def prettyPrintable = new IterablePrettyPrintable([1, 2, "hello", "world"])
        prettyPrintable.prettyPrint(printer)

        expectOutput('[*1*, 2, *"hello"*, "world"]')
    }

    @Test
    void "list of lists single line"() {
        def prettyPrintable = new IterablePrettyPrintable([[1, 2], [3, 4], ["hello", "world"]])
        prettyPrintable.prettyPrint(printer)

        expectOutput('[[1, 2], [3, 4], ["hello", "world"]]')
    }

    @Test
    void "list of lists multi line"() {
        printer.setRecommendedMaxWidthForSingleLineObjects(10)

        def prettyPrintable = new IterablePrettyPrintable([[1, 2], [3, 4], ["hello", "world"]])
        prettyPrintable.prettyPrint(printer)

        expectOutput('[\n' +
                '  [1, 2],\n' +
                '  [3, 4],\n' +
                '  [\n' +
                '    "hello",\n' +
                '    "world"\n' +
                '  ]\n' +
                ']')
    }

    @Test
    void "list of single line maps with decorations"() {
        printer.setPathsDecoration(new PrettyPrinterDecorationToken("*", Color.RED),
                [new ValuePath("[0].k2"), new ValuePath("[1].k3")])
        def prettyPrintable = new IterablePrettyPrintable([[k1: "v1", k2: "v2"], [k3: "v3"]])
        prettyPrintable.prettyPrint(printer)

        expectOutput('[{"k1": "v1", "k2": *"v2"*}, {"k3": *"v3"*}]')
    }

    @Test
    void "multiline list of single line maps with decorations"() {
        printer.setRecommendedMaxWidthForSingleLineObjects(25)

        printer.setPathsDecoration(new PrettyPrinterDecorationToken("*", Color.RED),
                [new ValuePath("[0].k2"), new ValuePath("[1].k3")])
        def prettyPrintable = new IterablePrettyPrintable([[k1: "v1", k2: "v2"], [k3: "v3"]])
        prettyPrintable.prettyPrint(printer)

        expectOutput('[\n' +
                '  {"k1": "v1", "k2": *"v2"*},\n' +
                '  {"k3": *"v3"*}\n' +
                ']')
    }
}
