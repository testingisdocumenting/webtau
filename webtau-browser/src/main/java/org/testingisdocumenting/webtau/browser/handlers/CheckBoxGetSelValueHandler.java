/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.handlers;

import org.openqa.selenium.WebElement;
import org.testingisdocumenting.webtau.browser.page.HtmlNode;
import org.testingisdocumenting.webtau.browser.page.NullWebElement;
import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.browser.page.PageElementStepExecutor;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.stringValue;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class CheckBoxGetSelValueHandler implements PageElementGetSetValueHandler {
    @Override
    public boolean handles(HtmlNode htmlNode, PageElement pageElement) {
        return htmlNode.getAttributes().getOrDefault("type", "").toLowerCase().equals("checkbox");
    }

    @Override
    public void setValue(PageElementStepExecutor stepExecutor, TokenizedMessage pathDescription, HtmlNode htmlNode, PageElement pageElement, Object value) {
        if (!(value instanceof Boolean)) {
            throw new IllegalArgumentException("setValue arg for checkbox must be true or false");
        }

        stepExecutor.execute(tokenizedMessage(action("setting checkbox value to"), stringValue(value)).add(pathDescription),
                (willClickObj) -> {
                    Boolean willClick = (Boolean) willClickObj;

                    return willClick ?
                            tokenizedMessage(action("set checkbox value to"), stringValue(value)).add(pathDescription):
                            tokenizedMessage(action("checkbox was already set to"), stringValue(value)).add(pathDescription);
                },
                () -> {
                    boolean needToBeSelected = (boolean) value;
                    WebElement webElement = pageElement.findElement();

                    boolean willClick = (!webElement.isSelected() && needToBeSelected) ||
                            (webElement.isSelected() && !needToBeSelected);

                    if (willClick) {
                        webElement.click();
                    }

                    return willClick;
                });
    }

    @Override
    public Boolean getValue(HtmlNode htmlNode, PageElement pageElement) {
        WebElement webElement = pageElement.findElement();
        if (webElement instanceof NullWebElement) {
            return null;
        }

        return webElement.isSelected();
    }
}
