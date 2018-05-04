package com.twosigma.webtau.utils

import org.junit.Test

class NumberUtilsTest {
    @Test
    void "should convert double as string to double"() {
        def number = NumberUtils.convertStringToNumber("12.45")
        assert number.class == Double
        assert number == 12.45
    }

    @Test
    void "should convert number without decimals as string to long"() {
        def number = NumberUtils.convertStringToNumber("12")
        assert number.class == Long
        assert number == 12
    }
}
