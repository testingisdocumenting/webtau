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

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.browser.page.path.PageElementFinder;
import org.testingisdocumenting.webtau.browser.page.path.PageElementPathSearchContext;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.List;
import java.util.function.Function;

import static org.testingisdocumenting.webtau.WebTauCore.*;

abstract public class ByRelativePageElementFinder implements PageElementFinder {
    private final PageElement target;
    private final String classifier;

    public ByRelativePageElementFinder(PageElement target, String classifier) {
        this.target = target;
        this.classifier = classifier;
    }

    abstract protected Function<WebElement, RelativeLocator.RelativeBy> direction(RelativeLocator.RelativeBy chainStart);

    @Override
    public List<WebElement> find(PageElementPathSearchContext parent) {
        RelativeLocator.RelativeBy chainStart = RelativeLocator.with(new ByAdapter(parent.parent()));
        RelativeLocator.RelativeBy relativeBy = direction(chainStart).apply(target.findElement());
        return parent.searchContext().findElements(relativeBy);
    }

    @Override
    public TokenizedMessage description(boolean isFirst) {
        return tokenizedMessage().selectorType(classifier).add(target.describe());
    }

    static private class ByAdapter extends By {
        private final WebElement parent;

        public ByAdapter(WebElement parent) {
            this.parent = parent;
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            return List.of(parent);
        }
    }
}
