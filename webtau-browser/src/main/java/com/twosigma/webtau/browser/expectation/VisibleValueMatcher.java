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

package com.twosigma.webtau.browser.expectation;

import com.twosigma.webtau.browser.page.ElementValue;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.ValueMatcher;

public class VisibleValueMatcher implements ValueMatcher {
    @Override
    public String matchingMessage() {
        return "to be visible";
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "is visible";
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return "is hidden";
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        ElementValue elementValue = (ElementValue) actual;
        return elementValue.getParent().isVisible();
    }

    @Override
    public String negativeMatchingMessage() {
        return "to be hidden";
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return "is hidden";
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return "is visible";
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        return ! matches(actualPath, actual);
    }
}
