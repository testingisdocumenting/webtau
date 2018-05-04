package com.twosigma.webtau.expectation.equality.handlers;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;

import java.util.regex.Pattern;

public class RegexpEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof String && expected instanceof Pattern;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        Pattern expectedPattern = (Pattern) expected;

        boolean areEqual = expectedPattern.matcher(actual.toString()).find();
        if (areEqual) {
            return;
        }

        equalComparator.reportMismatch(this, actualPath, mismatchMessage(actual, expected));
    }

    private String mismatchMessage(Object actual, Object expected) {
        return "   actual string: " + actual.toString() + "\n" +
               "expected pattern: " + expected.toString();
    }
}
