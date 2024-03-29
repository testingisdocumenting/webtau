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
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.expectation.ExpectedValuesAware;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.expectation.equality.CompareToComparator.AssertionMode.GREATER_THAN;

public class GreaterThanMatcher implements ValueMatcher, ExpectedValuesAware, PrettyPrintable {
    private CompareToComparator compareToComparator;
    private final Object expected;

    public GreaterThanMatcher(Object expected) {
        this.expected = expected;
    }

    @Override
    public TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("to be greater than").valueFirstLinesOnly(expected);
    }

    @Override
    public TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("greater than").valueFirstLinesOnly(expected);
    }

    @Override
    public TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return compareToComparator.generateGreaterThanMismatchReport();
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actual) {
        compareToComparator = CompareToComparator.comparator();
        return compareToComparator.compareIsGreater(actualPath, actual, expected);
    }

    @Override
    public TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("to be less than or equal to").valueFirstLinesOnly(expected);
    }

    @Override
    public TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("less than or equal to").valueFirstLinesOnly(expected);
    }

    @Override
    public TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return compareToComparator.generateLessThanOrEqualMismatchReport();
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actual) {
        compareToComparator = CompareToComparator.comparator();
        return compareToComparator.compareIsLessOrEqual(actualPath, actual, expected);
    }

    @Override
    public Stream<Object> expectedValues() {
        return Stream.of(expected);
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        GreaterLessEqualMatcherPrinter.prettyPrint(printer, this, GREATER_THAN, expected);
    }

    @Override
    public String toString() {
        return PrettyPrinter.renderAsTextWithoutColors(this);
    }
}
