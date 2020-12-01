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

package org.testingisdocumenting.webtau.data.table;

import org.testingisdocumenting.webtau.data.table.autogen.TableDataCellValueGenerator;
import org.testingisdocumenting.webtau.documentation.DocumentationArtifacts;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.testingisdocumenting.webtau.Matchers.code;
import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.data.table.TableDataJavaTestValidations.*;

public class TableDataJavaTest {
    private static final TableDataCellValueGenerator<?> increment = cell.above.plus(1);

    @Test
    public void shouldCreateTableUsingConvenienceMethodsForTableAndValues() {
        TableData tableData = createTableDataSeparateValues();
        validateSimpleTableData(tableData);
    }

    @Test
    public void shouldCreateTableUsingSingleTableMethod() {
        TableData tableData = createTableDataInOneGo();
        validateSimpleTableData(tableData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReportColumnsNumberMismatchDuringTableCreationUsingHeaderAndValuesVarargMethod() {
        table("Col A", "Col B", "Col C").values(
                "v1a",   "v1b", "v1c",
                "v2a",   "v2b");
    }

    @Test
    public void shouldGenerateMultipleRowsFromMultiValues() {
        TableData tableData = createTableDataWithPermute();

        validatePermute(tableData);
        DocumentationArtifacts.createAsJson(TableDataJavaTest.class, "table-with-permute", tableData);
    }

    @Test
    public void shouldGenerateIdsForMultipleRowsFromMultiValues() {
        TableData tableData = createTableDataWithPermuteAndGuid();

        validatePermuteAndGuid(tableData);
        DocumentationArtifacts.createAsJson(TableDataJavaTest.class, "table-with-permute-and-guid", tableData);
    }

    @Test
    public void cellAboveShouldBeSubstitutedWithValueFromPreviousRow() {
        TableData tableData = createTableDataWithAboveRef();
        validateAboveValue(tableData);

        saveTableWithDate(tableData, "table-with-cell-above");
    }

    @Test
    public void cellAboveShouldSupportPlusOperation() {
        TableData tableData = createTableDataWithAboveRefAndMath();
        validateAboveValueWithMath(tableData);

        saveTableWithDate(tableData, "table-with-cell-above-math");
    }

    @Test
    public void cellAboveShouldSupportPlusOperationWithExtraction() {
        TableData tableData = createTableDataWithAboveRefAndMathExtracted();
        validateAboveValueWithMath(tableData);
    }

    @Test
    public void shouldReplaceSingleSpecifiedValue() {
        TableData tableData = createTableDataInOneGo();
        TableData newTableData = replaceValue(tableData);

        validateSimpleTableData(tableData);
        validateSimpleTableDataAfterReplace(newTableData);

        saveTableWithDate(newTableData, "table-after-replace");
    }

    @Test
    public void accessByKeyColumn() {
        TableData tableData = createTableWithKeyColumns();
        findByKeyAndValidate(tableData);
    }

    @Test
    public void shouldChangeKeyColumnsAndValidateUniqueness() {
        TableData tableData = createTableWithKeyColumns();

        code(() ->
            changeKeyColumns(tableData)
        ).should(throwException("duplicate entry found with key: [N, T]\n" +
                "{id=id1, Name=N, Type=T}\n" +
                "{id=id3, Name=N, Type=T}"));
    }

    private static TableData replaceValue(TableData tableData) {
        return tableData.replace("v1b", "v1b_");
    }

    private static TableData changeKeyColumns(TableData tableData) {
        return tableData.withNewKeyColumns("Name", "Type");
    }

    private static void findByKeyAndValidate(TableData tableData) {
        Record found = tableData.find(key("id2"));
        actual(found.get("Name")).should(equal("N2"));
    }

    private static TableData createTableDataSeparateValues() {
        return table("Col A", "Col B", "Col C").values(
                       "v1a",   "v1b", "v1c",
                       "v2a",   "v2b", "v2c");
    }

    private static TableData createTableDataInOneGo() {
        return table("Col A", "Col B", "Col C",
                     ________________________________,
                       "v1a",   "v1b", "v1c",
                       "v2a",   "v2b", "v2c");
    }

    private static TableData createTableDataWithPermute() {
        return table("Col A"              , "Col B"         , "Col C",
                     ________________________________________________________________,
                      permute(true, false), "v1b"           , permute('a', 'b'),
                      "v2a"               , permute(10, 20) , "v2c");
    }

    private static TableData createTableDataWithPermuteAndGuid() {
        return table("ID"      , "Col A"              , "Col B"         , "Col C",
                     ______________________________________________________________________,
                      cell.guid, permute(true, false), "v1b"           , permute('a', 'b'),
                      cell.guid, "v2a"               , permute(10, 20) , "v2c");
    }

    private static TableData createTableDataWithAboveRef() {
        return table("Name", "Start Date"             , "Games To Play",
                     __________________________________________________,
                     "John", LocalDate.of(2016, 6, 20), 10,
                     "Bob" , cell.above               ,  8,
                     "Mike", cell.above               , 14,

                     "Drew", LocalDate.of(2016, 6, 22), 10,
                     "Pete", cell.above               , 11,
                     "Max" , cell.above               ,  3);
    }

    private static TableData createTableDataWithAboveRefAndMath() {
        return table("Name", "Start Date"             , "Games To Play",
                    ________________________________________________________________,
                     "John", LocalDate.of(2016, 6, 20), 10,
                     "Bob" , cell.above               , cell.above.plus(1),
                     "Mike", cell.above               , cell.above.plus(1));
    }

    private static void createIncrementExample() {
        TableDataCellValueGenerator<?> increment = cell.above.plus(1);
    }

    private static TableData createTableDataWithAboveRefAndMathExtracted() {
        return table("Name", "Start Date"             , "Games To Play",
                    ________________________________________________________________,
                     "John", LocalDate.of(2016, 6, 20), 10,
                     "Bob" , cell.above               , increment,
                     "Mike", cell.above               , increment);
    }

    static TableData createTableWithKeyColumns() {
        return table("*id" , "Name" , "Type",
                     _______________________,
                     "id1" , "N"    , "T",
                     "id2" , "N2"   , "T2",
                     "id3" , "N"    , "T");
    }
    
    private void saveTableWithDate(TableData tableData, String artifactName) {
        DocumentationArtifacts.createAsJson(TableDataJavaTest.class, artifactName,
                tableData
                        .map((rowIdx, colIdx, columnName, v) ->
                                columnName.equals("Start Date") ?
                                        ((LocalDate) v).format(DateTimeFormatter.ISO_DATE) : v));
    }
}
