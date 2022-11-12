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
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.data.table.comparison.TableDataComparison;
import org.testingisdocumenting.webtau.data.table.comparison.TableDataComparisonReport;
import org.testingisdocumenting.webtau.data.table.comparison.TableDataComparisonResult;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

import java.util.*;
import java.util.stream.Stream;

public class IterableAndTableDataCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof Iterable && expected instanceof TableData;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        TableData expectedTable = (TableData) expected;
        TableData actualTable = createTableFromIterable((Iterable<Object>) actual);

        TableDataComparisonResult result = TableDataComparison.compare(actualTable, expectedTable);
        if (!result.areEqual()) {
            comparator.reportNotEqual(this, actualPath, new TableDataComparisonReport(result).generate());
        }
    }

    private static TableData createTableFromIterable(Iterable<Object> actualList) {
        Iterator<Object> actualIt = actualList.iterator();
        if (!actualIt.hasNext()) {
            return new TableData(Stream.empty());
        }

        List<Map<String, ?>> actualAsListOfMaps = convertActualToListOfMaps(actualList);
        return TableData.fromListOfMaps(actualAsListOfMaps);
    }

    private static List<Map<String, ?>> convertActualToListOfMaps(Iterable<Object> actualIt) {
        List<Map<String, ?>> result = new ArrayList<>();
        for (Object row : actualIt) {
            result.add(ToMapConverters.convert(row));
        }

        return result;
    }
}
