package com.twosigma.webtau.utils

import org.junit.Assert
import org.junit.Test

class RegexpUtilsTest {
    @Test
    void "replace matches with a callback result"() {
        def replaced = RegexpUtils.replaceAll("hello 10 world of 42 numbers", ~/(\d)\d*/, { m -> '"' + m.group(1) + '"' })
        Assert.assertEquals('hello "1" world of "4" numbers', replaced)
    }
}
