/*
 * Copyright 2023 webtau maintainers
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

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator.AssertionMode;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

public class ValueMatcherCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return expected instanceof ValueMatcher;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ValuePath actualPath, Object actual, Object expected) {
        ValueMatcher expectedMatcher = (ValueMatcher) expected;

        if (comparator.getAssertionMode() == AssertionMode.EQUAL) {
            handleMatcher(comparator, actualPath, actual, expectedMatcher);
        } else {
            handleNegativeMatcher(comparator, actualPath, actual, expectedMatcher);
        }
    }

    private void handleMatcher(CompareToComparator comparator, ValuePath actualPath, Object actual, ValueMatcher expectedMatcher) {
        // trigger initializations if any is performed during matching message phase
        expectedMatcher.matchingTokenizedMessage(actualPath, actual);

        boolean matches = expectedMatcher.matches(actualPath, actual);
        if (matches) {
            comparator.reportEqual(this, actualPath, expectedMatcher.matchedTokenizedMessage(actualPath, actual));
        } else {
            comparator.reportNotEqual(this, actualPath, expectedMatcher.mismatchedTokenizedMessage(actualPath, actual));
        }
    }

    private void handleNegativeMatcher(CompareToComparator comparator, ValuePath actualPath, Object actual, ValueMatcher expectedMatcher) {
        // trigger initializations if any is performed during matching message phase
        expectedMatcher.negativeMatchingTokenizedMessage(actualPath, actual);

        boolean matches = expectedMatcher.negativeMatches(actualPath, actual);
        if (matches) {
            comparator.reportNotEqual(this, actualPath, expectedMatcher.negativeMatchedTokenizedMessage(actualPath, actual));
        } else {
            comparator.reportEqual(this, actualPath, expectedMatcher.negativeMismatchedTokenizedMessage(actualPath, actual));
        }
    }
}
