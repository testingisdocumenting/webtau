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

import org.testingisdocumenting.webtau.data.traceable.CheckLevel;
import org.testingisdocumenting.webtau.data.traceable.TraceableValue;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator.AssertionMode;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;
import org.testingisdocumenting.webtau.expectation.equality.CompareToResult;

public class TraceableValueCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleNulls() {
        return true;
    }

    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return handles(actual);
    }

    @Override
    public boolean handleGreaterLessEqual(Object actual, Object expected) {
        return handles(actual);
    }

    @Override
    public void compareGreaterLessEqual(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        TraceableValue traceableValue = (TraceableValue) actual;

        CompareToResult result = comparator.compareUsingCompareTo(actualPath, traceableValue.getValue(), expected);
        traceableValue.updateCheckLevel(determineCompareToCheckLevel(result, comparator.getAssertionMode()));
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        TraceableValue traceableValue = (TraceableValue) actual;

        CompareToResult result = comparator.compareUsingEqualOnly(actualPath, traceableValue.getValue(), expected);
        traceableValue.updateCheckLevel(determineEqualOnlyCheckLevel(result, comparator.getAssertionMode()));
    }

    private CheckLevel determineCompareToCheckLevel(CompareToResult result, AssertionMode assertionMode) {
        if (result.isGreater() && assertionMode == AssertionMode.GREATER_THAN ||
                result.isGreaterOrEqual() && assertionMode == AssertionMode.GREATER_THAN_OR_EQUAL ||
                result.isLess() && assertionMode == AssertionMode.LESS_THAN ||
                result.isLessOrEqual() && assertionMode == AssertionMode.LESS_THAN_OR_EQUAL) {
            return CheckLevel.FuzzyPassed;
        }

        if (result.isGreaterOrEqual() && assertionMode == AssertionMode.LESS_THAN ||
                result.isGreater() && assertionMode == AssertionMode.LESS_THAN_OR_EQUAL ||
                result.isLessOrEqual() && assertionMode == AssertionMode.GREATER_THAN ||
                result.isLess() && assertionMode == AssertionMode.GREATER_THAN_OR_EQUAL) {
            return CheckLevel.ExplicitFailed;
        }

        return CheckLevel.None;
    }

    private CheckLevel determineEqualOnlyCheckLevel(CompareToResult result, AssertionMode assertionMode) {
        if (result.isNotEqual() && assertionMode == AssertionMode.EQUAL ||
                result.isEqual() && assertionMode == AssertionMode.NOT_EQUAL) {
            return CheckLevel.ExplicitFailed;
        }

        if (result.isEqual() && assertionMode == AssertionMode.EQUAL) {
            return CheckLevel.ExplicitPassed;
        }

        if (result.isNotEqual() && assertionMode == AssertionMode.NOT_EQUAL) {
            return CheckLevel.FuzzyPassed;
        }

        return CheckLevel.None;
    }

    private boolean handles(Object actual) {
        return actual instanceof TraceableValue;
    }
}
