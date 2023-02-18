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

package org.testingisdocumenting.webtau.expectation.contain;

import org.testingisdocumenting.webtau.data.render.DataRenderers;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.ExpectedValuesAware;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class ContainMatcher implements ValueMatcher, ExpectedValuesAware {
    private ContainAnalyzer containAnalyzer;
    private final Object expected;
    private Boolean isNegative;

    public ContainMatcher(Object expected) {
        this.expected = expected;
    }

    @Override
    public TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("to contain").valueFirstLinesOnly(expected);
    }

    @Override
    public TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("contains").value(expected);
    }

    @Override
    public TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().error("no match found");
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actual) {
        containAnalyzer = ContainAnalyzer.containAnalyzer();
        isNegative = false;

        containAnalyzer.contains(actualPath, actual, expected);
        return containAnalyzer.hasMismatches();
    }

    @Override
    public TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("to not contain").valueFirstLinesOnly(expected);
    }

    @Override
    public TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("does not contain").value(expected);
    }

    @Override
    public TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().error("match is found");
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actual) {
        containAnalyzer = ContainAnalyzer.containAnalyzer();
        isNegative = true;

        containAnalyzer.notContains(actualPath, actual, expected);
        return containAnalyzer.hasMismatches();
    }

    @Override
    public String toString() {
        String renderedExpected = DataRenderers.render(expected);

        if (isNegative == null) {
            return this.getClass().getCanonicalName() + " " + renderedExpected;
        } else if (isNegative) {
            return "<not contain " + renderedExpected + ">";
        } else {
            return "<contain " + renderedExpected + ">";
        }
    }

    @Override
    public Stream<Object> expectedValues() {
        return Stream.of(expected);
    }
}
