package com.twosigma.webtau.expectation.equality.handlers

import org.junit.Test

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.equal

class ArrayAndIterableCompareToHandlerTest {
    @Test
    void "string array and list match"() {
        String[] strings = ['a', 'b', 'c']

        actual(strings).should(equal(['a', 'b', 'c']))
    }

    @Test
    void "boolean array and list match"() {
        boolean[] booleans = [true, false, true, false]

        actual(booleans).should(equal([true, false, true, false]))
    }

    @Test
    void "byte array and list match"() {
        byte[] bytes = [1, 2, 3]

        actual(bytes).should(equal(bytes.toList()))
    }

    @Test
    void "char array and list match"() {
        char[] chars = ['a' as char, 'b' as char, 'c' as char]

        actual(chars).should(equal(['a' as char, 'b' as char, 'c' as char]))
    }

    @Test
    void "short array and list match"() {
        short[] shorts = [1, 2, 3]

        actual(shorts).should(equal([1 as short, 2 as short, 3 as short]))
    }

    @Test
    void "int array and list match"() {
        int[] ints = [1, 2, 3]

        actual(ints).should(equal([1, 2, 3]))
    }

    @Test
    void "long array and list match"() {
        long[] longs = [1L, 2L, 3L]

        actual(longs).should(equal([1L, 2L, 3L]))
    }

    @Test
    void "float array and list match"() {
        float[] floats = [1F, 2F, 3F]

        actual(floats).should(equal([1F, 2F, 3F]))
    }

    @Test
    void "double array and list match"() {
        double[] doubles = [1D, 2D, 3D]

        actual(doubles).should(equal([1D, 2D, 3D]))
    }
}
