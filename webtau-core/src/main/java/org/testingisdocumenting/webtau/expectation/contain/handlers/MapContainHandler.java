/*
 * Copyright 2022 webtau maintainers
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

import org.testingisdocumenting.webtau.data.MapWithTrackedMissingKeys;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.contain.ContainAnalyzer;
import org.testingisdocumenting.webtau.expectation.contain.ContainHandler;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;

import java.util.Map;

public class MapContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof Map && expected instanceof Map;
    }

    @Override
    public Object convertedActual(Object actual, Object expected) {
        return new MapWithTrackedMissingKeys((Map<?, ?>) actual);
    }

    @Override
    public void analyzeContain(ContainAnalyzer containAnalyzer, ValuePath actualPath, Object actual, Object expected) {
        analyzeMapAndMap(containAnalyzer, actualPath, actual, expected, false);
    }

    @Override
    public void analyzeNotContain(ContainAnalyzer containAnalyzer, ValuePath actualPath, Object actual, Object expected) {
        analyzeMapAndMap(containAnalyzer, actualPath, actual, expected, true);
    }

    private void analyzeMapAndMap(ContainAnalyzer containAnalyzer, ValuePath actualPath,
                                  Object actual, Object expected,
                                  boolean isNegative) {
        Map<?, ?> actualMap = (Map<?, ?>) actual;
        Map<?, ?> expectedMap = (Map<?, ?>) expected;

        for (Map.Entry<?, ?> expectedEntry : expectedMap.entrySet()) {
            Object expectedKey = expectedEntry.getKey();

            ValuePath propertyPath = actualPath.property(expectedKey.toString());
            analyzeMapAndMapSingleExpectedEntry(containAnalyzer, actualMap, propertyPath, expectedEntry, isNegative);
        }
    }

    private void analyzeMapAndMapSingleExpectedEntry(ContainAnalyzer containAnalyzer,
                                                     Map<?, ?> actualMap,
                                                     ValuePath propertyPath,
                                                     Map.Entry<?, ?> expectedEntry,
                                                     boolean isNegative) {
        if (!actualMap.containsKey(expectedEntry.getKey())) {
            containAnalyzer.reportMissing(this, propertyPath, expectedEntry.getValue());
        } else {
            CompareToComparator comparator = CompareToComparator.comparator();

            Object actualValue = actualMap.get(expectedEntry.getKey());
            boolean actualValueEqual = isNegative ?
                    !comparator.compareIsNotEqual(propertyPath, actualValue, expectedEntry.getValue()):
                    comparator.compareIsEqual(propertyPath, actualValue, expectedEntry.getValue());

            containAnalyzer.registerConvertedActualByPath(comparator.getConvertedActualByPath());
            if (!actualValueEqual) {
                if (comparator.getMissingMessages() != null) {
                    comparator.getMissingMessages().forEach(m -> containAnalyzer.reportMissing(this, m));
                }

                if (comparator.getNotEqualMessages() != null) {
                    comparator.getNotEqualMessages().forEach(m -> containAnalyzer.reportMismatch(this, m));
                }
            } else {
                containAnalyzer.reportMatch(this, propertyPath, comparator::generateEqualMatchReport);
            }
        }
    }
}
