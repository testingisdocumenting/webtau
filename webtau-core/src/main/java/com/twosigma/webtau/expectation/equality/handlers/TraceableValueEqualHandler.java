package com.twosigma.webtau.expectation.equality.handlers;

import com.twosigma.webtau.data.traceable.CheckLevel;
import com.twosigma.webtau.data.traceable.TraceableValue;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.ComparatorResult;
import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;

public class TraceableValueEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof TraceableValue;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        TraceableValue traceableValue = (TraceableValue) actual;
        ComparatorResult result = equalComparator.compare(actualPath, traceableValue.getValue(), expected);

        if (result.isMismatch()) {
            traceableValue.updateCheckLevel(equalComparator.isNegative() ?
                CheckLevel.FuzzyFailed:
                CheckLevel.ExplicitFailed);
        } else {
            traceableValue.updateCheckLevel(equalComparator.isNegative() ?
                CheckLevel.FuzzyPassed:
                CheckLevel.ExplicitPassed);
        }
    }
}
