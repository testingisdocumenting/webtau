package com.twosigma.webtau.expectation.equality.handlers;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;

import static com.twosigma.webtau.utils.TraceUtils.renderValueAndType;

public class AnyEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return true;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        boolean areEqual = actual.equals(expected);
        if (areEqual) {
            return;
        }

        equalComparator.reportMismatch(this, actualPath, mismatchMessage(actual, expected));
    }

    private String mismatchMessage(Object actual, Object expected) {
        return "  actual: " + renderValueAndType(actual) + "\n" +
                "expected: " + renderValueAndType(expected);
    }
}
