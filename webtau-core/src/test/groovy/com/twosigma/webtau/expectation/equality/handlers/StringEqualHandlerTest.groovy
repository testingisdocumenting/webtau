package com.twosigma.webtau.expectation.equality.handlers

import com.twosigma.webtau.expectation.equality.EqualComparator
import org.junit.Test

import static com.twosigma.webtau.Ddjt.*
import static org.junit.Assert.assertEquals

class StringEqualHandlerTest {
    @Test
    void "handles instances of String and Characters as actual or expected"() {
        def handler = new StringEqualHandler()

        assert handler.handle("test", "test")
        assert handler.handle("t", Character.valueOf('t'.toCharArray()[0]))
        assert handler.handle(Character.valueOf('t'.toCharArray()[0]), "t")

        assert ! handler.handle("test", 100)
        assert ! handler.handle("test", ~/regexp/)
    }

    @Test
    void "compares string and character"() {
        actual("t").should(equal(Character.valueOf('t'.toCharArray()[0])))
    }

    @Test
    void "reports types before conversion to string in case of failure"() {
        def comparator = EqualComparator.comparator()
        comparator.compare(createActualPath("text"), Character.valueOf('t'.toCharArray()[0]), "b")

        assertEquals("mismatches:\n" +
                "\n" +
                "text:   actual: t <java.lang.String>(before conversion: t <java.lang.Character>)\n" +
                "      expected: b <java.lang.String>",
                comparator.generateMismatchReport())

    }
}
