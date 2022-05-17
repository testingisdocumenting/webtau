/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.utils

import org.junit.Test

import java.text.NumberFormat

class StringUtilsTest {
    @Test
    void "should calculate max length on a line in multiline text"() {
        def maxLineLength = StringUtils.maxLineLength("""line #1
line #_2
line #_3\r""")

        assert maxLineLength == 8
    }

    @Test
    void "strip common indentation"() {
        def code = "    int a = 2;\n    int b = 3;"
        def stripped = StringUtils.stripIndentation(code)
        assert stripped == "int a = 2;\nint b = 3;"
    }

    @Test
    void "extracts inside curly braces"() {
        def code = "{\n    statement1;\n    statement2}"
        def stripped = StringUtils.extractInsideCurlyBraces(code)
        assert stripped == "\n    statement1;\n    statement2"

        assert StringUtils.extractInsideCurlyBraces("") == ""
    }

    @Test
    void "removes content inside brackets and brackets"() {
        assert StringUtils.removeContentInsideBracketsInclusive("hello <world>") == "hello "
    }

    @Test
    void "concat prefix and multiline text preserving prefix size indentation"() {
        def concatenated = StringUtils.concatWithIndentation("a prefix:", "line1 line1\nline2\nline #3")

        assert concatenated == "a prefix:line1 line1\n" +
                "         line2\n" +
                "         line #3"
    }

    @Test
    void "check if text is a number"() {
        def formatter = NumberFormat.getNumberInstance()
        assert StringUtils.isNumeric(formatter, "123")
        assert StringUtils.isNumeric(formatter, "123.0")
        assert StringUtils.isNumeric(formatter, ".0")
        assert StringUtils.isNumeric(formatter, "123,000")

        assert !StringUtils.isNumeric(formatter, "")
        assert !StringUtils.isNumeric(formatter, "d.0")
        assert !StringUtils.isNumeric(formatter, "123 L")
    }

    @Test
    void "converts text to a number"() {
        def formatter = NumberFormat.getNumberInstance()
        def number = StringUtils.convertToNumber(formatter, "123,000")
        assert number == 123000
    }

    @Test
    void "should start with"() {
        assert StringUtils.ensureStartsWith("/foo", "/") == "/foo"
        assert StringUtils.ensureStartsWith("foo", "/") == "/foo"
    }

    @Test
    void "strip trailing"() {
        assert StringUtils.stripTrailing("foo/", '/' as char) == "foo"
        assert StringUtils.stripTrailing("foo", '/' as char) == "foo"
    }
}
