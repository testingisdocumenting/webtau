package com.twosigma.webtau.expectation;

import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;
import com.twosigma.webtau.page.ElementValue;
import com.twosigma.webtau.page.PageElement;

import static com.twosigma.webtau.Ddjt.createActualPath;

public class PageElementEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof PageElement;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        PageElement actualPageElement = (PageElement) actual;
        ElementValue elementValue = actualPageElement.elementValue();
        Object actualValue = elementValue.get();

        equalComparator.compare(createActualPath(elementValue.getName()), actualValue, expected);
    }
}
