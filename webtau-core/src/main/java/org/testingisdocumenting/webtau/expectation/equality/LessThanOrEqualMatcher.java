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

package org.testingisdocumenting.webtau.expectation.equality;

import org.testingisdocumenting.webtau.data.render.DataRenderers;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.ExpectedValuesAware;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;

import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.expectation.equality.CompareToComparator.AssertionMode.LESS_THAN_OR_EQUAL;

public class LessThanOrEqualMatcher implements ValueMatcher, ExpectedValuesAware {
    private CompareToComparator compareToComparator;
    private final Object expected;

    public LessThanOrEqualMatcher(Object expected) {
        this.expected = expected;
    }

    @Override
    public String matchingMessage() {
        return "to be less than or equal to " + DataRenderers.render(expected);
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "less than or equal " + DataRenderers.render(expected) + "\n" +
                compareToComparator.generateLessThanOrEqualToMatchReport();
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return compareToComparator.generateLessThanOrEqualMismatchReport();
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        compareToComparator = CompareToComparator.comparator();
        return compareToComparator.compareIsLessOrEqual(actualPath, actual, expected);
    }

    @Override
    public String negativeMatchingMessage() {
        return "to be greater than " + DataRenderers.render(expected);
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return "greater than " + DataRenderers.render(expected) + '\n' +
                compareToComparator.generateGreaterThanMatchReport();
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return compareToComparator.generateGreaterThanMismatchReport();
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        compareToComparator = CompareToComparator.comparator();
        return compareToComparator.compareIsGreater(actualPath, actual, expected);
    }

    @Override
    public String toString() {
        return GreaterLessEqualMatcherRenderer.render(this, LESS_THAN_OR_EQUAL, expected);
    }

    @Override
    public Stream<Object> expectedValues() {
        return Stream.of(expected);
    }
}
