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

package com.twosigma.webtau.browser.page.path.filter;

import com.twosigma.webtau.browser.page.path.ElementsFilter;
import com.twosigma.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;

import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.selectorType;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.selectorValue;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class ByNumberElementsFilter implements ElementsFilter {
    private int number;

    public ByNumberElementsFilter(int number) {
        this.number = number;
    }

    @Override
    public List<WebElement> filter(List<WebElement> original) {
        return (number > 0 && number <= original.size()) ?
                Collections.singletonList(original.get(number - 1)):
                Collections.emptyList();
    }

    @Override
    public TokenizedMessage description() {
        return tokenizedMessage(selectorType("element number"), selectorValue(String.valueOf(number)));
    }
}
