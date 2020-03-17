/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.webtau.data.render

import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.table
import static org.junit.Assert.assertEquals

class TableDataRenderTest {
    @Test
    void "should determine width for each column"() {
        def t = table('Column A', 'CB', 'Column C')
        t.addRow(['long line of text\nspread across multiple\nlines', 'A', 'test'])
        t.addRow(['little bit', 'CC', 'some more\nin two lines'])

        def expected = '\n' +
            ':Column A              |CB  |Column C     :\n' +
            '.______________________.____._____________.\n' +
            '|"long line of text    |"A" |"test"       |\n' +
            '|spread across multiple|    |             |\n' +
            '|lines"                |    |             |\n' +
            '.______________________.____._____________|\n' +
            '|"little bit"          |"CC"|"some more   |\n' +
            '|                      |    |in two lines"|\n' +
            '.______________________.____._____________|\n'

        assertEquals(expected, new TableDataRenderer().render(t))
    }
}
