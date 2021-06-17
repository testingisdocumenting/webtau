/*
 * Copyright 2021 webtau maintainers
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
import org.testingisdocumenting.webtau.utils.NumberUtils;
import org.testingisdocumenting.webtau.utils.TypeUtils;

import java.text.ParseException;

import static org.testingisdocumenting.webtau.expectation.equality.handlers.HandlerMessages.renderActualExpected;

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
            comparator.reportEqualOrNotEqual(this, false, actualPath, renderActualExpected(comparator.getAssertionMode(), Double.NaN, expected));
        } else {
            comparator.compareUsingEqualOnly(actualPath, actualNumber, expected);
        }
    }

    @Override
    public void compareGreaterLessEqual(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        Number actualNumber = convertToNumber(actual);

        if (actualNumber == null) {
            comparator.reportCompareToValue(this, compareToValueToFail(comparator), actualPath,
                    renderActualExpected(comparator.getAssertionMode(), Double.NaN, expected));
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
            case EQUAL:
                return 1;
            case NOT_EQUAL:
                return 0;
        }

        return 0;
    }

    private Number convertToNumber(Object actual) {
        try {
            return NumberUtils.convertStringToNumber((CharSequence) actual);
        } catch (ParseException e) {
            return null;
        }
    }

    private boolean handles(Object actual, Object expected) {
        return TypeUtils.isString(actual) &&
                expected instanceof Number;
    }
}
