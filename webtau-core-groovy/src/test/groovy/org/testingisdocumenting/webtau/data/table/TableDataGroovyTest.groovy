/*
 * Copyright 2020 webtau maintainers
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

import org.junit.Test

import java.time.LocalDate

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.data.table.TableDataJavaTestValidations.*

class TableDataGroovyTest {
    @Test
    void "should register header and values using pipes"() {
        def tableData = createTableWithUnderscore()
        validateSimpleTableData(tableData)
    }

    @Test
    void "table header underscore should be optional"() {
        def tableData = createTableWithoutUnderscore()
        validateSimpleTableData(tableData)
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
    void "should create a new table by selecting rows by id"() {
        def table = ["*id" | "description"] {
                    __________________________
                     "id1" | "description one"
                     "id2" | "description two"
                     "id3" | "description three" }

        def newTable = table.fromRowsByKeys("id1", "id3")
        newTable.should ==  ["*id" | "description"] {
                            __________________________
                             "id1" | "description one"
                             "id3" | "description three" }
    }

    @Test
    void "should create a new table by selecting rows by composite keys"() {
        def table = ["*id1" | "*id2" | "description"] {
                    _____________________________________
                     "id11" | "id21" | "description one"
                     "id21" | "id22" | "description two"
                     "id31" | "id32" | "description three" }

        def newTable = table.fromRowsByKeys(key("id11", "id21"), key("id31", "id32"))
        newTable.should ==  ["*id1" | "*id2" | "description"] {
                             _____________________________________
                             "id11" | "id21" | "description one"
                             "id31" | "id32" | "description three" }
    }

    @Test
    void "should merge in another table using only matching column names"() {
        def table = ["hello" | "world"] {
                         12  | 46
                         54  | 88  }

        def incoming = ["extra" | "hello"] {
                         700    | 24
                         800    | 48 }

        table.addRowsExistingColumnsOnly(incoming)
        table.should == ["hello" | "world"] {
                             12  | 46
                             54  | 88
                             24  | null
                             48  | null }
    }

    @Test
    void "should generate multiple rows from multi-values"() {
        def tableData = createTableDataWithPermute()
        validatePermute(tableData)
    }

    @Test
    void "should generate ids for multiple rows from multi-values"() {
        def tableData = createTableDataWithPermuteAndGuid()
        validatePermuteAndGuid(tableData)
    }

    @Test
    void "cell above should be substituted with value from a previous row"() {
        def tableData = createTableDataWithAboveRef()
        validateAboveValue(tableData)
    }

    @Test
    void "cell above should should support plus operation"() {
        def tableData = createTableDataWithAboveRefAndMath()
        validateAboveValueWithMath(tableData)
    }

    @Test
    void "cell above should should support plus operation extracted"() {
        def tableData = createTableDataWithAboveRefAndMathExtracted()
        validateAboveValueWithMath(tableData)
    }

    @Test
    void "should replace single specified value"() {
        def tableData = createTableWithUnderscore()
        TableData newTableData = replaceValue(tableData)

        validateSimpleTableData(tableData)
        validateSimpleTableDataAfterReplace(newTableData)
    }

    @Test
    void "access by key column"() {
        TableData tableData = createTableWithKeyColumn()
        findByKeyAndValidate(tableData)
    }

    @Test
    void "should error when find by key without keys defined"() {
        def table = createTableWithUnderscore()
        code {
            table.findByKey("id")
        } should throwException("no key columns defined")
    }

    @Test
    void "should error when find by key has different number of key parts"() {
        def table = createTableWithMultipleKeyColumns()
        code {
            table.findByKey("id")
        } should throwException("header has <2> key column(s) but provided key has <1>: [id1, id2]")
    }

    @Test
    void "table creation from list of maps"() {
        def list = [
                [k1: "v1", k2: "v2"],
                [k1: "v3", k3: "v4"]]

        TableData tableData = TableData.fromListOfMaps(list)

        TableData expected = ["k1" | "k2" | "k3"] {
                              ____________________
                              "v1" | "v2" | null
                              "v3" | null | "v4" }

        tableData.should == expected
    }

    @Test
    void "should change key columns and validate uniqueness"() {
        def tableData = createTableWithKeyColumn()
        code {
            changeKeyColumns(tableData)
        } should throwException("duplicate entry found with key: [N, T]\n" +
                "{id=id1, Name=N, Type=T}\n" +
                "{id=id3, Name=N, Type=T}")
    }

    @Test
    void "to string should use pretty printed version without colors"() {
        def tableData = createTableWithKeyColumn()

        tableData.toString().should == '*id   │ Name │ Type\n' +
                '"id1" │ "N"  │ "T" \n' +
                '"id2" │ "N2" │ "T2"\n' +
                '"id3" │ "N"  │ "T" '
    }

    private static TableData replaceValue(TableData tableData) {
        tableData.replace("v1b", "v1b_")
    }

    private static TableData changeKeyColumns(TableData tableData) {
        tableData.withNewKeyColumns("Name", "Type")
    }

    private static void findByKeyAndValidate(TableData tableData) {
        def found = tableData.findByKey("id2")
        found.Name.should == "N2"
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

    static TableData createTableDataWithPermuteAndGuid() {
        ["ID"      | "Col A"              | "Col B"         | "Col C"] {
        ________________________________________________________________________
         cell.guid | permute(true, false) | "v1b"           | permute('a', 'b')
         cell.guid | "v2a"                | permute(10, 20) | "v2c" }
    }

    static TableData createTableDataWithAboveRef() {
        ["Name" | "Start Date"              | "Games To Play" ] {
         ______________________________________________________
         "John" | LocalDate.of(2016, 6, 20) | 10
         "Bob"  | cell.above                |  8
         "Mike" | cell.above                | 14

         "Drew" | LocalDate.of(2016, 6, 22) | 10
         "Pete" | cell.above                | 11
         "Max"  | cell.above                |  3 }
    }

    static TableData createTableDataWithAboveRefAndMath() {
        ["Name" | "Start Date"              | "Games To Play" ] {
         ______________________________________________________
         "John" | LocalDate.of(2016, 6, 20) | 10
         "Bob"  | cell.above                | cell.above + 1
         "Mike" | cell.above                | cell.above + 1 }
    }

    static TableData createTableDataWithAboveRefAndMathExtracted() {
        def increment = cell.above + 1

        ["Name" | "Start Date"              | "Games To Play" ] {
         ______________________________________________________
         "John" | LocalDate.of(2016, 6, 20) | 10
         "Bob"  | cell.above                | increment
         "Mike" | cell.above                | increment }
    }

    static TableData createTableWithKeyColumn() {
        ["*id" | "Name" | "Type"] {
        ___________________________
         "id1" | "N"    | "T"
         "id2" | "N2"   | "T2"
         "id3" | "N"    | "T" }
    }

    static TableData createTableWithMultipleKeyColumns() {
        ["*id1" | "*id2" | "Type"] {
        ___________________________
          "id1" | "sid1" | "T"
          "id2" | "sid2" | "T2"
          "id3" | "sid3" | "T" }
    }
}