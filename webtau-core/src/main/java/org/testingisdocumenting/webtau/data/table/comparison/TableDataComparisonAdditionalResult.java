/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.data.table.comparison;

import java.util.*;

import org.testingisdocumenting.webtau.data.table.Record;
import org.testingisdocumenting.webtau.data.table.TableData;

public class TableDataComparisonAdditionalResult {
    private final Set<String> missingColumns;
    private final TableData missingRows;
    private final TableData extraRows;

    private final TableData actual;
    private final TableData expected;

    public TableDataComparisonAdditionalResult(TableData actual, TableData expected) {
        this.actual = actual;
        this.expected = expected;

        missingColumns = new TreeSet<>();
        missingRows = new TableData(expected.getHeader());
        extraRows = new TableData(actual.getHeader());
    }

    public boolean areEqual() {
        return missingColumns.isEmpty() &&
            missingRows.isEmpty() &&
            extraRows.isEmpty();
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

    public boolean hasMissingColumns() {
        return !missingColumns.isEmpty();
    }

    public Set<String> getMissingColumns() {
        return missingColumns;
    }

    public boolean hasMissingRows() {
        return !missingRows.isEmpty();
    }

    public TableData getMissingRows() {
        return missingRows;
    }

    public boolean hasExtraRows() {
        return !extraRows.isEmpty();
    }

    public TableData getExtraRows() {
        return extraRows;
    }
}
