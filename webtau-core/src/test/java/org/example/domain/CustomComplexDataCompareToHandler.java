/*
 * Copyright 2023 webtau maintainers
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

package org.example.domain;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.data.table.header.TableDataHeader;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

public class CustomComplexDataCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof CustomComplexData && ( // handler for actual as your data
                expected instanceof CustomComplexData || // and expected as either your data or TableData
                        expected instanceof TableData);
    }

    @Override
    public Object convertedActual(Object actual, Object expected) {
        return createTableDataFromCustomData((CustomComplexData) actual);
    }

    @Override
    public Object convertedExpected(Object actual, Object expected) {
        if (expected instanceof TableData) {
            return expected;
        }

        return createTableDataFromCustomData((CustomComplexData) expected);
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ValuePath actualPath, Object actual, Object expected) {
        comparator.compareUsingEqualOnly(actualPath, actual, expected); // delegate back to WebTau to compare using converted types
    }

    private TableData createTableDataFromCustomData(CustomComplexData actual) {
        TableData tableData = new TableData(new TableDataHeader(actual.getColumnNames().stream()));
        actual.forEach(tableData::addRow); // create TableData from complex domain data

        return tableData;
    }
}
