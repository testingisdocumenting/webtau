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

import com.twosigma.webtau.expectation.equality.CompareToComparator;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.twosigma.webtau.utils.TraceUtils.renderValueAndType;

class HandlerMessages {
    private static final String ACTUAL_PREFIX = "  actual: ";
    private static final String EXPECTED_PREFIX = "expected: ";

    static String renderActualExpected(CompareToComparator.AssertionMode assertionMode, Object actual, Object expected) {
        return ACTUAL_PREFIX + renderValueAndType(actual) + "\n" +
                expected(assertionMode, renderValueAndType(expected));
    }

    static String renderActualExpected(CompareToComparator.AssertionMode assertionMode,
                                       Object convertedActual, Object convertedExpected,
                                       Object actual, Object expected) {
        return ACTUAL_PREFIX + renderValueAndType(convertedActual) +
                "(before conversion: " + renderValueAndType(actual) + ")\n" +
                expected(assertionMode, renderValueAndType(convertedExpected) +
                "(before conversion: " + renderValueAndType(expected) + ")");
    }

    static String expected(CompareToComparator.AssertionMode assertionMode, Object expected) {
        return expected(EXPECTED_PREFIX, assertionMode, expected);
    }

    static String expected(String prefix, CompareToComparator.AssertionMode assertionMode, Object expected) {
        return Stream.of(prefix, assertionMode.getMessage(), expected)
                .map(Objects::toString)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(" "))
                + '\n';
    }
}
