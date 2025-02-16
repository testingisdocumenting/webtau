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

package org.testingisdocumenting.webtau.browser.page.path.filter;

import org.openqa.selenium.WebElement;
import org.testingisdocumenting.webtau.browser.AdditionalBrowserInteractions;
import org.testingisdocumenting.webtau.browser.page.NullWebElement;
import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.browser.page.path.PageElementsFilter;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.Collections;
import java.util.List;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class NearbyPageElementFilter implements PageElementsFilter {
    private final AdditionalBrowserInteractions additionalBrowserInteractions;
    private final PageElement target;

    public NearbyPageElementFilter(AdditionalBrowserInteractions additionalBrowserInteractions, PageElement target) {
        this.additionalBrowserInteractions = additionalBrowserInteractions;
        this.target = target;
    }

    @Override
    public List<WebElement> filter(List<WebElement> original) {
        WebElement targetElement = target.findElement();
        if (targetElement instanceof NullWebElement) {
            return Collections.emptyList();
        }

        return additionalBrowserInteractions.filterByNearby(original, targetElement);
    }

    @Override
    public TokenizedMessage description() {
        return tokenizedMessage().classifier("nearby").add(target.describe());
    }
}
