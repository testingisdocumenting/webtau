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

package com.twosigma.webtau.data.table;

import com.twosigma.webtau.documentation.DocumentationArtifacts;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.twosigma.webtau.Ddjt.*;
import static com.twosigma.webtau.data.table.TableDataJavaTestValidations.*;

public class TableDataJavaTest {
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

    @Test
    public void cellAboveShouldBeSubstitutedWithValueFromPreviousRow() {
        TableData tableData = createTableDataWithAboveRef();
        validateAboveValue(tableData);


        DocumentationArtifacts.create(TableDataJavaTest.class, "table-with-cell-above.json",
                tableData
                        .map((rowIdx, colIdx, columnName, v) ->
                                columnName.equals("Start Date") ? ((LocalDate)v).format(DateTimeFormatter.ISO_DATE) : v)
                        .toJson());
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
        DocumentationArtifacts.create(TableDataJavaTest.class, "table-with-permute.json",
                tableData.toJson());
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

    private static TableData createTableDataWithAboveRef() {
        return table("Name", "Start Date"             , "Games To Play",
                     ________________________________________________,
                     "John", LocalDate.of(2016, 6, 20), 10,
                     "Bob" , cell.above               ,  8,
                     "Mike", cell.above               , 14,

                     "Drew", LocalDate.of(2016, 6, 22), 10,
                     "Pete", cell.above               , 11,
                     "Max" , cell.above               ,  3);
    }
}
