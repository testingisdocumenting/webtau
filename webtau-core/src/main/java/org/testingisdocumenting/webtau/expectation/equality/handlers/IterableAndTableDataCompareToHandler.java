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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.testingisdocumenting.webtau.data.converters.ToMapConverters;
import org.testingisdocumenting.webtau.data.table.header.TableDataHeader;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.data.table.comparison.TableDataComparison;
import org.testingisdocumenting.webtau.data.table.comparison.TableDataComparisonReport;
import org.testingisdocumenting.webtau.data.table.comparison.TableDataComparisonResult;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IterableAndTableDataCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof Iterable && expected instanceof TableData;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        TableData expectedTable = (TableData) expected;
        TableData actualTable = createTableFromList(expectedTable.getHeader(), (List<Object>) actual);

        TableDataComparisonResult result = TableDataComparison.compare(actualTable, expectedTable);
        if (! result.areEqual()) {
            comparator.reportNotEqual(this, actualPath, new TableDataComparisonReport(result).generate());
        }
    }

    private static TableData createTableFromList(TableDataHeader expectedHeader, List<Object> actualList) {
        TableData actualTable = new TableData(expectedHeader.getNamesStream());
        for (Object actualRecord : actualList) {
            Map<String, ?> actualMap = ToMapConverters.convert(actualRecord);
            actualTable.addRow(mapToList(expectedHeader, actualMap));
        }

        return actualTable;
    }

    private static List<Object> mapToList(TableDataHeader header, Map<String, ?> map) {
        List<Object> result = new ArrayList<>();
        header.getNamesStream().forEach(n -> result.add(map.get(n)));

        return result;
    }
}
