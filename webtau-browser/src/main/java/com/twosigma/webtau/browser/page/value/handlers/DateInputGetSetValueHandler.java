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

package com.twosigma.webtau.browser.page.value.handlers;

import com.twosigma.webtau.browser.driver.CurrentWebDriver;
import com.twosigma.webtau.browser.page.HtmlNode;
import com.twosigma.webtau.browser.page.PageElement;
import com.twosigma.webtau.browser.page.PageElementStepExecutor;
import com.twosigma.webtau.reporter.TokenizedMessage;

import java.time.LocalDate;

public class DateInputGetSetValueHandler implements PageElementGetSetValueHandler {
    private final CurrentWebDriver driver = CurrentWebDriver.INSTANCE;

    @Override
    public boolean handles(HtmlNode htmlNode, PageElement pageElement) {
        return htmlNode.getTagName().equals("input") &&
                htmlNode.getAttributes().getOrDefault("type", "").toLowerCase().equals("date");
    }

    @Override
    public void setValue(PageElementStepExecutor stepExecutor,
                         TokenizedMessage pathDescription,
                         HtmlNode htmlNode,
                         PageElement pageElement,
                         Object value) {
        LocalDate localDate = LocalDate.parse(value.toString());
        Object javaScriptRenderedDate = driver.executeScript(
                "return new Date(arguments[0], arguments[1], arguments[2]).toLocaleDateString()",
                localDate.getYear(), localDate.getMonth().getValue() - 1, localDate.getDayOfMonth());
        pageElement.sendKeys(javaScriptRenderedDate.toString());
    }

    @Override
    public String getValue(HtmlNode htmlNode, PageElement pageElement) {
        return htmlNode.getValue();
    }
}
