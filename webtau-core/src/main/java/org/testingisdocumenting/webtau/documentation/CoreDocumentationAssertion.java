/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.documentation;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.*;

import java.util.List;
import java.util.stream.Collectors;

public class CoreDocumentationAssertion implements ExpectationHandler {
    private final ThreadLocal<Object> lastActualValue = new ThreadLocal<>();
    private final ThreadLocal<Object> lastExpectedValues = new ThreadLocal<>();

    @Override
    public void onValueMatch(ValueMatcher valueMatcher, ValuePath actualPath, Object actualValue) {
        lastActualValue.set(actualValue);
        lastExpectedValues.set(extractExpectedValues(valueMatcher));
    }

    @Override
    public void onCodeMatch(CodeMatcher codeMatcher) {
        lastActualValue.set(extractActualValue(codeMatcher));
        lastExpectedValues.set(extractExpectedValues(codeMatcher));
    }

    Object actualValue() {
        return lastActualValue.get();
    }

    Object expectedValue() {
        return lastExpectedValues.get();
    }

    private static Object extractExpectedValues(Object matcher) {
        if (!(matcher instanceof ExpectedValuesAware)) {
            return null;
        }

        List<Object> list = ((ExpectedValuesAware) matcher).expectedValues().collect(Collectors.toList());
        if (list.isEmpty()) {
            return null;
        }

        if (list.size() > 1) {
            return list;
        }

        return list.get(0);
    }

    private static Object extractActualValue(CodeMatcher codeMatcher) {
        if (!(codeMatcher instanceof ActualValueAware)) {
            return null;
        }

        return ((ActualValueAware) codeMatcher).actualValue();
    }
}
