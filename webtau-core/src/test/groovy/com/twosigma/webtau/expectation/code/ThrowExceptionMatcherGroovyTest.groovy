package com.twosigma.webtau.expectation.code

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import static com.twosigma.webtau.Ddjt.*

class ThrowExceptionMatcherGroovyTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none()

    @Test
    void "should validate exception message"() {
        thrown.expectMessage("\nmismatches:\n" +
                "\n" +
                "expected exception message:   actual: error message <java.lang.String>\n" +
                "                            expected: error message1 <java.lang.String>")

        code {
            throw new RuntimeException("error message")
        } should throwException("error message1")
    }

    @Test
    void "should validate exception message using regexp"() {
        thrown.expectMessage("\nmismatches:\n" +
                "\n" +
                "expected exception message:    actual string: error message\n" +
                "                            expected pattern: error \\d+")

        code {
            throw new RuntimeException("error message")
        } should throwException(~/error \d+/)
    }

    @Test
    void "should validate exception class"() {
        thrown.expectMessage("\nmismatches:\n" +
                "\n" +
                "expected exception class:   actual: class java.lang.IllegalArgumentException <java.lang.Class>\n" +
                "                          expected: class java.lang.UnsupportedOperationException <java.lang.Class>")

        code {
            throw new IllegalArgumentException("error message")
        } should throwException(UnsupportedOperationException)
    }

    @Test
    void "should validate exception class and expected message pattern"() {
        thrown.expectMessage("\nmismatches:\n" +
                "\n" +
                "expected exception message:    actual string: error message\n" +
                "                            expected pattern: error \\d\n" +
                "expected exception class:   actual: class java.lang.IllegalArgumentException <java.lang.Class>\n" +
                "                          expected: class java.lang.UnsupportedOperationException <java.lang.Class>")

        code {
            throw new IllegalArgumentException("error message")
        } should throwException(UnsupportedOperationException, ~/error \d/)
    }

    @Test
    void "should validate exception class and expected message"() {
        thrown.expectMessage("\nmismatches:\n" +
                "\n" +
                "expected exception message:   actual: error message <java.lang.String>\n" +
                "                            expected: error message1 <java.lang.String>\n" +
                "expected exception class:   actual: class java.lang.IllegalArgumentException <java.lang.Class>\n" +
                "                          expected: class java.lang.UnsupportedOperationException <java.lang.Class>")

        code {
            throw new IllegalArgumentException("error message")
        } should throwException(UnsupportedOperationException, "error message1")
    }

    @Test
    void "should fail if no exception was thrown"() {
        thrown.expectMessage("\nmismatches:\n" +
                "\n" +
                "expected exception message:   actual: null\n" +
                "                            expected: error message1 <java.lang.String>\n" +
                "expected exception class:   actual: null\n" +
                "                          expected: class java.lang.UnsupportedOperationException <java.lang.Class>")

        code {
        } should throwException(UnsupportedOperationException, "error message1")
    }

    @Test
    void examples() {
        code {
            businessLogic(-10)
        } should throwException(IllegalArgumentException, "negative not allowed")

        code {
            businessLogic(-10)
        } should throwException(IllegalArgumentException, ~/negative.*not allowed/)

        code {
            businessLogic(-10)
        } should throwException(IllegalArgumentException)
    }

    private static businessLogic(num) {
        if (num < 0) {
            throw new IllegalArgumentException("negative not allowed")
        }
    }
}
