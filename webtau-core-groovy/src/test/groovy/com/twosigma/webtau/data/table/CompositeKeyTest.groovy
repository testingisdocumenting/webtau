package com.twosigma.webtau.data.table

import org.junit.Test

class CompositeKeyTest {
    @Test
    void "should equal when all the parts are equal"() {
        def key1 = new CompositeKey(['a', 'b'].stream())
        def key2 = new CompositeKey(['a', 'b'].stream())

        assert key1 == key2
        assert key1.hashCode() == key2.hashCode()
    }

    @Test
    void "hash code should be calculated for all parts of the key"() {
        assert new CompositeKey([10].stream()).hashCode() != new CompositeKey([10, 20].stream()).hashCode()
    }

    @Test
    void "order of parts should matter"() {
        def key1 = new CompositeKey([10, 20].stream())
        def key2 = new CompositeKey([20, 10].stream())

        assert key1 != key2
        assert key1.hashCode() != key2.hashCode()
    }
}
