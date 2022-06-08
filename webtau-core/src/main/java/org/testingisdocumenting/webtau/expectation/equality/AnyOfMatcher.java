/*
 * Copyright 2022 webtau maintainers
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

import java.util.Collection;
import java.util.stream.Stream;

public class AnyOfMatcher implements ValueMatcher, ExpectedValuesAware {
    private final Collection<Object> expectedList;
    private CompareToComparator comparator;

    public AnyOfMatcher(Collection<Object> expected) {
        this.expectedList = expected;
    }

    @Override
    public Stream<Object> expectedValues() {
        return expectedList.stream();
    }

    @Override
    public String matchingMessage() {
        return "to equal any of " + DataRenderers.render(expectedList);
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "equals any of " + DataRenderers.render(expectedList) + "\n" +
                comparator.generateEqualMatchReport();
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return comparator.generateEqualMismatchReport();
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        boolean result = false;

        comparator = CompareToComparator.comparator();
        for (Object expected : expectedList) {
            result = result || comparator.compareIsEqual(actualPath, actual, expected);
        }

        return result;
    }

    @Override
    public String negativeMatchingMessage() {
        return "to not equal any of " + DataRenderers.render(expectedList);
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return "doesn't equal any of " + DataRenderers.render(expectedList) + "\n" +
                comparator.generateNotEqualMatchReport();
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return comparator.generateNotEqualMismatchReport();
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        boolean result = true;

        comparator = CompareToComparator.comparator();
        for (Object expected : expectedList) {
            result = result && comparator.compareIsNotEqual(actualPath, actual, expected);
        }

        return result;
    }
}
