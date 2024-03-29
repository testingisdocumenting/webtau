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

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.expectation.ExpectedValuesAware;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.Collection;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class AnyOfMatcher implements ValueMatcher, ExpectedValuesAware, PrettyPrintable {
    private final Collection<Object> expectedList;
    private CompareToComparator comparator;

    public AnyOfMatcher(Collection<Object> expected) {
        this.expectedList = expected;
    }

    @Override
    public ValueConverter valueConverter() {
        return comparator.createValueConverter();
    }

    @Override
    public Stream<Object> expectedValues() {
        return expectedList.stream();
    }

    @Override
    public TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual) {
        comparator = CompareToComparator.comparator();
        return tokenizedMessage().matcher("to match any of").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("matches any of").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return comparator.generateEqualMismatchReport();
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actual) {
        boolean result = false;

        comparator.resetReportData();
        for (Object expected : expectedList) {
            result = result || comparator.compareIsEqual(actualPath, actual, expected);
        }

        return result;
    }

    @Override
    public TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual) {
        comparator = CompareToComparator.comparator();
        return tokenizedMessage().matcher("to not match any of").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("doesn't match any of").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return comparator.generateNotEqualMismatchReport();
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actual) {
        boolean result = true;

        comparator.resetReportData();
        for (Object expected : expectedList) {
            result = result && comparator.compareIsNotEqual(actualPath, actual, expected);
        }

        return result;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        printer.printDelimiter("<");
        printer.print(PrettyPrinter.CLASSIFIER_COLOR, "any of ");
        printer.printObject(expectedList);
        printer.printDelimiter(">");
    }

    @Override
    public String toString() {
        return PrettyPrinter.renderAsTextWithoutColors(this);
    }
}
