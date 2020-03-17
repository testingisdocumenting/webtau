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

package org.testingisdocumenting.webtau.browser.page.path.filter;

import org.testingisdocumenting.webtau.browser.AdditionalBrowserInteractions;
import org.testingisdocumenting.webtau.browser.page.path.ElementsFilter;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Pattern;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.selectorType;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.selectorValue;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class ByRegexpElementsFilter implements ElementsFilter {
    private final AdditionalBrowserInteractions additionalBrowserInteractions;
    private Pattern regexp;

    public ByRegexpElementsFilter(AdditionalBrowserInteractions additionalBrowserInteractions, Pattern regexp) {
        this.additionalBrowserInteractions = additionalBrowserInteractions;
        this.regexp = regexp;
    }

    @Override
    public List<WebElement> filter(List<WebElement> original) {
        return additionalBrowserInteractions.filterByRegexp(original, regexp.pattern());
    }

    @Override
    public TokenizedMessage description() {
        return tokenizedMessage(selectorType("element(s) with regexp"), selectorValue(regexp.pattern()));
    }
}
