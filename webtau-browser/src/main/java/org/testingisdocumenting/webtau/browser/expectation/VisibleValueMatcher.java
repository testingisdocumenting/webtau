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

package org.testingisdocumenting.webtau.browser.expectation;

import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class VisibleValueMatcher implements ValueMatcher {
    @Override
    public TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("to be visible");
    }

    @Override
    public TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("is visible");
    }

    @Override
    public TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("is hidden");
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actual) {
        PageElement pageElement = (PageElement) actual;
        return pageElement.isVisible();
    }

    @Override
    public TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("to be hidden");
    }

    @Override
    public TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("is hidden");
    }

    @Override
    public TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("is visible");
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actual) {
        return ! matches(actualPath, actual);
    }
}
