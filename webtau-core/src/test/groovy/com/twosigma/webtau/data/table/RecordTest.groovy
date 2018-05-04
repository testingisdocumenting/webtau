package com.twosigma.webtau.data.table

import org.junit.Test

import static com.twosigma.webtau.Ddjt.*

class RecordTest {
    @Test
    void "should be convertible to map"() {
        def record = new Record(new Header(["n1", "n2"].stream()), ["v1", null].stream())
        actual(record.toMap()).should(equal([n1: 'v1', n2: null]))
    }

    @Test
    void "should have key defined if header has key columns"() {
        def record = new Record(new Header(["*id1", "*id2", "c1"].stream()), ["id1", "id2", "v1"].stream())
        assert record.key.values == ["id1", "id2"]
    }

    @Test
    void "should have null key if header has no key columns"() {
        def record = new Record(new Header(["c1", "c2", "c3"].stream()), ["v1", "v2", "v3"].stream())
        assert record.key == null
    }
}
