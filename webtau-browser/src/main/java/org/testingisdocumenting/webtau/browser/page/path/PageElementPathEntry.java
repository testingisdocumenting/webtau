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

import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

class PageElementPathEntry {
    private final PageElementFinder finder;
    private List<PageElementsFilter> filters;

    PageElementPathEntry(PageElementFinder finder) {
        this.finder = finder;
        this.filters = new ArrayList<>();
    }

    void addFilter(PageElementsFilter filter) {
        filters.add(filter);
    }

    PageElementPathEntry copy() {
        PageElementPathEntry copy = new PageElementPathEntry(finder);
        copy.filters = new ArrayList<>(filters);

        return copy;
    }

    List<WebElement> find(PageElementPathSearchContext searchContext) {
        List<WebElement> elements = finder.find(searchContext);
        if (elements.isEmpty()) {
            return elements;
        }

        List<WebElement> filtered = elements;
        for (PageElementsFilter filter : filters) {
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
        filters.forEach((f) -> description.comma().add(f.description()));

        return description;
    }

    @Override
    public String toString() {
        return finder.description(false).toString();
    }
}
