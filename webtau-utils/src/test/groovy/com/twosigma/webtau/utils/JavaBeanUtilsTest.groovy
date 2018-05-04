package com.twosigma.webtau.utils

import org.junit.Test

class JavaBeanUtilsTest {
    @Test
    void "converts classic get/set bean without fields to a map"() {
        def asMap = JavaBeanUtils.convertBeanToMap(new ClassicBean())
        assert asMap == [description: "d1", price: 100]
    }

    @Test
    void "converts read-only bean with fields to a map"() {
        def asMap = JavaBeanUtils.convertBeanToMap(new BeanWithFields(20, "text"))
        assert asMap == [propA: 20, propB: "text"]
    }

    @Test
    void "converts bean with nulls to a map"() {
        def asMap = JavaBeanUtils.convertBeanToMap(new BeanWithFields(10, null))
        assert asMap == [propA: 10, propB: null]
    }
}
