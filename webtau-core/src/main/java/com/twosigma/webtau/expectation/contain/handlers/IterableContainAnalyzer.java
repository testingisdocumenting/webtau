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

package com.twosigma.webtau.expectation.contain.handlers;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.CompareToComparator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IterableContainAnalyzer {
    private ActualPath actualPath;
    private Object actual;
    private Object expected;
    private CompareToComparator comparator;

    public IterableContainAnalyzer(ActualPath actualPath, Object actual, Object expected) {
        this.actualPath = actualPath;
        this.actual = actual;
        this.expected = expected;
        this.comparator = CompareToComparator.comparator();
    }

    public List<IndexedValue> containingIndexedValues() {
        List<IndexedValue> matchedIndexes = new ArrayList<>();

        Iterable actualIterable = (Iterable) actual;
        Iterator iterator = actualIterable.iterator();

        int idx = 0;
        while (iterator.hasNext()) {
            Object actualValue = iterator.next();
            ActualPath indexedPath = actualPath.index(idx);

            boolean isEqual = comparator.compareIsEqual(indexedPath, actualValue, expected);
            if (isEqual) {
                matchedIndexes.add(new IndexedValue(idx, actualValue));
            }

            idx++;
        }

        return matchedIndexes;
    }

    public CompareToComparator getComparator() {
        return comparator;
    }
}
