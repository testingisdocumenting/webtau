/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.page.value.handlers;

import org.testingisdocumenting.webtau.browser.page.HtmlNode;
import org.testingisdocumenting.webtau.browser.page.NullWebElement;
import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.browser.page.PageElementStepExecutor;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.stringValue;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class SelectGetSetValueHandler implements PageElementGetSetValueHandler {
    @Override
    public boolean handles(HtmlNode htmlNode, PageElement pageElement) {
        return htmlNode.getTagName().equals("select");
    }

    @Override
    public void setValue(PageElementStepExecutor stepExecutor,
                         TokenizedMessage pathDescription,
                         HtmlNode htmlNode,
                         PageElement pageElement,
                         Object value) {

        stepExecutor.execute(tokenizedMessage(action("selecting drop down option"), stringValue(value)).add(pathDescription),
                () -> tokenizedMessage(action("selected drop down option"), stringValue(value)).add(pathDescription),
                () -> {
                    Select select = new Select(pageElement.findElement());
                    select.selectByValue(value.toString());
                });
    }

    @Override
    public Object getValue(HtmlNode htmlNode, PageElement pageElement) {
        WebElement webElement = pageElement.findElement();
        if (webElement instanceof NullWebElement) {
            return null;
        }

        Select select = new Select(webElement);
        return select.getFirstSelectedOption().getText();
    }
}
