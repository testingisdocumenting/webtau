/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.page.path.finder;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.testingisdocumenting.webtau.browser.AdditionalBrowserInteractions;
import org.testingisdocumenting.webtau.browser.page.path.PageElementFinder;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.Collections;
import java.util.List;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class ParentByCssPageElementFinder implements PageElementFinder {
    private final AdditionalBrowserInteractions additionalBrowserInteractions;
    private final String css;

    public ParentByCssPageElementFinder(AdditionalBrowserInteractions additionalBrowserInteractions, String css) {
        this.additionalBrowserInteractions = additionalBrowserInteractions;
        this.css = css;
    }

    @Override
    public List<WebElement> find(SearchContext parent) {
        WebElement webElement = additionalBrowserInteractions.parentByCss(parent, css);
        return webElement == null ? Collections.emptyList() : Collections.singletonList(webElement);
    }

    @Override
    public TokenizedMessage description(boolean isFirst) {
        return tokenizedMessage().selectorType("parent element by css").selectorValue(css);
    }
}
