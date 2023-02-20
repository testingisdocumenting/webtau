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

class MapPrettyPrintableTest extends PrettyPrintableTestBase {
    @Test
    void "empty map"() {
        def prettyPrintable = new MapPrettyPrintable([:])
        prettyPrintable.prettyPrint(printer)

        expectOutput("{}")
    }

    @Test
    void "map of simple values single line"() {
        def prettyPrintable = new MapPrettyPrintable([key1: "value1", key2: 20])
        prettyPrintable.prettyPrint(printer)

        expectOutput('{"key1": "value1", "key2": 20}')
    }

    @Test
    void "map with multiline string"() {
        def prettyPrintable = new MapPrettyPrintable([key1: "line1\nline two", price: 100, key2: "three\nmore lines\nwith some words"])
        prettyPrintable.prettyPrint(printer)

        expectOutput('{\n' +
                '  "key1": ________\n' +
                '          line1\n' +
                '          line two\n' +
                '          ________,\n' +
                '  "price": 100,\n' +
                '  "key2": _______________\n' +
                '          three\n' +
                '          more lines\n' +
                '          with some words\n' +
                '          _______________\n' +
                '}')
    }

    @Test
    void "map of nested maps with with decorations single line"() {
        printer.setPathsDecoration(new PrettyPrinterDecorationToken("*", Color.RED),
                [new ValuePath("key2.key21")])

        def prettyPrintable = new MapPrettyPrintable([key1: "value1", key2: [key21: "hello", key22: 22]])
        prettyPrintable.prettyPrint(printer)

        expectOutput('{"key1": "value1", "key2": {"key21": *"hello"*, "key22": 22}}')
    }

    @Test
    void "map of nested maps with with decorations multi line"() {
        printer.setRecommendedMaxWidthForSingleLineObjects(10)
        printer.setPathsDecoration(new PrettyPrinterDecorationToken("*", Color.RED),
                [new ValuePath("key2.key21")])

        def prettyPrintable = new MapPrettyPrintable([key1: "value1", key2: [key21: "hello", key22: 22]])
        prettyPrintable.prettyPrint(printer)

        expectOutput("{\n" +
                "  \"key1\": \"value1\",\n" +
                "  \"key2\": {\n" +
                "    \"key21\": *\"hello\"*,\n" +
                "    \"key22\": 22\n" +
                "  }\n" +
                "}")
    }
}
