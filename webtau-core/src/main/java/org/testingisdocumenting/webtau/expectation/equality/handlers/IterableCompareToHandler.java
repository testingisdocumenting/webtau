/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

import java.util.Iterator;

public class IterableCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof Iterable && expected instanceof Iterable;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        Iterator actualIt = ((Iterable) actual).iterator();
        Iterator expectedIt  = ((Iterable) expected).iterator();

        int idx = 0;
        while (actualIt.hasNext() && expectedIt.hasNext()) {
            Object actualElement = actualIt.next();
            Object expectedElement = expectedIt.next();

            comparator.compareUsingEqualOnly(actualPath.index(idx), actualElement, expectedElement);
            idx++;
        }

        while (actualIt.hasNext()) {
            Object actualElement = actualIt.next();
            comparator.reportExtra(this, actualPath.index(idx), actualElement);
            idx++;
        }

        while (expectedIt.hasNext()) {
            Object expectedElement = expectedIt.next();
            comparator.reportMissing(this, actualPath.index(idx), expectedElement);
            idx++;
        }
    }
}
