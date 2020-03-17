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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapsCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof Map && expected instanceof Map;
    }

    @Override
    public void compareEqualOnly(CompareToComparator compareToComparator, ActualPath actualPath, Object actual, Object expected) {
        Map<?, ?> actualMap = (Map) actual;
        Map<?, ?> expectedMap = (Map) expected;

        Comparator comparator = new Comparator(compareToComparator, actualPath, actualMap, expectedMap);
        comparator.compare();
    }

    private class Comparator {
        private CompareToComparator compareToComparator;
        private ActualPath actualPath;
        private Map<?, ?> actualMap;
        private Map<?, ?> expectedMap;
        private Set<Object> allKeys;

        Comparator(CompareToComparator compareToComparator, ActualPath actualPath, Map<?, ?> actualMap, Map<?, ?> expectedMap) {
            this.compareToComparator = compareToComparator;
            this.actualPath = actualPath;
            this.actualMap = actualMap;
            this.expectedMap = expectedMap;

            allKeys = new HashSet<>(actualMap.keySet());
            allKeys.addAll(expectedMap.keySet());
        }

        void compare() {
            allKeys.forEach(this::handleKey);
        }

        private void handleKey(Object key) {
            ActualPath propertyPath = actualPath.property(key.toString());

            if (! actualMap.containsKey(key)) {
                compareToComparator.reportMissing(MapsCompareToHandler.this, propertyPath, expectedMap.get(key));
            } else if (! expectedMap.containsKey(key)) {
                compareToComparator.reportExtra(MapsCompareToHandler.this, propertyPath, actualMap.get(key));
            } else {
                compareToComparator.compareUsingEqualOnly(propertyPath, actualMap.get(key), expectedMap.get(key));
            }
        }
    }
}
