/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.db;

import org.apache.commons.lang3.StringUtils;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

import java.util.Map;
import java.util.function.Function;

public class DatabaseCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof DbQuery ||
                actual instanceof DatabaseTable;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ValuePath actualPath, Object actual, Object expected) {
        comparator.compareUsingEqualOnly(actualPath, actual, expected);
    }

    @Override
    public Object convertedActual(Object actual, Object expected) {
        Function<String, String> actualHeaderConverter = headerConverterBasedOnExpected(expected);

        if (actual instanceof DatabaseTable) {
            return ((DatabaseTable) actual).query().queryTableDataNoStep(actualHeaderConverter);
        }

        DbQuery actualResult = (DbQuery) actual;
        TableData tableData = actualResult.queryTableDataNoStep(actualHeaderConverter);
        if (actualResult.isSingleValue(tableData)) {
            return actualResult.getUnderlyingSingleValue(tableData);
        }

        if (expected instanceof Map && tableData.numberOfRows() == 1) {
            return tableData.row(0);
        }

        return tableData;
    }

    private Function<String, String> headerConverterBasedOnExpected(Object expected) {
        if (!(expected instanceof TableData)) {
            return TableHeaderConverters::toUpperCase;
        }

        TableData tableData = (TableData) expected;
        if (tableData.getHeader().getNamesStream().allMatch(StringUtils::isAllUpperCase)) {
            return TableHeaderConverters::unmodified;
        }

        return tableData.getHeader().getNamesStream().anyMatch(name -> name.contains("_")) ?
                TableHeaderConverters::toUpperCase :
                TableHeaderConverters::underscoreToCamelCase;
    }
}
