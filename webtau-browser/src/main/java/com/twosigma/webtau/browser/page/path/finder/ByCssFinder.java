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

package com.twosigma.webtau.browser.page.path.finder;

import com.twosigma.webtau.browser.page.path.ElementsFinder;
import com.twosigma.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.selectorType;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.selectorValue;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class ByCssFinder implements ElementsFinder {
    private String css;

    public ByCssFinder(String css) {
        this.css = css;
    }

    @Override
    public List<WebElement> find(SearchContext parent) {
        return parent.findElements(By.cssSelector(css));
    }

    @Override
    public TokenizedMessage description(boolean isFirst) {
        TokenizedMessage byCssMessage = tokenizedMessage(selectorType("by css"), selectorValue(css));
        return isFirst ? byCssMessage : tokenizedMessage(selectorType("nested find by css"), selectorValue(css));
    }
}
