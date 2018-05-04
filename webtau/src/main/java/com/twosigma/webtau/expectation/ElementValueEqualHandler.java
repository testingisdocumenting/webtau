package com.twosigma.webtau.expectation;

import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;
import com.twosigma.webtau.page.ElementValue;

import static com.twosigma.webtau.Ddjt.createActualPath;

public class ElementValueEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof ElementValue;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        ElementValue actualElementValue = (ElementValue) actual;
        Object actualValue = actualElementValue.get();

        equalComparator.compare(createActualPath(actualElementValue.getName()), actualValue, expected);
    }
}
