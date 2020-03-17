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
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

import java.util.regex.Pattern;

import static org.testingisdocumenting.webtau.expectation.equality.handlers.HandlerMessages.expected;

public class RegexpEqualCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof String && expected instanceof Pattern;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        Pattern expectedPattern = (Pattern) expected;

        boolean isEqual = expectedPattern.matcher(actual.toString()).find();
        comparator.reportEqualOrNotEqual(this, isEqual,
                actualPath, renderActualExpected(comparator.getAssertionMode(), actual, expected));
    }

    private String renderActualExpected(CompareToComparator.AssertionMode assertionMode, Object actual, Object expected) {
        return "   actual string: " + actual.toString() + "\n" +
               expected("expected pattern: ", assertionMode, expected.toString());
    }
}
