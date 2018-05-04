/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.data.table.comparison;

import java.util.Set;

import com.twosigma.webtau.data.table.Record;
import com.twosigma.webtau.data.table.TableData;
import com.twosigma.webtau.expectation.equality.EqualComparator;

import static com.twosigma.webtau.Ddjt.createActualPath;
import static java.util.stream.Collectors.toSet;

public class TableDataComparison {
    private TableData actual;
    private TableData expected;
    private TableDataComparisonResult comparisonResult;
    private Set<String> columnsToCompare;

    public static TableDataComparisonResult compare(TableData actual, TableData expected) {
        TableDataComparison comparison = new TableDataComparison(actual, expected);
        comparison.compare();

        return comparison.comparisonResult;
    }

    private TableDataComparison(TableData actual, TableData expected) {
        this.actual = actual;
        this.expected = expected;
        this.comparisonResult = new TableDataComparisonResult(actual, expected);
    }

    private void compare() {
        compareColumns();
        compareRows();
    }

    private void compareColumns() {
        Set<String> actualColumns = actual.getHeader().getNames().collect(toSet());
        Set<String> expectedColumns = expected.getHeader().getNames().collect(toSet());

        columnsToCompare = expectedColumns.stream().filter(actualColumns::contains).collect(toSet());
        expectedColumns.stream().filter(c -> ! actualColumns.contains(c)).forEach(comparisonResult::addMissingColumn);
    }

    // TODO handle key columns

    private void compareRows() {
        reportExtraRows();
        reportMissingRows();
        compareCommonRows();
    }

    private void reportExtraRows() {
        for (int rowIdx = expected.numberOfRows(); rowIdx < actual.numberOfRows(); rowIdx++) {
            comparisonResult.addExtraRow(actual.row(rowIdx));
        }
    }

    private void reportMissingRows() {
        for (int rowIdx = actual.numberOfRows(); rowIdx < expected.numberOfRows(); rowIdx++) {
            comparisonResult.addMissingRow(expected.row(rowIdx));
        }
    }

    private void compareCommonRows() {
        int commonRowsSize = Math.min(actual.numberOfRows(), expected.numberOfRows());

        for (int rowIdx = 0; rowIdx < commonRowsSize; rowIdx++) {
            compare(rowIdx, actual.row(rowIdx), expected.row(rowIdx));
        }
    }

    private void compare(int rowIdx, Record actual, Record expected) {
        columnsToCompare.forEach(columnName -> compare(rowIdx, columnName, actual.get(columnName), expected.get(columnName)));
    }

    private void compare(int rowIdx, String columnName, Object actual, Object expected) {
        EqualComparator ec = EqualComparator.comparator();
        ec.compare(createActualPath(columnName), actual, expected);

        if (ec.areEqual())
            return;

        comparisonResult.addMismatch(rowIdx, rowIdx, columnName, ec.generateMismatchReport());
    }
}
