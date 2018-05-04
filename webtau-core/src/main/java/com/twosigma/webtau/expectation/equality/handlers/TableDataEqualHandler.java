package com.twosigma.webtau.expectation.equality.handlers;

import com.twosigma.webtau.data.table.TableData;
import com.twosigma.webtau.data.table.comparison.TableDataComparison;
import com.twosigma.webtau.data.table.comparison.TableDataComparisonReport;
import com.twosigma.webtau.data.table.comparison.TableDataComparisonResult;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;

public class TableDataEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof TableData && expected instanceof TableData;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        TableDataComparisonResult result = TableDataComparison.compare((TableData) actual, (TableData) expected);
        if (! result.areEqual()) {
            equalComparator.reportMismatch(this, actualPath, new TableDataComparisonReport(result).generate());
        }
    }
}
