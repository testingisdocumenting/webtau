package com.twosigma.webtau.expectation.equality;

import com.twosigma.webtau.expectation.ActualPath;

public interface EqualComparatorHandler {
    boolean handle(Object actual, Object expected);

    default boolean handleNulls() {
        return false;
    }

    void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected);
}
