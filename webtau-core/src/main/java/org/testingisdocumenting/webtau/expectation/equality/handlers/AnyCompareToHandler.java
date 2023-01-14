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

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

import static org.testingisdocumenting.webtau.expectation.equality.handlers.HandlerMessages.renderActualExpected;

public class AnyCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return true;
    }

    @Override
    public boolean handleGreaterLessEqual(Object actual, Object expected) {
        return actual instanceof Comparable && expected instanceof Comparable;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ValuePath actualPath, Object actual, Object expected) {
        boolean isEqual = actual.equals(expected);
        String message = renderActualExpected(comparator.getAssertionMode(), actual, expected);
        comparator.reportEqualOrNotEqual(this, isEqual, actualPath, message);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void compareGreaterLessEqual(CompareToComparator comparator, ValuePath actualPath, Object actual, Object expected) {
        Comparable<Object> actualComparable = (Comparable<Object>) actual;
        int compareTo = actualComparable.compareTo(expected);

        String message = renderActualExpected(comparator.getAssertionMode(), actual, expected);
        comparator.reportCompareToValue(this, compareTo, actualPath, message);
    }
}
