package com.twosigma.webtau.expectation.equality.handlers;

import java.util.Iterator;

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;

public class IterableEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof Iterable && expected instanceof Iterable;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        Iterator actualIt = ((Iterable) actual).iterator();
        Iterator expectedIt  = ((Iterable) expected).iterator();

        int idx = 0;
        while (actualIt.hasNext() && expectedIt.hasNext()) {
            Object actualElement = actualIt.next();
            Object expectedElement = expectedIt.next();

            equalComparator.compare(actualPath.index(idx), actualElement, expectedElement);
            idx++;
        }

        while (actualIt.hasNext()) {
            Object actualElement = actualIt.next();
            equalComparator.reportMismatch(this, actualPath, "extra element " + actualPath.index(idx).getPath() + ": " + DataRenderers.render(actualElement));
            idx++;
        }

        while (expectedIt.hasNext()) {
            Object expectedElement = expectedIt.next();
            equalComparator.reportMismatch(this, actualPath, "missing element " + actualPath.index(idx).getPath() + ": " + DataRenderers.render(expectedElement));
            idx++;
        }
    }
}
