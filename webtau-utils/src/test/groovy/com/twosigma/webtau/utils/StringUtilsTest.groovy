/*
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

package com.twosigma.webtau.utils

import org.junit.Assert
import org.junit.Test

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
        Assert.assertEquals("int a = 2;\nint b = 3;", stripped)
    }

    @Test
    void "extracts inside curly braces"() {
        def code = "{\n    statement1;\n    statement2}"
        def stripped = StringUtils.extractInsideCurlyBraces(code)
        Assert.assertEquals("\n    statement1;\n    statement2", stripped)

        Assert.assertEquals("", StringUtils.extractInsideCurlyBraces(""))
    }

    @Test
    void "removes content inside brackets and brackets"() {
        Assert.assertEquals("hello ",
                StringUtils.removeContentInsideBracketsInclusive("hello <world>"))
    }

    @Test
    void "concat prefix and multiline text preserving prefix size indentation"() {
        def concatenated = StringUtils.concatWithIndentation("a prefix:", "line1 line1\nline2\nline #3")

        Assert.assertEquals("a prefix:line1 line1\n" +
                "         line2\n" +
                "         line #3", concatenated)
    }

    @Test
    void "check if text is a number"() {
        assert StringUtils.isNumeric("123")
        assert StringUtils.isNumeric("123.0")
        assert StringUtils.isNumeric(".0")
        assert StringUtils.isNumeric("123,000")

        assert ! StringUtils.isNumeric("")
        assert ! StringUtils.isNumeric("d.0")
        assert ! StringUtils.isNumeric("123 L")
    }

    @Test
    void "converts text to a number"() {
        def number = StringUtils.convertToNumber("123,000")
        assert number == 123000
    }
}
