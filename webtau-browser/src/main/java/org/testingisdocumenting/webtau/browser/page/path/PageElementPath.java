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

package org.testingisdocumenting.webtau.browser.page.path;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testingisdocumenting.webtau.browser.page.path.finder.ByCssPageElementFinder;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.*;

public class PageElementPath {
    private List<PageElementPathEntry> entries;

    public PageElementPath() {
        entries = new ArrayList<>();
    }

    public void addFinder(PageElementFinder finder) {
        PageElementPathEntry entry = new PageElementPathEntry(finder);
        entries.add(entry);
    }

    public void addFilter(PageElementsFilter filter) {
        if (entries.isEmpty()) {
            throw new RuntimeException("add a finder first");
        }

        entries.get(entries.size() - 1).addFilter(filter);
    }

    public PageElementPath copy() {
        PageElementPath copy = new PageElementPath();
        copy.entries = entries.stream().map(PageElementPathEntry::copy).collect(toList());

        return copy;
    }

    public static PageElementPath css(String cssSelector) {
        PageElementPath path = new PageElementPath();
        path.addFinder(new ByCssPageElementFinder(cssSelector));

        return path;
    }

    public List<WebElement> find(WebDriver driver) {
        var context = new PageElementPathSearchContext(driver, null);

        List<WebElement> webElements = Collections.emptyList();
        for (PageElementPathEntry entry : entries) {
            webElements = entry.find(context);
            if (webElements.isEmpty()) {
                return webElements;
            }

            context = PageElementPathSearchContext.fromElement(webElements.get(0));
        }

        return webElements;
    }

    public TokenizedMessage describe() {
        TokenizedMessage message = new TokenizedMessage();

        int i = 0;
        int lastIdx = entries.size() - 1;
        for (PageElementPathEntry entry : entries) {
            message.add(entry.description(i == 0));
            if (i != lastIdx) {
                message.comma();
            }

            i++;
        }

        return message;
    }

    @Override
    public String toString() {
        return describe().toString();
    }
}
