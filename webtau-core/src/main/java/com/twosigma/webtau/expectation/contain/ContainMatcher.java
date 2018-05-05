/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.expectation.contain;

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.ValueMatcher;

public class ContainMatcher implements ValueMatcher {
    private ContainAnalyzer containAnalyzer;
    private final Object expected;

    public ContainMatcher(Object expected) {
        this.expected = expected;
        this.containAnalyzer = ContainAnalyzer.containAnalyzer();
    }

    @Override
    public String matchingMessage() {
        return "co contain " + DataRenderers.render(expected);
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "contains " + DataRenderers.render(expected);
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return actualPath + " expect to contain " + DataRenderers.render(expected) + "\n" +
                containAnalyzer.generateMismatchReport();
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        containAnalyzer.contains(actualPath, actual, expected);
        return containAnalyzer.hasMismatches();
    }

    @Override
    public String negativeMatchingMessage() {
        return "to not contain " + DataRenderers.render(expected);
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return "does not contain " + DataRenderers.render(expected);
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return actualPath + " expect to not contain " + DataRenderers.render(expected) + "\n" +
                containAnalyzer.generateMismatchReport();
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        containAnalyzer.containsNot(actualPath, actual, expected);
        return containAnalyzer.hasMismatches();
    }
}
