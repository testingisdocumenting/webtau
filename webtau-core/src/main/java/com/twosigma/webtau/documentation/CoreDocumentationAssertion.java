/*
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

package com.twosigma.webtau.documentation;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.ExpectationHandler;
import com.twosigma.webtau.expectation.ExpectedValuesAware;
import com.twosigma.webtau.expectation.ValueMatcher;

import java.util.List;
import java.util.stream.Collectors;

public class CoreDocumentationAssertion implements ExpectationHandler {
    private final ThreadLocal<Object> lastActualValue = new ThreadLocal<>();
    private final ThreadLocal<Object> lastExpectedValues = new ThreadLocal<>();

    @Override
    public void onValueMatch(ValueMatcher valueMatcher, ActualPath actualPath, Object actualValue) {
        lastActualValue.set(actualValue);
        lastExpectedValues.set(extractExpectedValues(valueMatcher));
    }

    Object actualValue() {
        return lastActualValue.get();
    }

    Object expectedValue() {
        return lastExpectedValues.get();
    }

    private static Object extractExpectedValues(ValueMatcher valueMatcher) {
        if (!(valueMatcher instanceof ExpectedValuesAware)) {
            return null;
        }

        List<Object> list = ((ExpectedValuesAware) valueMatcher).expectedValues().collect(Collectors.toList());
        if (list.isEmpty()) {
            return null;
        }

        if (list.size() > 1) {
            return list;
        }

        return list.get(0);
    }
}
