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

package com.twosigma.webtau.data.table

import org.junit.Test

import static com.twosigma.webtau.Ddjt.*

class TableDataExtensionTest {
    @Test
    void "should register header and values using pipes"() {
        def tableData = createTableWithUnderscore()
        validateTableData(tableData)
    }

    @Test
    void "table header underscore should be optional"() {
        def tableData = createTableWithoutUnderscore()
        validateTableData(tableData)
    }

    @Test
    void "should support null values"() {
        def table = ["hello" | "world"] {
                         12  | 46
                         54  | null  }

        table.size().should == 2
        table.header.getNamesStream().should == ["hello", "world"]
        table.row(0).should == [hello: 12, world: 46]
        table.row(1).should == [hello: 54, world: null]
    }

    @Test
    void "should generate multiple rows from multi-values"() {
        def tableData = createTableDataWithPermute()

        assert tableData.numberOfRows() == 6
        assert tableData.row(0).toMap() == ['Col A': true, 'Col B': 'v1b', 'Col C': 'a']
        assert tableData.row(1).toMap() == ['Col A': false, 'Col B': 'v1b', 'Col C': 'a']
        assert tableData.row(2).toMap() == ['Col A': true, 'Col B': 'v1b', 'Col C': 'b']
        assert tableData.row(3).toMap() == ['Col A': false, 'Col B': 'v1b', 'Col C': 'b']
        assert tableData.row(4).toMap() == ['Col A': 'v2a', 'Col B': 10, 'Col C': 'v2c']
        assert tableData.row(5).toMap() == ['Col A': 'v2a', 'Col B': 20, 'Col C': 'v2c']
    }

    @Test
    void "cell previous should be substituted with value from a previous row"() {
        def tableData = createTableDataWithPreviousRef()
        assert tableData.numberOfRows() == 3
        assert tableData.row(0).toMap() == ["Col A": "v1a", "Col B": "v1b", "Col C": 10]
        assert tableData.row(1).toMap() == ["Col A": "v2a", "Col B": "v2b", "Col C": 10]
        assert tableData.row(2).toMap() == ["Col A": "v2a", "Col B": "v2b", "Col C": 20]
    }

    @Test
    void "should ignore underscore under header"() {
        def table = ["hello" | "world"] {
                    ___________________
                         12  | 46
                         54  | null  }
        table.size().should == 2
    }

    static TableData createTableWithUnderscore() {
        ["Col A" | "Col B" | "Col C"] {
        ________________________________
           "v1a" |   "v1b" | "v1c"
           "v2a" |   "v2b" | "v2c" }
    }

    static TableData createTableWithoutUnderscore() {
        ["Col A" | "Col B" | "Col C"] {
           "v1a" |   "v1b" | "v1c"
           "v2a" |   "v2b" | "v2c" }
    }

    static TableData createTableDataWithPermute() {
        ["Col A"              | "Col B"         | "Col C"] {
        ___________________________________________________________
         permute(true, false) | "v1b"           | permute('a', 'b')
         "v2a"                | permute(10, 20) | "v2c" }
    }

    static TableData createTableDataWithPreviousRef() {
        ["Col A" | "Col B" | "Col C"] {
        __________________________________________
           "v1a" |   "v1b" | 10
           "v2a" |   "v2b" | cell.previous
           "v2a" |   "v2b" | cell.previous + 10 }
    }

    private static void validateTableData(TableData tableData) {
        tableData.numberOfRows().should == 2
        tableData.row(0).toMap().should == ["Col A": "v1a", "Col B": "v1b", "Col C": "v1c"]
        tableData.row(1).toMap().should == ["Col A": "v2a", "Col B": "v2b", "Col C": "v2c"]
    }
}
