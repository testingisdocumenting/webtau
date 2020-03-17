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
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator.AssertionMode;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

import static org.testingisdocumenting.webtau.expectation.equality.handlers.HandlerMessages.expected;
import static org.testingisdocumenting.webtau.utils.TraceUtils.renderValueAndType;

public class NullCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return eitherIsNull(actual, expected);
    }

    @Override
    public boolean handleGreaterLessEqual(Object actual, Object expected) {
        return eitherIsNull(actual, expected);
    }

    @Override
    public boolean handleNulls() {
        return true;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        if (actual == null && expected == null) {
            comparator.reportEqual(this, actualPath,
                    "  actual: null\n" + expected(comparator.getAssertionMode(), null));
        } else if (actual == null) {
            comparator.reportNotEqual(this, actualPath,
                    "  actual: null\n" + expected(comparator.getAssertionMode(), renderValueAndType(expected)));
        } else {
            comparator.reportNotEqual(this, actualPath,
                    "  actual: " + renderValueAndType(actual) + "\n" + expected(comparator.getAssertionMode(), null));
        }
    }

    @Override
    public void compareGreaterLessEqual(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        if (actual == null && expected == null && checksEquality(comparator)) {
            comparator.reportEqual(this, actualPath,
                    "  actual: null\n" + expected(comparator.getAssertionMode(), null));
        } else if (actual == null) {
            String message = "  actual: null\n" + expected(comparator.getAssertionMode(), renderValueAndType(expected));
            generateOppositeReport(comparator, actualPath, message);
        } else {
            String message = "  actual: " + renderValueAndType(actual) + "\n" + expected(comparator.getAssertionMode(),
                    null);
            generateOppositeReport(comparator, actualPath, message);
        }
    }

    private void generateOppositeReport(CompareToComparator comparator, ActualPath actualPath, String message) {
        switch (comparator.getAssertionMode()) {
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUAL:
                comparator.reportLess(this, actualPath, message);
                break;
            case LESS_THAN:
            case LESS_THAN_OR_EQUAL:
                comparator.reportGreater(this, actualPath, message);
                break;
        }
    }

    private boolean checksEquality(CompareToComparator comparator) {
        return comparator.getAssertionMode() == AssertionMode.EQUAL ||
                comparator.getAssertionMode() == AssertionMode.NOT_EQUAL ||
                comparator.getAssertionMode() == AssertionMode.LESS_THAN_OR_EQUAL ||
                comparator.getAssertionMode() == AssertionMode.GREATER_THAN_OR_EQUAL;
    }

    private boolean eitherIsNull(Object actual, Object expected) {
        return actual == null || expected == null;
    }
}
