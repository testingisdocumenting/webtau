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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.twosigma.webtau.data.table.Record;
import com.twosigma.webtau.data.table.TableData;

public class TableDataComparisonResult {
    private Map<Integer, Map<String, String>> messageByActualRowIdxAndColumn;
    private Map<Integer, Map<String, String>> messageByExpectedRowIdxAndColumn;

    private Set<String> missingColumns;
    private TableData missingRows;
    private TableData extraRows;

    private TableData actual;
    private TableData expected;

    public TableDataComparisonResult(TableData actual, TableData expected) {
        this.actual = actual;
        this.expected = expected;

        messageByActualRowIdxAndColumn = new HashMap<>();
        messageByExpectedRowIdxAndColumn = new HashMap<>();

        missingColumns = new TreeSet<>();
        missingRows = new TableData(expected.getHeader());
        extraRows = new TableData(actual.getHeader());
    }

    public boolean areEqual() {
        return messageByActualRowIdxAndColumn.isEmpty() &&
            missingColumns.isEmpty() &&
            missingRows.isEmpty() &&
            extraRows.isEmpty();
    }

    public Map<Integer, Map<String, String>> getMessageByActualRowIdxAndColumn() {
        return messageByActualRowIdxAndColumn;
    }

    public Map<Integer, Map<String, String>> getMessageByExpectedRowIdxAndColumn() {
        return messageByExpectedRowIdxAndColumn;
    }

    public TableData getActual() {
        return actual;
    }

    public TableData getExpected() {
        return expected;
    }

    public void addMissingColumn(String name) {
        missingColumns.add(name);
    }

    public void addExtraRow(Record row) {
        extraRows.addRow(row);
    }

    public void addMissingRow(Record row) {
        missingRows.addRow(row);
    }

    public Set<String> getMissingColumns() {
        return missingColumns;
    }

    public TableData getMissingRows() {
        return missingRows;
    }

    public TableData getExtraRows() {
        return extraRows;
    }

    public void addMismatch(Integer actualRowIdx, Integer expectedRowIdx, String columnName, String message) {
        addMismatch(messageByActualRowIdxAndColumn, actualRowIdx, columnName, message);
        addMismatch(messageByExpectedRowIdxAndColumn, expectedRowIdx, columnName, message);
    }

    public String getActualMismatch(Integer rowIdx, String columnName) {
        return getMismatch(messageByActualRowIdxAndColumn, rowIdx, columnName);
    }

    public String getExpectedMismatch(Integer rowIdx, String columnName) {
        return getMismatch(messageByExpectedRowIdxAndColumn, rowIdx, columnName);
    }

    private String getMismatch(Map<Integer, Map<String, String>> messagesByRowIdx, int rowIdx, String columnName) {
        Map<String, String> byRow = messagesByRowIdx.get(rowIdx);
        if (byRow == null) {
            return null;
        }

        return byRow.get(columnName);
    }

    private void addMismatch(Map<Integer, Map<String, String>> messagesByRowIdx, int rowIdx, String columnName, String message) {
        Map<String, String> byRow = messagesByRowIdx.computeIfAbsent(rowIdx, k -> new HashMap<>());
        byRow.put(columnName, message);
    }
}
