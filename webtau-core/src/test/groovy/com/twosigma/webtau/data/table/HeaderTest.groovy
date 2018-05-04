package com.twosigma.webtau.data.table

import org.junit.Test

import java.util.stream.Stream

import static java.util.stream.Collectors.toList

class HeaderTest {
    def header = new Header(Stream.of("ColumnA", "*ColumnB", "*ColumnC", "ColumnD"))

    @Test
    void "knows columns index"() {
        assert header.columnIdxByName("ColumnD") == 3
    }

    @Test
    void "knows if column is present"() {
        assert header.has("ColumnA")
        assert ! header.has("ColumnX")
    }

    @Test
    void "defines key columns using asterisk in front of column name"() {
        assert header.getKeyNames().collect(toList()) == ["ColumnB", "ColumnC"]
        assert header.getKeyIdx().collect(toList()) == [1, 2]
    }

    @Test
    void "knows when key columns are defined"() {
        assert header.hasKeyColumns()

        def keyLessHeader = new Header(Stream.of("ColumnA", "ColumnB"))
        assert ! keyLessHeader.hasKeyColumns()
    }
}
