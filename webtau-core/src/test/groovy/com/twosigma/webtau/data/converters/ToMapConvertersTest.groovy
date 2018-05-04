package com.twosigma.webtau.data.converters

import org.junit.Test

class ToMapConvertersTest {
    @Test
    void "defaults to javabean to map converter"() {
        assert ToMapConverters.convert(100) == [DummyNumberToMapConverter: 100]

        def asMap = ToMapConverters.convert(new SimpleBean())
        assert asMap.containsKey("a")
        assert asMap.containsKey("b")
    }

    static class SimpleBean {
        Integer a
        String b
    }
}
