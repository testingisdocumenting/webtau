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

package org.testingisdocumenting.webtau.browser.expectation;

import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;

public class DisabledValueMatcher implements ValueMatcher {
    @Override
    public String matchingMessage() {
        return "to be disabled";
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "is disabled";
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return "is enabled";
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        PageElement pageElement = (PageElement) actual;
        return ! pageElement.isEnabled();
    }

    @Override
    public String negativeMatchingMessage() {
        return "to be enabled";
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return "is enabled";
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return "is disabled";
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        return ! matches(actualPath, actual);
    }
}
