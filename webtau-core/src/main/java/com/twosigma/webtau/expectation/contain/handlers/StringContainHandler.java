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

package com.twosigma.webtau.expectation.contain.handlers;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.contain.ContainAnalyzer;
import com.twosigma.webtau.expectation.contain.ContainHandler;

import java.util.function.BiFunction;
import java.util.function.Function;

public class StringContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof String && expected instanceof String;
    }

    @Override
    public void analyzeContain(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        analyze(containAnalyzer, actualPath, actual, expected, String::contains);
    }

    @Override
    public void analyzeNotContain(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        analyze(containAnalyzer, actualPath, actual, expected,
                (actualString, expectedString) -> !actualString.contains(expectedString));
    }

    private void analyze(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected,
                         BiFunction<String, String, Boolean> predicate) {
        String actualString = (String) actual;
        String expectedString = (String) expected;

        if (!predicate.apply(actualString, expectedString)) {
            containAnalyzer.reportMismatch(this, actualPath, actualString);
        }
    }
}
