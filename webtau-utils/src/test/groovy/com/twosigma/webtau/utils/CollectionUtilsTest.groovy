package com.twosigma.webtau.utils

import org.junit.Test

class CollectionUtilsTest {
    @Test
    void "should create map from the vararg sequence"() {
        def map = CollectionUtils.createMap("key1", 10, "key2", 20, "key3", 30)
        assert map == [key1: 10, key2: 20, key3: 30]
    }

    @Test(expected = IllegalArgumentException)
    void "should validate completeness of the map when create from varargs"() {
        CollectionUtils.createMap("key1", 10, "key2")
    }
}
