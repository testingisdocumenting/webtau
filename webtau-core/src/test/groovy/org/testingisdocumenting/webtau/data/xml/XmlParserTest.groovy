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

import static org.testingisdocumenting.webtau.WebTauCore.*

class XmlParserTest {
    @Test
    void "single node"() {
        def xml = XmlParser.parse("<div/>")
        actual(xml).should(equal([tagName: "div", attributes: [], children: [], text: ""]))
    }

    @Test
    void "single node with text"() {
        def xml = XmlParser.parse("<div>hello\n" +
                "  </div>")
        trace("xml", properties(xml))

        actual(xml).should(equal([
                tagName: "div", attributes: [], children: [
                [tagName: "", text: "hello ", attributes: [], children: []]],
                text   : ""]))
    }

    @Test
    void "nested nodes and text"() {
        def xml = XmlParser.parse("<div>hello\n" +
                " <a>link text</a> world </div>")

        actual(xml).should(equal([
                tagName   : "div",
                attributes: [],
                children  : [
                        [attributes: [], children: [], tagName: "", text: "hello "],
                        [
                                tagName   : "a",
                                children  : [[attributes: [], children: [], tagName: "", text: "link text"]],
                                attributes: [],
                                text      : ""
                        ],
                        [attributes: [], children: [], tagName: "", text: " world "]
                ],
                text      : ""
        ]))
    }
}
