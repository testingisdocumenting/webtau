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
import com.twosigma.webtau.expectation.ValueMatcher;
import com.twosigma.webtau.expectation.equality.CompareToComparator;
import com.twosigma.webtau.expectation.equality.CompareToComparator.AssertionMode;
import com.twosigma.webtau.expectation.equality.CompareToHandler;

public class ValueMatcherCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return expected instanceof ValueMatcher;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        ValueMatcher expectedMatcher = (ValueMatcher) expected;

        if (comparator.getAssertionMode() == AssertionMode.EQUAL) {
            handleMatcher(comparator, actualPath, actual, expectedMatcher);
        } else {
            handleNegativeMatcher(comparator, actualPath, actual, expectedMatcher);
        }
    }

    private void handleMatcher(CompareToComparator comparator, ActualPath actualPath, Object actual, ValueMatcher expectedMatcher) {
        boolean matches = expectedMatcher.matches(actualPath, actual);
        if (matches) {
            comparator.reportEqual(this, actualPath, expectedMatcher.matchedMessage(actualPath, actual));
        } else {
            comparator.reportNotEqual(this, actualPath, expectedMatcher.mismatchedMessage(actualPath, actual));
        }
    }

    private void handleNegativeMatcher(CompareToComparator comparator, ActualPath actualPath, Object actual, ValueMatcher expectedMatcher) {
        boolean matches = expectedMatcher.negativeMatches(actualPath, actual);
        if (matches) {
            comparator.reportNotEqual(this, actualPath, expectedMatcher.negativeMatchedMessage(actualPath, actual));
        } else {
            comparator.reportEqual(this, actualPath, expectedMatcher.negativeMismatchedMessage(actualPath, actual));
        }
    }
}
