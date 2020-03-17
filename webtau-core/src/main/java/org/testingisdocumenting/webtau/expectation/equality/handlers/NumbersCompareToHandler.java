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

package com.twosigma.webtau.expectation.equality.handlers;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.CompareToComparator;
import com.twosigma.webtau.expectation.equality.CompareToHandler;

import java.math.BigDecimal;
import java.util.Scanner;

public class NumbersCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return areNumbersOfDiffType(actual, expected);
    }

    @Override
    public boolean handleGreaterLessEqual(Object actual, Object expected) {
        return areNumbersOfDiffType(actual, expected);
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        ConvertedAndOriginal convertedAndOriginal = new ConvertedAndOriginal(comparator.getAssertionMode(), actual, expected);

        boolean isEqual = convertedAndOriginal.compareTo() == 0;
        comparator.reportEqualOrNotEqual(this, isEqual,
                actualPath, convertedAndOriginal.renderActualExpected());
    }

    @Override
    public void compareGreaterLessEqual(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        ConvertedAndOriginal convertedAndOriginal = new ConvertedAndOriginal(comparator.getAssertionMode(), actual, expected);

        comparator.reportCompareToValue(this, convertedAndOriginal.compareTo(),
                actualPath, convertedAndOriginal.renderActualExpected());
    }

    private boolean areNumbersOfDiffType(Object actual, Object expected) {
        return actual instanceof Number && expected instanceof Number && (actual.getClass() != expected.getClass());
    }

    private static class ConvertedAndOriginal {
        CompareToComparator.AssertionMode assertionMode;
        Object actual;
        Object expected;
        Comparable convertedActual;
        Comparable convertedExpected;

        ConvertedAndOriginal(CompareToComparator.AssertionMode assertionMode, Object actual, Object expected) {
            this.assertionMode = assertionMode;
            this.actual = actual;
            this.expected = expected;

            Class largest = largest(actual, expected);
            this.convertedActual = convertTo(largest, actual);
            this.convertedExpected = convertTo(largest, expected);
        }

        @SuppressWarnings("unchecked")
        int compareTo() {
            return convertedActual.compareTo(convertedExpected);
        }

        String renderActualExpected() {
            return HandlerMessages.renderActualExpected(assertionMode, convertedActual, convertedExpected, actual, expected);
        }

        private static Class largest(Object actual, Object expected) {
            if (isOfType(BigDecimal.class, actual, expected)) {
                return BigDecimal.class;
            }

            if (isOfType(Double.class, actual, expected)) {
                return Double.class;
            }

            if (isOfType(Long.class, actual, expected)) {
                return Long.class;
            }

            return actual.getClass();
        }

        private static Comparable convertTo(Class to, Object original) {
            Scanner scanner = new Scanner(original.toString());

            if (to == BigDecimal.class) {
                return scanner.nextBigDecimal();
            }

            if (to == Double.class) {
                return scanner.nextDouble();
            }

            if (to == Long.class) {
                return scanner.nextLong();
            }

            if (to == Integer.class) {
                return scanner.nextInt();
            }

            return (Comparable) original;
        }

        private static boolean isOfType(Class type, Object actual, Object expected) {
            return actual.getClass().isAssignableFrom(type) || expected.getClass().isAssignableFrom(expected.getClass());
        }
    }
}
