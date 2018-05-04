/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
