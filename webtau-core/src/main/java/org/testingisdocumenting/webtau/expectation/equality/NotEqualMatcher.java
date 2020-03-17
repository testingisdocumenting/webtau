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

public class NotEqualMatcher implements ValueMatcher, ExpectedValuesAware {
    private CompareToComparator comparator;
    private final Object expected;

    public NotEqualMatcher(Object expected) {
        this.expected = expected;
    }

    @Override
    public String matchingMessage() {
        return "to not equal " + DataRenderers.render(expected);
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "doesn't equal " + DataRenderers.render(expected) + "\n" +
                comparator.generateNotEqualMatchReport();
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return "equals " + DataRenderers.render(expected) + ", but shouldn't\n" +
                comparator.generateNotEqualMismatchReport();
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        comparator = CompareToComparator.comparator();
        return comparator.compareIsNotEqual(actualPath, actual, expected);
    }

    @Override
    public String negativeMatchingMessage() {
        return "to equal " + DataRenderers.render(expected);
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return "equals " + DataRenderers.render(expected) + "\n" +
                comparator.generateEqualMatchReport();
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return "doesn't equal " + DataRenderers.render(expected) + ", but should\n" +
                comparator.generateEqualMismatchReport();
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        comparator = CompareToComparator.comparator();
        return comparator.compareIsEqual(actualPath, actual, expected);
    }

    @Override
    public String toString() {
        return EqualNotEqualMatcherRenderer.render(this, comparator, expected);
    }

    @Override
    public Stream<Object> expectedValues() {
        return Stream.of(expected);
    }
}
