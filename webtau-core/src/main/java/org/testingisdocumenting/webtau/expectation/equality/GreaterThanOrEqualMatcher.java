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

package org.testingisdocumenting.webtau.expectation.equality;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.ExpectedValuesAware;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.expectation.equality.CompareToComparator.AssertionMode.*;

public class GreaterThanOrEqualMatcher implements ValueMatcher, ExpectedValuesAware {
    private CompareToComparator compareToComparator;
    private final Object expected;

    public GreaterThanOrEqualMatcher(Object expected) {
        this.expected = expected;
    }

    @Override
    public TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("to be greater than or equal to").valueFirstLinesOnly(expected);
    }

    @Override
    public TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("greater than or equal").value(expected);
    }

    @Override
    public TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return compareToComparator.generateGreaterThanOrEqualMismatchReport();
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actual) {
        compareToComparator = CompareToComparator.comparator();
        return compareToComparator.compareIsGreaterOrEqual(actualPath, actual, expected);
    }

    @Override
    public TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("to be less than").valueFirstLinesOnly(expected);
    }

    @Override
    public TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("less than").value(expected);
    }

    @Override
    public TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return compareToComparator.generateLessThanMismatchReport();
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actual) {
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
