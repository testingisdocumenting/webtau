/*
 * Copyright 2021 webtau maintainers
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

import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.DataRenderers;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.ExpectedValuesAware;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class ContainAllMatcher implements ValueMatcher, ExpectedValuesAware {
    private ContainAnalyzer containAnalyzer;
    private final Collection<Object> expectedList;
    private List<Object> expectedThatFailContains;

    public ContainAllMatcher(Collection<Object> expected) {
        this.expectedList = expected;
    }

    @Override
    public ValueConverter valueConverter() {
        return containAnalyzer.createValueConverter();
    }

    @Override
    public Set<ValuePath> matchedPaths() {
        return containAnalyzer.generateMatchPaths();
    }

    @Override
    public Set<ValuePath> mismatchedPaths() {
        return containAnalyzer.generateMismatchPaths();
    }

    @Override
    public TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual) {
        containAnalyzer = ContainAnalyzer.containAnalyzer();
        return tokenizedMessage().matcher("to contain all").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("contains all").value(expectedList);
    }

    @Override
    public TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().error("no matches found for").colon().value(expectedThatFailContains);
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actual) {
        expectedThatFailContains = new ArrayList<>();
        containAnalyzer.resetReportData();

        for (Object oneOfExpected : expectedList) {
            boolean expectedContains = containAnalyzer.contains(actualPath, actual, oneOfExpected);
            if (!expectedContains) {
                expectedThatFailContains.add(oneOfExpected);
            }
        }

        return containAnalyzer.noMismatches();
    }

    @Override
    public TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual) {
        containAnalyzer = ContainAnalyzer.containAnalyzer();
        return tokenizedMessage().matcher("to not contain all").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("does not contain all").value(expectedList);
    }

    @Override
    public TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return containAnalyzer.generateMismatchReport();
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actual) {
        containAnalyzer.resetReportData();

        boolean allContains = true;
        for (Object oneOfExpected : expectedList) {
            allContains = allContains && !containAnalyzer.notContains(actualPath, actual, oneOfExpected);
        }

        return !allContains;
    }

    @Override
    public String toString() {
        String renderedExpected = DataRenderers.render(expectedList);
        return "<contain all " + renderedExpected + ">";
    }

    @Override
    public Stream<Object> expectedValues() {
        return expectedList.stream();
    }
}
