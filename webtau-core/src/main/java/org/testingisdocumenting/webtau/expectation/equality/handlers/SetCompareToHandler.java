/*
 * Copyright 2023 webtau maintainers
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
import org.testingisdocumenting.webtau.expectation.equality.CompareToResult;

import java.util.*;

public class SetCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof Set && expected instanceof Set;
    }

    @Override
    public void compareEqualOnly(CompareToComparator compareToComparator, ValuePath actualPath, Object actual, Object expected) {
        Set<?> actualSet = (Set<?>) actual;
        Set<?> expectedSet = (Set<?>) expected;

        Comparator comparator = new Comparator(compareToComparator, actualPath, actualSet, expectedSet);
        comparator.compare();
    }

    private class Comparator {
        private final CompareToComparator comparator;
        private final CompareToComparator localComparator;
        private final ValuePath actualPath;
        List<?> actualLeft;
        List<?> expectedLeft;

        Map<Object, Integer> actualIndexes;

        Comparator(CompareToComparator compareToComparator, ValuePath actualPath, Set<?> actual, Set<?> expected) {
            this.comparator = compareToComparator;
            this.localComparator = CompareToComparator.comparator(compareToComparator.getAssertionMode());
            this.actualPath = actualPath;
            this.actualLeft = new ArrayList<>(actual);
            this.expectedLeft = new ArrayList<>(expected);
            this.actualIndexes = buildActualIndexes();
        }

        private Map<Object, Integer> buildActualIndexes() {
            Map<Object, Integer> result = new HashMap<>();
            int idx = 0;
            for (Object actual : actualLeft) {
                result.put(actual, idx);
                idx++;
            }

            return result;
        }

        void compare() {
            expectedLeft.removeIf(this::checkAndRemoveFromActualIfMatch);

            actualLeft.forEach(e -> comparator.reportExtra(SetCompareToHandler.this, actualPath.index(actualIndexes.get(e)), e));
            expectedLeft.forEach(e -> comparator.reportMissing(SetCompareToHandler.this, actualPath, e));
        }

        private boolean checkAndRemoveFromActualIfMatch(Object expected) {
            Iterator<?> it = actualLeft.iterator();
            int idx = 0;
            while (it.hasNext()) {
                Object actual = it.next();

                CompareToResult result = localComparator.compareUsingEqualOnly(actualPath, actual, expected);
                if (result.isEqual()) {
                    comparator.reportEqual(SetCompareToHandler.this, actualPath.index(idx),
                            HandlerMessages.renderActualExpected(comparator.getAssertionMode(), actual, expected));
                    it.remove();
                    return true;
                }

                idx++;
            }

            return false;
        }
    }
}
