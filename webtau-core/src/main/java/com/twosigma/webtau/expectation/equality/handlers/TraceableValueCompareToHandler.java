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

import com.twosigma.webtau.data.traceable.CheckLevel;
import com.twosigma.webtau.data.traceable.TraceableValue;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.CompareToComparator;
import com.twosigma.webtau.expectation.equality.CompareToHandler;

public class TraceableValueCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof TraceableValue;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        TraceableValue traceableValue = (TraceableValue) actual;

        boolean isEqual = comparator.compareIsEqual(actualPath, traceableValue.getValue(), expected);

        if (! isEqual) {
            traceableValue.updateCheckLevel(comparator.isNegative() ?
                CheckLevel.FuzzyPassed:
                CheckLevel.ExplicitFailed);
        } else {
            traceableValue.updateCheckLevel(comparator.isNegative() ?
                CheckLevel.ExplicitFailed:
                CheckLevel.ExplicitPassed);
        }
    }
}
