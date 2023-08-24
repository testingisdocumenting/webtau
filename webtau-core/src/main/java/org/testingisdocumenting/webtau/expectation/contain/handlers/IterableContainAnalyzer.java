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

package org.testingisdocumenting.webtau.expectation.contain.handlers;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToResult;
import org.testingisdocumenting.webtau.expectation.equality.ValuePathMessage;

import java.util.*;

public class IterableContainAnalyzer {
    private final ValuePath actualPath;
    private final Object actual;
    private final Object expected;
    private final CompareToComparator comparator;
    private final List<List<ValuePathMessage>> mismatchMessagesPerIdx;

    public IterableContainAnalyzer(ValuePath actualPath, Object actual, Object expected, boolean isNegative) {
        this.actualPath = actualPath;
        this.actual = actual;
        this.expected = expected;
        this.mismatchMessagesPerIdx = new ArrayList<>();
        this.comparator = CompareToComparator.comparator(isNegative ? CompareToComparator.AssertionMode.NOT_EQUAL : CompareToComparator.AssertionMode.EQUAL);
    }

    public List<IndexedValue> findContainingIndexedValues() {
        List<IndexedValue> matchedIndexes = new ArrayList<>();

        Iterable<?> actualIterable = (Iterable<?>) actual;
        Iterator<?> iterator = actualIterable.iterator();

        int idx = 0;
        while (iterator.hasNext()) {
            Object actualValue = iterator.next();
            ValuePath indexedPath = actualPath.index(idx);

            CompareToResult compareToResult = comparator.compareUsingEqualOnly(indexedPath, actualValue, expected);
            boolean isEqual = compareToResult.isEqual();
            if (isEqual) {
                matchedIndexes.add(new IndexedValue(idx, actualValue));
            } else {
                mismatchMessagesPerIdx.add(compareToResult.getNotEqualMessages());
            }

            idx++;
        }

        return matchedIndexes;
    }

    public List<List<ValuePathMessage>> getMismatchMessagesPerIdx() {
        return mismatchMessagesPerIdx;
    }

    public CompareToComparator getComparator() {
        return comparator;
    }
}
