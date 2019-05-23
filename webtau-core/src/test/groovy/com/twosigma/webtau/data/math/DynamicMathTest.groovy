package com.twosigma.webtau.data.math

import org.junit.Test

class DynamicMathTest {
    @Test
    void "should use discovered simple math handlers for add operation"() {
        assert DynamicMath.add(10.0d, 10L) == 20.0d
        assert DynamicMath.add(10L, 5i) == 15L
    }

    @Test
    void "should use discovered simple math handlers for remove operation"() {
        assert DynamicMath.subtract(10.0d, 5L) == 5.0d
        assert DynamicMath.subtract(10L, 5i) == 5L
    }
}