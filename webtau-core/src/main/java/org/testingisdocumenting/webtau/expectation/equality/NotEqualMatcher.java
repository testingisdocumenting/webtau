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
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.expectation.ExpectedValuesAware;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class NotEqualMatcher implements ValueMatcher, ExpectedValuesAware, PrettyPrintable {
    private CompareToComparator comparator;

    private final Object expected;
    private final ValueMatcher expectedMatcher;

    public NotEqualMatcher(Object expected) {
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
    public TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.negativeMatchingTokenizedMessage(actualPath, actual);
        }

        comparator = CompareToComparator.comparator();
        return tokenizedMessage().matcher("to not equal").valueFirstLinesOnly(expected);
    }

    @Override
    public TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.negativeMatchedTokenizedMessage(actualPath, actual);
        }

        return tokenizedMessage().matcher("doesn't equal").valueFirstLinesOnly(expected);
    }

    @Override
    public TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.negativeMismatchedTokenizedMessage(actualPath, actual);
        }

        return comparator.generateNotEqualMismatchReport();
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.negativeMatches(actualPath, actual);
        }

        comparator.resetReportData();
        return comparator.compareIsNotEqual(actualPath, actual, expected);
    }

    @Override
    public TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.matchingTokenizedMessage(actualPath, actual);
        }

        comparator = CompareToComparator.comparator();
        return tokenizedMessage().matcher("to equal").valueFirstLinesOnly(expected);
    }

    @Override
    public TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.matchedTokenizedMessage(actualPath, actual);
        }

        return tokenizedMessage().matcher("equals").valueFirstLinesOnly(expected);
    }

    @Override
    public TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.mismatchedTokenizedMessage(actualPath, actual);
        }

        return comparator.generateEqualMismatchReport();
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actual) {
        if (expectedMatcher != null) {
            return expectedMatcher.matches(actualPath, actual);
        }

        comparator.resetReportData();
        return comparator.compareIsEqual(actualPath, actual, expected);
    }

    @Override
    public Stream<Object> expectedValues() {
        return Stream.of(expected);
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        GreaterLessEqualMatcherPrinter.prettyPrint(printer, this, CompareToComparator.AssertionMode.NOT_EQUAL, expected);
    }

    @Override
    public String toString() {
        return PrettyPrinter.renderAsTextWithoutColors(this);
    }
}
