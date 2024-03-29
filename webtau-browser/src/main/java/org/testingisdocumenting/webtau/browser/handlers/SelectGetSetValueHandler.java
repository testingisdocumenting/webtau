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

package org.testingisdocumenting.webtau.browser.handlers;

import org.openqa.selenium.NoSuchElementException;
import org.testingisdocumenting.webtau.browser.page.*;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static org.testingisdocumenting.webtau.WebTauCore.tokenizedMessage;

public class SelectGetSetValueHandler implements PageElementGetSetValueHandler {
    @Override
    public boolean handles(HtmlNodeAndWebElementList htmlNodeAndWebElements, PageElement pageElement) {
        HtmlNode htmlNode = htmlNodeAndWebElements.firstHtmlNode();
        return htmlNode.tagName().equalsIgnoreCase("select");
    }

    @Override
    public void setValue(PageElementStepExecutor stepExecutor,
                         TokenizedMessage pathDescription,
                         HtmlNodeAndWebElementList htmlNodeAndWebElements,
                         PageElement pageElement,
                         Object value,
                         boolean noLog) {
        if (noLog) {
            throw new IllegalArgumentException("noLog option is not supported for checkboxes");
        }

        stepExecutor.execute(tokenizedMessage().action("selecting drop down option").string(value).add(pathDescription),
                () -> tokenizedMessage().action("selected drop down option").string(value).add(pathDescription),
                () -> {
                    WebElement element = pageElement.findElement();
                    Select select = new Select(element);
                    try {
                        select.selectByValue(value.toString());
                        return;
                    } catch (NoSuchElementException ignored) {
                    }

                    try {
                        select.selectByVisibleText(value.toString());
                    } catch (NoSuchElementException ignored) {
                        throw new NoSuchElementException("Cannot locate option with either value or visible text: " + value);
                    }
                });
    }

    @Override
    public Object getValue(HtmlNodeAndWebElementList htmlNodeAndWebElements, PageElement pageElement, int idx) {
        WebElement webElement = pageElement.findElement();
        if (webElement instanceof NullWebElement) {
            return null;
        }

        Select select = new Select(webElement);
        return select.getFirstSelectedOption().getText();
    }
}
