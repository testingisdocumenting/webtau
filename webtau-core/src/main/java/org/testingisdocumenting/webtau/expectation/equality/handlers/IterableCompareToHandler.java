/*
 * Copyright 2020 webtau maintainers
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

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IterableCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof Iterable && expected instanceof Iterable;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ValuePath actualPath, Object actual, Object expected) {
        Iterator<?> actualIt = ((Iterable<?>) actual).iterator();
        Iterator<?> expectedIt  = ((Iterable<?>) expected).iterator();

        int idx = 0;
        while (actualIt.hasNext() && expectedIt.hasNext()) {
            Object actualElement = actualIt.next();
            Object expectedElement = expectedIt.next();

            comparator.compareUsingEqualOnly(actualPath.index(idx), actualElement, expectedElement);
            idx++;
        }

        List<Object> extraValues = new ArrayList<>();
        while (actualIt.hasNext()) {
            Object actualElement = actualIt.next();
            extraValues.add(actualElement);
        }

        if (!extraValues.isEmpty()) {
            comparator.reportExtra(this, actualPath, extraValues);
        }

        List<Object> missingValues = new ArrayList<>();
        while (expectedIt.hasNext()) {
            Object expectedElement = expectedIt.next();
            missingValues.add(expectedElement);
        }

        if (!missingValues.isEmpty()) {
            comparator.reportMissing(this, actualPath, missingValues);
        }
    }
}
