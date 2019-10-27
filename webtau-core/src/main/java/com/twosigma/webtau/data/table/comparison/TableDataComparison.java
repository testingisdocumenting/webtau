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

package com.twosigma.webtau.data.table.comparison;

import com.twosigma.webtau.data.table.header.CompositeKey;
import com.twosigma.webtau.data.table.Record;
import com.twosigma.webtau.data.table.TableData;
import com.twosigma.webtau.expectation.equality.CompareToComparator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.twosigma.webtau.Ddjt.createActualPath;
import static java.util.stream.Collectors.toSet;

public class TableDataComparison {
    private TableData actual;
    private TableData expected;

    private final Map<CompositeKey, Record> actualRowsByKey;
    private final Map<CompositeKey, Integer> actualRowIdxByKey;

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

        this.actualRowIdxByKey = new HashMap<>();
        this.actualRowsByKey = new HashMap<>();
        mapActualRowsByKeyDefinedInExpected();

        this.comparisonResult = new TableDataComparisonResult(actual, expected);
    }

    private void compare() {
        compareColumns();
        compareRows();
    }

    private void compareColumns() {
        Set<String> actualColumns = actual.getHeader().getNamesStream().collect(toSet());
        Set<String> expectedColumns = expected.getHeader().getNamesStream().collect(toSet());

        columnsToCompare = expectedColumns.stream().filter(actualColumns::contains).collect(toSet());
        expectedColumns.stream().filter(c -> ! actualColumns.contains(c)).forEach(comparisonResult::addMissingColumn);
    }

    private void compareRows() {
        reportExtraRows();
        reportMissingRows();
        compareCommonRows();
    }

    private void reportExtraRows() {
        HashSet<CompositeKey> actualKeys = new HashSet<>(actualRowIdxByKey.keySet());
        actualKeys.removeAll(expected.keySet());

        for (CompositeKey actualKey : actualKeys) {
            comparisonResult.addExtraRow(actualRowsByKey.get(actualKey));
        }
    }

    private void reportMissingRows() {
        HashSet<CompositeKey> expectedKeys = new HashSet<>(expected.keySet());
        expectedKeys.removeAll(actualRowIdxByKey.keySet());

        for (CompositeKey expectedKey : expectedKeys) {
            comparisonResult.addMissingRow(expected.find(expectedKey));
        }
    }

    private void compareCommonRows() {
        HashSet<CompositeKey> actualKeys = new HashSet<>(actualRowsByKey.keySet());
        actualKeys.retainAll(expected.keySet());

        for (CompositeKey actualKey : actualKeys) {
            Integer actualRowIdx = actualRowIdxByKey.get(actualKey);
            Integer expectedRowIdx = expected.findRowIdxByKey(actualKey);
            compare(actualRowIdx, expectedRowIdx,
                    actual.row(actualRowIdx), expected.row(expectedRowIdx));
        }
    }

    private void compare(Integer actualRowIdx, Integer expectedRowIdx, Record actual, Record expected) {
        columnsToCompare.forEach(columnName -> compare(actualRowIdx, expectedRowIdx, columnName,
                actual.get(columnName), expected.get(columnName)));
    }

    private void compare(Integer actualRowIdx, Integer expectedRowIdx, String columnName, Object actual, Object expected) {
        CompareToComparator comparator = CompareToComparator.comparator();
        boolean isEqual = comparator.compareIsEqual(createActualPath(columnName), actual, expected);

        if (isEqual) {
            return;
        }

        comparisonResult.addMismatch(actualRowIdx, expectedRowIdx, columnName, comparator.generateEqualMismatchReport());
    }

    private void mapActualRowsByKeyDefinedInExpected() {
        for (int rowIdx = 0; rowIdx < actual.numberOfRows(); rowIdx++) {
            Record row = actual.row(rowIdx);

            CompositeKey key = expected.getHeader().hasKeyColumns() ?
                    expected.getHeader().createKey(row) :
                    new CompositeKey(Stream.of(rowIdx));

            Record previous = actualRowsByKey.put(key, row);
            if (previous != null) {
                throw new IllegalArgumentException("duplicate entry found in actual table with key: " + key +
                        "\n" + previous +
                        "\n" + row);
            }

            actualRowIdxByKey.put(key, rowIdx);
        }
    }
}
