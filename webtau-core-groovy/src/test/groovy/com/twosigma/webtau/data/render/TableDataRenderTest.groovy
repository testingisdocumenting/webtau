package com.twosigma.webtau.data.render

import org.junit.Test

import static com.twosigma.webtau.Ddjt.header
import static org.junit.Assert.assertEquals

class TableDataRenderTest {
    @Test
    void "should determine width for each column"() {
        def t = header("Column A", "CB", "Column C")
        t.addRow(["long line of text\nspread across multiple\nlines", "A", "test"])
        t.addRow(["little bit", "CC", "some more\nin two lines"])

        def expected = "\n" +
                ":Column A              |CB|Column C    :\n" +
                ".______________________.__.____________.\n" +
                "|long line of text     |A |test        |\n" +
                "|spread across multiple|  |            |\n" +
                "|lines                 |  |            |\n" +
                ".______________________.__.____________|\n" +
                "|little bit            |CC|some more   |\n" +
                "|                      |  |in two lines|\n" +
                ".______________________.__.____________|\n"

        assertEquals(expected, new TableDataRenderer().render(t))
    }
}
