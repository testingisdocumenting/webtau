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
import com.twosigma.webtau.expectation.equality.CompareToComparator;
import com.twosigma.webtau.expectation.equality.CompareToHandler;
import com.twosigma.webtau.utils.NumberUtils;

import java.text.ParseException;

import static com.twosigma.webtau.expectation.equality.handlers.HandlerMessages.renderActualExpected;

public class NumberAndStringCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return handles(actual, expected);
    }

    @Override
    public boolean handleGreaterLessEqual(Object actual, Object expected) {
        return handles(actual, expected);
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        Number actualNumber = convertToNumber(actual);

        if (actualNumber == null) {
            comparator.reportEqualOrNotEqual(this, false, actualPath, renderActualExpected(Double.NaN, expected));
        } else {
            comparator.compareUsingEqualOnly(actualPath, actualNumber, expected);
        }
    }

    @Override
    public void compareGreaterLessEqual(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        Number actualNumber = convertToNumber(actual);

        if (actualNumber == null) {
            comparator.reportCompareToValue(this, compareToValueToFail(comparator), actualPath,
                    renderActualExpected(Double.NaN, expected));
        } else {
            comparator.compareUsingCompareTo(actualPath, actualNumber, expected);
        }
    }

    private int compareToValueToFail(CompareToComparator comparator) {
        switch (comparator.getAssertionMode()) {
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUAL:
                return -1;
            case LESS_THAN:
            case LESS_THAN_OR_EQUAL:
                return 1;
            case EQUAL:
                return 1;
            case NOT_EQUAL:
                return 0;
        }

        return 0;
    }

    private Number convertToNumber(Object actual) {
        try {
            return NumberUtils.convertStringToNumber((String) actual);
        } catch (ParseException e) {
            return null;
        }
    }

    private boolean handles(Object actual, Object expected) {
        return actual instanceof String &&
                expected instanceof Number;
    }
}
