/*
 * Copyright 2022 webtau maintainers
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

import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.DataRenderers;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.ExpectedValuesAware;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;

import java.util.Set;
import java.util.stream.Stream;

public class EqualMatcher implements ValueMatcher, ExpectedValuesAware {
    private CompareToComparator comparator;
    private final Object expected;
    private final ValueMatcher expectedMatcher;

    public EqualMatcher(Object expected) {
        this.expected = expected;
        this.expectedMatcher = expected instanceof ValueMatcher ?
                (ValueMatcher) expected :
                null;
    }

    @Override
    public ValueConverter valueConverter() {
        return comparator == null ? ValueConverter.EMPTY : comparator.createValueConverter();
    }

    @Override
    public String matchingMessage() {
        if (expectedMatcher != null) {
            return expectedMatcher.matchingMessage();
        }

        return "to equal " + DataRenderers.renderLimitFromEnd(expected, 5);
    }

    @Override
    public String matchedMessage(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.matchedMessage(actualPath, actual);
        }

        return "equals " + DataRenderers.render(expected) + "\n" +
                comparator.generateEqualMatchReport();
    }

    @Override
    public Set<ValuePath> matchedPaths() {
        return comparator.generateEqualMatchPaths();
    }

    @Override
    public String mismatchedMessage(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.mismatchedMessage(actualPath, actual);
        }

        return comparator.generateEqualMismatchReport();
    }

    @Override
    public Set<ValuePath> mismatchedPaths() {
        return comparator.generateEqualMismatchPaths();
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.matches(actualPath, actual);
        }

        comparator = CompareToComparator.comparator();
        return comparator.compareIsEqual(actualPath, actual, expected);
    }

    @Override
    public String negativeMatchingMessage() {
        if (expectedMatcher != null) {
            return expectedMatcher.negativeMatchingMessage();
        }

        return "to not equal " + DataRenderers.render(expected);
    }

    @Override
    public String negativeMatchedMessage(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.negativeMatchedMessage(actualPath, actual);
        }

        return "doesn't equal " + DataRenderers.render(expected) + "\n" +
                comparator.generateNotEqualMatchReport();
    }

    @Override
    public String negativeMismatchedMessage(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.negativeMismatchedMessage(actualPath, actual);
        }

        return comparator.generateNotEqualMismatchReport();
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.negativeMatches(actualPath, actual);
        }

        comparator = CompareToComparator.comparator();
        return comparator.compareIsNotEqual(actualPath, actual, expected);
    }

    @Override
    public String toString() {
        if (expectedMatcher != null) {
            return expectedMatcher.toString();
        }

        return EqualNotEqualMatcherRenderer.render(this, comparator, expected);
    }

    @Override
    public Stream<Object> expectedValues() {
        if (expectedMatcher instanceof ExpectedValuesAware) {
            return ((ExpectedValuesAware) expectedMatcher).expectedValues();
        }

        return Stream.of(expected);
    }
}
