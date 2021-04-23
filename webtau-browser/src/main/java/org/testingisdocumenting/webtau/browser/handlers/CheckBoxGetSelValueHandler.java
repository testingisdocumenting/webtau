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
import org.testingisdocumenting.webtau.browser.page.*;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;

public class CheckBoxGetSelValueHandler implements PageElementGetSetValueHandler {
    @Override
    public boolean handles(HtmlNodeAndWebElementList htmlNodeAndWebElements, PageElement pageElement) {
        HtmlNode htmlNode = htmlNodeAndWebElements.firstHtmlNode();
        return htmlNode.getType().equalsIgnoreCase("checkbox");
    }

    @Override
    public void setValue(PageElementStepExecutor stepExecutor,
                         TokenizedMessage pathDescription,
                         HtmlNodeAndWebElementList htmlNodeAndWebElements,
                         PageElement pageElement,
                         Object value) {
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
    public Boolean getValue(HtmlNodeAndWebElementList htmlNodeAndWebElements, PageElement pageElement, int idx) {
        WebElement webElement = pageElement.findElement();
        if (webElement instanceof NullWebElement) {
            return null;
        }

        return webElement.isSelected();
    }
}
