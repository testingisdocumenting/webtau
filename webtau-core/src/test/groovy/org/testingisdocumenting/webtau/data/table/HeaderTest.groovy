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

package org.testingisdocumenting.webtau.data.table

import org.testingisdocumenting.webtau.data.table.header.Header
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
        assert header.getKeyNamesStream().collect(toList()) == ["ColumnB", "ColumnC"]
        assert header.getKeyIdxStream().collect(toList()) == [1, 2]
    }

    @Test
    void "knows when key columns are defined"() {
        assert header.hasKeyColumns()

        def keyLessHeader = new Header(Stream.of("ColumnA", "ColumnB"))
        assert ! keyLessHeader.hasKeyColumns()
    }
}
