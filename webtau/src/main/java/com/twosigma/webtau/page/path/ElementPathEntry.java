/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.page.path;

import com.twosigma.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.COMMA;

class ElementPathEntry {
    private ElementsFinder finder;
    private List<ElementsFilter> filters;

    ElementPathEntry(ElementsFinder finder) {
        this.finder = finder;
        this.filters = new ArrayList<>();
    }

    void addFilter(ElementsFilter filter) {
        filters.add(filter);
    }

    ElementPathEntry copy() {
        ElementPathEntry copy = new ElementPathEntry(finder);
        copy.filters = new ArrayList<>(filters);

        return copy;
    }

    List<WebElement> find(WebDriver driver, WebElement parent) {
        List<WebElement> elements = finder.find(driver);
        if (elements.isEmpty()) {
            return elements;
        }

        List<WebElement> filtered = elements;
        for (ElementsFilter filter : filters) {
            filtered = filter.filter(filtered);
            if (filtered.isEmpty()) {
                return filtered;
            }
        }

        return filtered;
    }

    /**
     * @param isFirst is this the first entry in the path
     * @return tokenized message
     */
    public TokenizedMessage description(boolean isFirst) {
        TokenizedMessage description = finder.description(isFirst);
        filters.forEach((f) -> {
            description.add(COMMA);
            description.add(f.description());
        });

        return description;
    }

    @Override
    public String toString() {
        return finder.description(false).toString();
    }
}
