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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.data.table.comparison.TableDataComparison;
import org.testingisdocumenting.webtau.data.table.comparison.TableDataComparisonReport;
import org.testingisdocumenting.webtau.data.table.comparison.TableDataComparisonAdditionalResult;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

public class TableDataCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof TableData && expected instanceof TableData;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ValuePath actualPath, Object actual, Object expected) {
        TableData actualTableData = (TableData) actual;
        TableData expectedTableData = (TableData) expected;

        TableDataComparisonAdditionalResult result = TableDataComparison.compare(comparator, actualPath, actualTableData, expectedTableData);
        TableDataComparisonReport comparisonReportGenerator = new TableDataComparisonReport(result);

        if (actualTableData.isEmpty() && !expectedTableData.isEmpty()) {
            comparator.reportNotEqual(this, actualPath, "is empty");
        } else {
            if (result.hasMissingColumns()) {
                comparator.reportNotEqual(this, actualPath, comparisonReportGenerator.missingColumnsReport());
            }

            if (result.hasMissingRows()) {
                comparator.reportNotEqual(this, actualPath, comparisonReportGenerator.missingRowsReport());
            }

            if (result.hasExtraRows()) {
                comparator.reportNotEqual(this, actualPath, comparisonReportGenerator.extraRowsReport());
            }
        }
    }
}
