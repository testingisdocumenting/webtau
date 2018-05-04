package com.twosigma.webtau.expectation.equality.handlers;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;

import static com.twosigma.webtau.utils.TraceUtils.renderValueAndType;

public class NullEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual == null || expected == null;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        if (actual == null && expected == null) {
            return;
        }

        if (actual == null) {
            equalComparator.reportMismatch(this, actualPath,
                    "  actual: null\n" +
                              "expected: " + renderValueAndType(expected));
        } else {
            equalComparator.reportMismatch(this, actualPath, "  actual: " + renderValueAndType(actual) + "\n" +
                "expected: null\n");
        }
    }
}
