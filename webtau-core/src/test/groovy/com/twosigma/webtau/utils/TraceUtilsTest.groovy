package com.twosigma.webtau.utils

import org.junit.Test

class TraceUtilsTest {
    @Test
    void "converts exception to string"() {
        def e = new RuntimeException("for test")
        def stackTrace = TraceUtils.stackTrace(e)

        assert stackTrace.contains("at com.twosigma.webtau.utils.TraceUtilsTest.converts exception to string")
    }
}
