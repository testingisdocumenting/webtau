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

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.contain.ContainAnalyzer;
import org.testingisdocumenting.webtau.expectation.contain.ContainHandler;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;

import java.util.Map;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class MapContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof Map && expected instanceof Map;
    }

    @Override
    public void analyzeContain(ContainAnalyzer containAnalyzer, ValuePath actualPath, Object actual, Object expected) {
        analyze(containAnalyzer, actualPath, actual, expected, false);
    }

    @Override
    public void analyzeNotContain(ContainAnalyzer containAnalyzer, ValuePath actualPath, Object actual, Object expected) {
        analyze(containAnalyzer, actualPath, actual, expected, true);
    }

    private void analyze(ContainAnalyzer containAnalyzer, ValuePath actualPath,
                         Object actual, Object expected,
                         boolean isNegative) {
        Map<?, ?> actualMap = (Map<?, ?>) actual;
        Map<?, ?> expectedMap = (Map<?, ?>) expected;

        for (Map.Entry<?, ?> expectedEntry : expectedMap.entrySet()) {
            Object expectedKey = expectedEntry.getKey();

            ValuePath propertyPath = actualPath.property(expectedKey.toString());

            if (isNegative) {
                analyzeNegative(containAnalyzer, actualMap, propertyPath, expectedEntry);
            } else {
                analyzePositive(containAnalyzer, actualMap, propertyPath, expectedEntry);
            }
        }
    }

    private void analyzePositive(ContainAnalyzer containAnalyzer,
                                 Map<?, ?> actualMap,
                                 ValuePath propertyPath,
                                 Map.Entry<?, ?> expectedEntry) {
        if (!actualMap.containsKey(expectedEntry.getKey())) {
            containAnalyzer.reportMismatch(this, propertyPath, tokenizedMessage().matcher("is missing"));
        } else {
            CompareToComparator comparator = CompareToComparator.comparator();

            Object actualValue = actualMap.get(expectedEntry.getKey());
            boolean actualValueEqual = comparator.compareIsEqual(propertyPath,
                    actualValue, expectedEntry.getValue());

            if (!actualValueEqual) {
                containAnalyzer.reportMismatch(this, propertyPath, comparator.generateEqualMismatchReport());
            }
        }
    }

    private void analyzeNegative(ContainAnalyzer containAnalyzer,
                                 Map<?, ?> actualMap,
                                 ValuePath propertyPath,
                                 Map.Entry<?, ?> expectedEntry) {
        if (actualMap.containsKey(expectedEntry.getKey())) {
            CompareToComparator comparator = CompareToComparator.comparator();

            Object actualValue = actualMap.get(expectedEntry.getKey());
            boolean actualValueNotEqual = comparator.compareIsNotEqual(propertyPath,
                    actualValue, expectedEntry.getValue());

            if (!actualValueNotEqual) {
                containAnalyzer.reportMismatch(this, propertyPath, comparator.generateNotEqualMismatchReport());
            }
        }
    }
}
