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

package org.testingisdocumenting.webtau.browser.page.path;

import org.testingisdocumenting.webtau.browser.page.path.finder.ByCssFinder;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.COMMA;
import static java.util.stream.Collectors.toList;

public class ElementPath {
    private List<ElementPathEntry> entries;

    public ElementPath() {
        entries = new ArrayList<>();
    }

    public void addFinder(ElementsFinder finder) {
        ElementPathEntry entry = new ElementPathEntry(finder);
        entries.add(entry);
    }

    public void addFilter(ElementsFilter filter) {
        if (entries.isEmpty()) {
            throw new RuntimeException("add a finder first");
        }

        entries.get(entries.size() - 1).addFilter(filter);
    }

    public ElementPath copy() {
        ElementPath copy = new ElementPath();
        copy.entries = entries.stream().map(ElementPathEntry::copy).collect(toList());

        return copy;
    }

    public static ElementPath css(String cssSelector) {
        ElementPath path = new ElementPath();
        path.addFinder(new ByCssFinder(cssSelector));

        return path;
    }

    public List<WebElement> find(WebDriver driver) {
        SearchContext root = driver;

        List<WebElement> webElements = Collections.emptyList();
        for (ElementPathEntry entry : entries) {
            webElements = entry.find(root);
            if (webElements.isEmpty()) {
                return webElements;
            }

            root = webElements.get(0);
        }

        return webElements;
    }

    public TokenizedMessage describe() {
        TokenizedMessage message = new TokenizedMessage();

        int i = 0;
        int lastIdx = entries.size() - 1;
        for (ElementPathEntry entry : entries) {
            message.add(entry.description(i == 0));
            if (i != lastIdx) {
                message.add(COMMA);
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
