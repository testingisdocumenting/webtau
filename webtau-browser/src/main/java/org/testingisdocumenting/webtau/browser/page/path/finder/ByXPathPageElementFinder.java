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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testingisdocumenting.webtau.browser.page.path.PageElementFinder;
import org.testingisdocumenting.webtau.browser.page.path.PageElementPathSearchContext;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.List;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class ByXPathPageElementFinder implements PageElementFinder {
    private final String xPath;

    public ByXPathPageElementFinder(String xPath) {
        this.xPath = xPath;
    }

    @Override
    public List<WebElement> find(PageElementPathSearchContext parent) {
        return parent.searchContext().findElements(By.xpath(xPath));
    }

    @Override
    public TokenizedMessage description(boolean isFirst) {
        TokenizedMessage byCssMessage = tokenizedMessage().selectorType("by xpath").selectorValue(xPath);
        return isFirst ? byCssMessage : tokenizedMessage().selectorType("nested find by xpath").selectorValue(xPath);
    }
}
