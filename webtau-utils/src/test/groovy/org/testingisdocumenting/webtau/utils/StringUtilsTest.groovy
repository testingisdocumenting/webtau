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
    void "split lines preserve separator"() {
        assert StringUtils.splitLinesPreserveNewLineSeparator("").toList() == [""]
        assert StringUtils.splitLinesPreserveNewLineSeparator("hello").toList() == ["hello"]

        assert StringUtils.splitLinesPreserveNewLineSeparator("hello\nworld\n\nof lines\n")
                .toList() == ["hello", "\n", "world", "\n", "\n", "of lines", "\n"]
    }

    @Test
    void "has new line separator"() {
        assert !StringUtils.hasNewLineSeparator("hello")
        assert StringUtils.hasNewLineSeparator("hello\n")
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
    void "first n lines"() {
        assert StringUtils.firstNLines("", 3) == ""
        assert StringUtils.firstNLines("one", 0) == ""
        assert StringUtils.firstNLines("one", 1) == "one"
        assert StringUtils.firstNLines("one", 3) == "one"
        assert StringUtils.firstNLines("one\ntwo", 3) == "one\ntwo"
        assert StringUtils.firstNLines("one\ntwo\nthree", 3) == "one\ntwo\nthree"
        assert StringUtils.firstNLines("one\ntwo\nthree\nfour", 3) == "one\ntwo\nthree"
    }

    @Test
    void "number of lines"() {
        assert StringUtils.numberOfLines("") == 1
        assert StringUtils.numberOfLines("hello") == 1
        assert StringUtils.numberOfLines("hello\n") == 2
        assert StringUtils.numberOfLines("hello\nworld") == 2
        assert StringUtils.numberOfLines("hello\nworld\n") == 3
        assert StringUtils.numberOfLines("hello\nworld\n\n") == 4
    }

    @Test
    void "number of empty lines at start"() {
        assert StringUtils.numberOfEmptyLinesAtStart("") == 0
        assert StringUtils.numberOfEmptyLinesAtStart("\n") == 1
        assert StringUtils.numberOfEmptyLinesAtStart("\n\n") == 2
        assert StringUtils.numberOfEmptyLinesAtStart("\n\r\n") == 2
        assert StringUtils.numberOfEmptyLinesAtStart("\n \n") == 1
        assert StringUtils.numberOfEmptyLinesAtStart("\nhello\n") == 1
        assert StringUtils.numberOfEmptyLinesAtStart("\n\nhello\n") == 2
    }

    @Test
    void "number of empty lines at end"() {
        assert StringUtils.numberOfEmptyLinesAtEnd("") == 0
        assert StringUtils.numberOfEmptyLinesAtEnd("\n") == 1
        assert StringUtils.numberOfEmptyLinesAtEnd("\n\n") == 2
        assert StringUtils.numberOfEmptyLinesAtEnd("\n\r\n") == 2
        assert StringUtils.numberOfEmptyLinesAtEnd("\n \n") == 1
        assert StringUtils.numberOfEmptyLinesAtEnd("\nhello\n") == 1
        assert StringUtils.numberOfEmptyLinesAtEnd("\nhello\n\n") == 2
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
        assert StringUtils.convertToNumber("123,100") == 123100

        def formatter = NumberFormat.getNumberInstance()
        assert StringUtils.convertToNumber(formatter, "123,000") == 123000
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

    @Test
    void "chat sequence to string or null"() {
        assert StringUtils.toStringOrNull(null) == null
        assert StringUtils.toStringOrNull("hello") == "hello"
    }
}
