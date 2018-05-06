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
import com.twosigma.webtau.expectation.equality.ComparatorResult;
import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;

public class TraceableValueEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof TraceableValue;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        TraceableValue traceableValue = (TraceableValue) actual;
        ComparatorResult result = equalComparator.compare(actualPath, traceableValue.getValue(), expected);

        if (result.isMismatch()) {
            traceableValue.updateCheckLevel(equalComparator.isNegative() ?
                CheckLevel.FuzzyPassed:
                CheckLevel.ExplicitFailed);
        } else {
            traceableValue.updateCheckLevel(equalComparator.isNegative() ?
                CheckLevel.ExplicitFailed:
                CheckLevel.ExplicitPassed);
        }
    }
}
