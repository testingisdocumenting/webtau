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

import org.testingisdocumenting.webtau.data.render.DataRenderers;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.ExpectedValuesAware;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;

import java.util.Collection;
import java.util.stream.Stream;

public class ContainAllMatcher implements ValueMatcher, ExpectedValuesAware {
    private ContainAnalyzer containAnalyzer;
    private final Collection<Object> expectedList;
    private Boolean isNegative;

    public ContainAllMatcher(Collection<Object> expected) {
        this.expectedList = expected;
    }

    @Override
    public String matchingMessage() {
        return "to contain all " + DataRenderers.render(expectedList);
    }

    @Override
    public String matchedMessage(ValuePath actualPath, Object actual) {
        return "contains all " + DataRenderers.render(expectedList);
    }

    @Override
    public String mismatchedMessage(ValuePath actualPath, Object actual) {
        return actualPath + " expects to contain all " + DataRenderers.render(expectedList) + "\n" +
                containAnalyzer.generateMismatchReport();
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actual) {
        containAnalyzer = ContainAnalyzer.containAnalyzer();
        isNegative = false;

        for (Object oneOfExpected : expectedList) {
            containAnalyzer.contains(actualPath, actual, oneOfExpected);
        }

        return containAnalyzer.hasMismatches();
    }

    @Override
    public String negativeMatchingMessage() {
        return "to not contain all " + DataRenderers.render(expectedList);
    }

    @Override
    public String negativeMatchedMessage(ValuePath actualPath, Object actual) {
        return "does not contain all " + DataRenderers.render(expectedList);
    }

    @Override
    public String negativeMismatchedMessage(ValuePath actualPath, Object actual) {
        return actualPath + " expects to not contain all " + DataRenderers.render(expectedList) + "\n" +
                containAnalyzer.generateMismatchReport();
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actual) {
        containAnalyzer = ContainAnalyzer.containAnalyzer();
        isNegative = true;

        boolean allContains = true;
        for (Object oneOfExpected : expectedList) {
            // we need !not as `contains` is not producing any report info at this moment
            allContains = allContains && !containAnalyzer.notContains(actualPath, actual, oneOfExpected);
        }

        return !allContains;
    }

    @Override
    public String toString() {
        String renderedExpected = DataRenderers.render(expectedList);

        if (isNegative == null) {
            return this.getClass().getCanonicalName() + " " + renderedExpected;
        } else if (isNegative) {
            return "<not contain all " + renderedExpected + ">";
        } else {
            return "<contain all " + renderedExpected + ">";
        }
    }

    @Override
    public Stream<Object> expectedValues() {
        return expectedList.stream();
    }
}
