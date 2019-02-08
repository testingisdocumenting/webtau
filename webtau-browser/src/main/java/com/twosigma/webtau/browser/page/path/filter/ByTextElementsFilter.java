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

import com.twosigma.webtau.browser.InjectedJavaScript;
import com.twosigma.webtau.browser.page.path.ElementsFilter;
import com.twosigma.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.selectorType;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.selectorValue;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;
import static java.util.stream.Collectors.toList;

public class ByTextElementsFilter implements ElementsFilter {
    private final InjectedJavaScript injectedJavaScript;
    private String text;

    public ByTextElementsFilter(InjectedJavaScript injectedJavaScript, String text) {
        this.injectedJavaScript = injectedJavaScript;
        this.text = text;
    }

    @Override
    public List<WebElement> filter(List<WebElement> original) {
        return injectedJavaScript.filterByText(original, text);
    }

    @Override
    public TokenizedMessage description() {
        return tokenizedMessage(selectorType("element(s) with text"), selectorValue(text));
    }
}
