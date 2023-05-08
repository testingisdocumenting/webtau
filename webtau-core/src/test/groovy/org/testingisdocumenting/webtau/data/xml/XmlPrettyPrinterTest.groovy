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

package org.testingisdocumenting.webtau.data.xml

import org.junit.Test
import org.testingisdocumenting.webtau.data.render.PrettyPrintableTestBase

class XmlPrettyPrinterTest extends PrettyPrintableTestBase {
    @Test
    void "single node no text no attributes"() {
        printAndExpect("<div/>", "<div/>")
    }

    @Test
    void "single node with attributes"() {
        printAndExpect("<div    id=\"my-input\"   class =   \"card\"/>", "<div id=\"my-input\" class=\"card\"/>")
    }

    @Test
    void "node with text"() {
        printAndExpect("<div>hello world</div>", "<div>hello world</div>")
    }

    @Test
    void "node with nested node with text"() {
        printAndExpect("<ul><li><a href=\"#ref\">hello world</a></li></ul>", "<ul><li><a href=\"#ref\">hello world</a></li></ul>")
    }

    @Test
    void "node with multiple children"() {
        printAndExpect("<ul class=\"list\"><li>item one</li><li>   item two</li></ul>", "<ul class=\"list\">\n" +
                "  <li>item one</li>\n" +
                "  <li> item two</li>\n" +
                "</ul>")
    }

    @Test
    void "print original when can't parse"() {
        printAndExpect("<body test", "<body test")
    }

    void printAndExpect(String xml, String expected) {
        new XmlPrettyPrinter(xml).prettyPrint(printer)
        expectOutput(expected)
    }
}
