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

package org.testingisdocumenting.webtau;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.testingisdocumenting.webtau.browser.AdditionalBrowserInteractions;
import org.testingisdocumenting.webtau.browser.page.HtmlNode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FakeAdditionalBrowserInteractions implements AdditionalBrowserInteractions {
    @Override
    public void flashWebElements(List<WebElement> webElements) {
    }

    @Override
    public List<HtmlNode> extractHtmlNodes(List<WebElement> webElements) {
        return webElements.stream()
                .map(webElement -> new HtmlNode(
                        webElement.getTagName(),
                        "",
                        ((FakeWebElement)webElement).getOuterHtml(),
                        webElement.getText(),
                        "",
                        Collections.emptyMap()))
                .collect(Collectors.toList());
    }

    @Override
    public List<WebElement> filterByText(List<WebElement> webElements, String text) {
        return Collections.emptyList();
    }

    @Override
    public List<WebElement> filterByRegexp(List<WebElement> webElements, String regexp) {
        return Collections.emptyList();
    }

    @Override
    public List<WebElement> filterByNearby(List<WebElement> webElements, WebElement target) {
        return Collections.emptyList();
    }

    @Override
    public WebElement parentByCss(SearchContext element, String css) {
        return null;
    }
}
