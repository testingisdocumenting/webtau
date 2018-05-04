package com.twosigma.webtau.data.table

import org.junit.Test

import static com.twosigma.webtau.Ddjt.header

class TableDataTest {
    @Test
    void "should create table using header and values convenient methods"() {
        def tableData = header("Col A", "Col B", "Col C").values(
                                 "v1a",   "v1b", "v1c",
                                 "v2a",   "v2b", "v2c")

        assert tableData.numberOfRows() == 2
        assert tableData.row(0).toMap() == ["Col A": "v1a", "Col B": "v1b", "Col C": "v1c"]
        assert tableData.row(1).toMap() == ["Col A": "v2a", "Col B": "v2b", "Col C": "v2c"]
    }

    @Test(expected = IllegalArgumentException)
    void "should report columns number mismatch during table creation using header and values vararg methods"() {
        header("Col A", "Col B", "Col C").values(
                 "v1a",   "v1b", "v1c",
                 "v2a",   "v2b")
    }
}
