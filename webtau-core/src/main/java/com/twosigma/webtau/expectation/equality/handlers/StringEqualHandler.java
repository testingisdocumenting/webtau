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

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;

import static com.twosigma.webtau.utils.TraceUtils.renderValueAndType;

public class StringEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return (actual instanceof Character || actual instanceof String) &&
                (expected instanceof Character || expected instanceof String);
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        String actualString = convertToString(actual);
        String expectedString = convertToString(expected);

        boolean areEqual = actualString.equals(expectedString);
        if (areEqual) {
            return;
        }

        equalComparator.reportMismatch(this, actualPath, mismatchMessage(actual, actualString, expected, expectedString));
    }

    private String convertToString(Object expected) {
        return expected.toString();
    }

    private String mismatchMessage(Object actual, Object convertedActual, Object expected, Object convertedExpected) {
        return "  actual: " + renderValueAndType(convertedActual) + additionalTypeInfo(actual, convertedActual) + "\n" +
                "expected: " + renderValueAndType(convertedExpected) + additionalTypeInfo(expected, convertedExpected);
    }

    private String additionalTypeInfo(Object original, Object converted) {
        return (original.getClass() != converted.getClass() ? "(before conversion: " + renderValueAndType(original) + ")" : "");
    }
}
