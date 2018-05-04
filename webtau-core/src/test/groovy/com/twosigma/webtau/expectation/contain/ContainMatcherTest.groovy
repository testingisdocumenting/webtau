package com.twosigma.webtau.expectation.contain

import org.junit.Test

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.contain
import static com.twosigma.webtau.Ddjt.throwException

class ContainMatcherTest {
    @Test
    void "should throw exception when value doesn't contain expected value"() {
        code {
            actual("hello world").should(contain("world!"))
        } should throwException("\ndoes not contain:\n" +
            "\n" +
            "[value]:              actual: hello world\n" +
            "         expected to contain: world!")
    }

    @Test
    void "should throw exception when value contain expected value, but should not"() {
        code {
            actual("hello world").shouldNot(contain("world"))
        } should throwException("[value] contains world\n" +
            "actual: hello world")
    }

    @Test
    void "should pass when value contains expected value"() {
        actual("hello world").should(contain("world"))
    }

    @Test
    void "should pass when value doesn't contains expected value and should not"() {
        actual("hello world").shouldNot(contain("world!"))
    }
}
