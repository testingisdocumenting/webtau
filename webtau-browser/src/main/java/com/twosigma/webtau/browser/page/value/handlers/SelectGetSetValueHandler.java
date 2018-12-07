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

package com.twosigma.webtau.browser.page.value.handlers;

import com.twosigma.webtau.browser.page.HtmlNode;
import com.twosigma.webtau.browser.page.PageElementStepExecutor;
import com.twosigma.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class SelectGetSetValueHandler implements PageElementGetSetValueHandler {
    @Override
    public boolean handles(HtmlNode htmlNode, WebElement webElement) {
        return htmlNode.getTagName().equals("select");
    }

    @Override
    public void setValue(PageElementStepExecutor stepExecutor,
                         TokenizedMessage pathDescription,
                         HtmlNode htmlNode,
                         WebElement webElement,
                         Object value) {

        stepExecutor.execute(tokenizedMessage(action("selecting drop down option"), stringValue(value)).add(pathDescription),
                () -> tokenizedMessage(action("selected drop down option"), stringValue(value)).add(pathDescription),
                () -> {
                    Select select = new Select(webElement);
                    select.selectByValue(value.toString());
                });
    }

    @Override
    public String getValue(HtmlNode htmlNode, WebElement webElement) {
        Select select = new Select(webElement);
        return select.getFirstSelectedOption().getText();
    }
}
