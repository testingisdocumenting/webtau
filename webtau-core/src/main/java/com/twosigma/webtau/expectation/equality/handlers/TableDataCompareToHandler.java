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

package com.twosigma.webtau.expectation.equality.handlers;

import com.twosigma.webtau.data.table.TableData;
import com.twosigma.webtau.data.table.comparison.TableDataComparison;
import com.twosigma.webtau.data.table.comparison.TableDataComparisonReport;
import com.twosigma.webtau.data.table.comparison.TableDataComparisonResult;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.CompareToComparator;
import com.twosigma.webtau.expectation.equality.CompareToHandler;

public class TableDataCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof TableData && expected instanceof TableData;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        TableDataComparisonResult result = TableDataComparison.compare((TableData) actual, (TableData) expected);
        if (! result.areEqual()) {
            comparator.reportNotEqual(this, actualPath, new TableDataComparisonReport(result).generate());
        }
    }
}
