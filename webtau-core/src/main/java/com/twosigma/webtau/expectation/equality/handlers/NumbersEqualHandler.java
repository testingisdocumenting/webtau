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

package com.twosigma.webtau.expectation.equality.handlers;

import java.math.BigDecimal;
import java.util.Scanner;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;

import static com.twosigma.webtau.utils.TraceUtils.renderValueAndType;

public class NumbersEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof Number && expected instanceof Number && (actual.getClass() != expected.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual,
        Object expected) {

        Class largest = largest(actual, expected);

        Comparable convertedActual = convertTo(largest, actual);
        Comparable convertedExpected = convertTo(largest, expected);

        if (convertedActual.compareTo(convertedExpected) != 0) {
            equalComparator.reportMismatch(this, actualPath, mismatchMessage(actualPath, actual, convertedActual, expected, convertedExpected));
        }
    }

    private String mismatchMessage(ActualPath actualPath, Object actual, Object convertedActual, Object expected, Object convertedExpected) {
        return "  actual: " + renderValueAndType(convertedActual) + "(before conversion: " + renderValueAndType(actual) + ")\n" +
               "expected: " + renderValueAndType(convertedExpected) + "(before conversion: " + renderValueAndType(expected) + ")";
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

    private static boolean isOfType(Class type, Object actual, Object expected) {
        return actual.getClass().isAssignableFrom(type) || expected.getClass().isAssignableFrom(expected.getClass());
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
}
