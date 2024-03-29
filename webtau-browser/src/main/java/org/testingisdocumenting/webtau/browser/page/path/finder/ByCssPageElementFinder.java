/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.page.path.finder;

import org.testingisdocumenting.webtau.browser.page.path.PageElementFinder;
import org.testingisdocumenting.webtau.browser.page.path.PageElementPathSearchContext;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.testingisdocumenting.webtau.WebTauCore.tokenizedMessage;

public class ByCssPageElementFinder implements PageElementFinder {
    private final String css;

    public ByCssPageElementFinder(String css) {
        this.css = css;
    }

    @Override
    public List<WebElement> find(PageElementPathSearchContext parent) {
        return parent.searchContext().findElements(By.cssSelector(css));
    }

    @Override
    public TokenizedMessage description(boolean isFirst) {
        TokenizedMessage byCssMessage = tokenizedMessage().selectorType("by css").selectorValue(css);
        return isFirst ? byCssMessage : tokenizedMessage().selectorType("nested find by css").selectorValue(css);
    }
}
