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

package com.twosigma.webtau.expectation.equality;

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.ExpectedValuesAware;
import com.twosigma.webtau.expectation.ValueMatcher;

import java.util.stream.Stream;

import static com.twosigma.webtau.expectation.equality.CompareToComparator.AssertionMode.GREATER_THAN_OR_EQUAL;

public class GreaterThanOrEqualMatcher implements ValueMatcher, ExpectedValuesAware {
    private CompareToComparator compareToComparator;
    private final Object expected;

    public GreaterThanOrEqualMatcher(Object expected) {
        this.expected = expected;
    }

    @Override
    public String matchingMessage() {
        return "to be greater than or equal to " + DataRenderers.render(expected);
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "greater than or equal " + DataRenderers.render(expected) + "\n" +
                compareToComparator.generateGreaterThanOrEqualMatchReport();
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return compareToComparator.generateGreaterThanOrEqualMismatchReport();
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        compareToComparator = CompareToComparator.comparator();
        return compareToComparator.compareIsGreaterOrEqual(actualPath, actual, expected);
    }

    @Override
    public String negativeMatchingMessage() {
        return "to be less than " + DataRenderers.render(expected);
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return "less than " + DataRenderers.render(expected) + '\n' +
                compareToComparator.generateLessThanMatchReport();
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return compareToComparator.generateLessThanMismatchReport();
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        compareToComparator = CompareToComparator.comparator();
        return compareToComparator.compareIsLess(actualPath, actual, expected);
    }

    @Override
    public String toString() {
        return GreaterLessEqualMatcherRenderer.render(this, GREATER_THAN_OR_EQUAL, expected);
    }

    @Override
    public Stream<Object> expectedValues() {
        return Stream.of(expected);
    }
}
