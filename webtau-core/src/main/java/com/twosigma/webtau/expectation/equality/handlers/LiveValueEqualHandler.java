package com.twosigma.webtau.expectation.equality.handlers;

import com.twosigma.webtau.data.live.LiveValue;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;

public class LiveValueEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof LiveValue;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        LiveValue actualLiveValue = (LiveValue) actual;
        equalComparator.compare(actualPath, actualLiveValue.get(), expected);
    }
}
