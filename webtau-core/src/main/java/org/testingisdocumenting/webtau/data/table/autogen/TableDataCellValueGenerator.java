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

package org.testingisdocumenting.webtau.data.table.autogen;

import org.testingisdocumenting.webtau.data.math.DynamicMath;
import org.testingisdocumenting.webtau.data.table.Record;

public class TableDataCellValueGenerator<R> {
    private TableDataCellValueGenFullFunction<R> genFunction;
    private TableDataCellValueGenOnlyRecordFunction<R> genOnlyRecordFunction;

    public TableDataCellValueGenerator(TableDataCellValueGenFullFunction<R> genFunction) {
        this.genFunction = genFunction;
    }

    public TableDataCellValueGenerator(TableDataCellValueGenOnlyRecordFunction<R> genOnlyRecordFunction) {
        this.genOnlyRecordFunction = genOnlyRecordFunction;
    }

    public Object generate(Record row, Record prev, int rowIdx, int colIdx, String columnName) {
        if (genOnlyRecordFunction != null) {
            return genOnlyRecordFunction.apply(row);
        } else {
            return genFunction.apply(row, prev, rowIdx, colIdx, columnName);
        }
    }

    @SuppressWarnings("unchecked")
    public <N> TableDataCellValueGenerator<N> plus(Object right) {
        return new TableDataCellValueGenerator<>(((row, prev, rowIdx, colIdx, columnName) -> {
            R calculated = genFunction.apply(row, prev, rowIdx, colIdx, columnName);
            return (N) DynamicMath.add(calculated, right);
        }));
    }

    public <N> TableDataCellValueGenerator<N> minus(Object right) {
        return new TableDataCellValueGenerator<>(((row, prev, rowIdx, colIdx, columnName) -> {
            R calculated = genFunction.apply(row, prev, rowIdx, colIdx, columnName);
            return (N) DynamicMath.subtract(calculated, right);
        }));
    }
}
