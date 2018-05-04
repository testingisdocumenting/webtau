package com.twosigma.webtau.expectation.contain.handlers

import com.twosigma.webtau.expectation.contain.ContainAnalyzer
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import static com.twosigma.webtau.Ddjt.createActualPath

class StringContainHandlerTest {
    private ContainAnalyzer analyzer

    @Before
    void init() {
        analyzer = ContainAnalyzer.containAnalyzer()
    }

    @Test
    void "handles only strings"() {
        def handler = new StringContainHandler()
        assert handler.handle("text", "ext")
        assert !handler.handle("text", 10)
        assert !handler.handle(10, 20)
    }

    @Test
    void "no mismatches when actual string contains expected"() {
        assert analyzer.contains(createActualPath("text"), "hello world", "world")
    }

    @Test
    void "mismatches report should have both values"() {
        assert ! analyzer.contains(createActualPath("text"), "hello world", "disc")

        Assert.assertEquals("does not contain:\n" +
                "\n" +
                "text:              actual: hello world\n" +
                "      expected to contain: disc", analyzer.generateMismatchReport())
    }
}
