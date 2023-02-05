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
import org.testingisdocumenting.webtau.data.ValuePath;
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
        return "to match any of " + DataRenderers.render(expectedList);
    }

    @Override
    public String matchedMessage(ValuePath actualPath, Object actual) {
        return "matches any of " + DataRenderers.render(expectedList);
    }

    @Override
    public String mismatchedMessage(ValuePath actualPath, Object actual) {
        return comparator.generateEqualMismatchReport();
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actual) {
        boolean result = false;

        comparator = CompareToComparator.comparator();
        for (Object expected : expectedList) {
            result = result || comparator.compareIsEqual(actualPath, actual, expected);
        }

        return result;
    }

    @Override
    public String negativeMatchingMessage() {
        return "to not match any of " + DataRenderers.render(expectedList);
    }

    @Override
    public String negativeMatchedMessage(ValuePath actualPath, Object actual) {
        return "doesn't match any of " + DataRenderers.render(expectedList);
    }

    @Override
    public String negativeMismatchedMessage(ValuePath actualPath, Object actual) {
        return comparator.generateNotEqualMismatchReport();
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actual) {
        boolean result = true;

        comparator = CompareToComparator.comparator();
        for (Object expected : expectedList) {
            result = result && comparator.compareIsNotEqual(actualPath, actual, expected);
        }

        return result;
    }
}
