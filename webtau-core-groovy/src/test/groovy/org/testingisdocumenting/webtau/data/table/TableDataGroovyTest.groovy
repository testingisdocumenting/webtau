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

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.data.table.TableDataJavaTestValidations.validateAboveValue
import static org.testingisdocumenting.webtau.data.table.TableDataJavaTestValidations.validateAboveValueWithMath
import static org.testingisdocumenting.webtau.data.table.TableDataJavaTestValidations.validatePermute
import static org.testingisdocumenting.webtau.data.table.TableDataJavaTestValidations.validatePermuteAndGuid
import static org.testingisdocumenting.webtau.data.table.TableDataJavaTestValidations.validateSimpleTableData
import static org.testingisdocumenting.webtau.data.table.TableDataJavaTestValidations.validateSimpleTableDataAfterReplace

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

    private static TableData replaceValue(TableData tableData) {
        tableData.replace("v1b", "v1b_")
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
}
