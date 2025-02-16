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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.testingisdocumenting.webtau.browser.page.PageElement;

import java.util.function.Function;

public class ByRelativeAbovePageElementFinder extends ByRelativePageElementFinder {
    public ByRelativeAbovePageElementFinder(PageElement target) {
        super(target, "above");
    }

    protected Function<WebElement, RelativeLocator.RelativeBy> direction(RelativeLocator.RelativeBy chainStart) {
        return chainStart::above;
    }
}
