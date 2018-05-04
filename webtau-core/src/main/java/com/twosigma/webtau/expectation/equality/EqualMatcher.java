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

package com.twosigma.webtau.expectation.equality;

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.ValueMatcher;

public class EqualMatcher implements ValueMatcher {
    private EqualComparator equalComparator;
    private final Object expected;

    public EqualMatcher(Object expected) {
        this.expected = expected;
    }

    @Override
    public String matchingMessage() {
        return "to equal " + DataRenderers.render(expected);
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "equals " + DataRenderers.render(expected);
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return equalComparator.generateMismatchReport();
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        equalComparator = EqualComparator.comparator();
        return compare(actualPath, actual);
    }

    @Override
    public String negativeMatchingMessage() {
        return "to not equal " + DataRenderers.render(expected);
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return "doesn't equal " + DataRenderers.render(expected);
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return actualPath + " equals " + DataRenderers.render(expected) + "\nactual:\n" + actual;
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        equalComparator = EqualComparator.negativeComparator();
        return compare(actualPath, actual);
    }

    private boolean compare(ActualPath actualPath, Object actual) {
        equalComparator.compare(actualPath, actual, expected);
        return equalComparator.areEqual();
    }
}
