/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.contain;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.ValuePathWithValue;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.expectation.ExpectedValuesAware;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class ContainExactlyMatcher implements ValueMatcher, ExpectedValuesAware, PrettyPrintable {
    private final Collection<Object> expectedList;
    private List<ValuePathWithValue<Object>> actualCopy;
    private List<ValuePathWithValue<Object>> expectedCopy;

    private CompareToComparator comparator;

    public ContainExactlyMatcher(Collection<Object> expected) {
        expectedList = expected;
    }

    @Override
    public ValueConverter valueConverter() {
        return comparator == null ? ValueConverter.EMPTY : comparator.createValueConverter();
    }

    @Override
    public Stream<Object> expectedValues() {
        return expectedList.stream();
    }

    @Override
    public Set<ValuePath> mismatchedPaths() {
        return actualCopy.stream().map(ValuePathWithValue::getPath).collect(Collectors.toSet());
    }

    @Override
    public TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual) {
        comparator = CompareToComparator.comparator(CompareToComparator.AssertionMode.EQUAL);
        return tokenizedMessage().matcher("to contain exactly").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("contains exactly").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        TokenizedMessage messageTokens = tokenizedMessage();
        if (!expectedCopy.isEmpty()) {
            messageTokens = messageTokens.error("no matches found for").colon().value(
                    expectedCopy.stream().map(ValuePathWithValue::getValue).toList());
        }

        if (!actualCopy.isEmpty()) {
            if (!messageTokens.isEmpty()) {
                messageTokens = messageTokens.newLine();
            }
            messageTokens = messageTokens.error("unexpected elements").colon().value(
                    actualCopy.stream().map(ValuePathWithValue::getValue).toList());
        }

        return messageTokens;
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actualIterable) {
        return matches(comparator, actualPath, actualIterable);
    }

    @Override
    public TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual) {
        comparator = CompareToComparator.comparator(CompareToComparator.AssertionMode.NOT_EQUAL);
        return tokenizedMessage().matcher("to not contain exactly").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("not contains exactly").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("contains exactly").value(expectedList);
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actualIterable) {
        return !matches(comparator, actualPath, actualIterable);
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        printer.printDelimiter("<");
        printer.print(PrettyPrinter.CLASSIFIER_COLOR, "containExactly ");
        printer.printObject(expectedList);
        printer.printDelimiter(">");
    }

    @Override
    public String toString() {
        return PrettyPrinter.renderAsTextWithoutColors(this);
    }

    @SuppressWarnings("unchecked")
    private boolean matches(CompareToComparator comparator, ValuePath actualPath, Object actualIterable) {
        if (!(actualIterable instanceof Iterable)) {
            return false;
        }

        actualCopy = ValuePathWithValue.listFromIterable(actualPath, ((Iterable<Object>) actualIterable));
        expectedCopy = ValuePathWithValue.listFromIterable(actualPath, expectedList);

        Iterator<ValuePathWithValue<Object>> expectedIt = expectedCopy.iterator();
        while (expectedIt.hasNext()) {
            ValuePathWithValue<Object> expected = expectedIt.next();
            Iterator<ValuePathWithValue<Object>> actualIt = actualCopy.iterator();
            while (actualIt.hasNext()) {
                ValuePathWithValue<Object> actual = actualIt.next();
                boolean isEqual = comparator.compareIsEqual(actual.getPath(), actual.getValue(), expected.getValue());
                if (isEqual) {
                    actualIt.remove();
                    expectedIt.remove();
                    break;
                }
            }
        }

        return actualCopy.isEmpty() && expectedCopy.isEmpty();
    }
}
