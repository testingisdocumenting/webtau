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
}
