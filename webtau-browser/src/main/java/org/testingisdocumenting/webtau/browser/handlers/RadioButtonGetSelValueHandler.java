/*
 * Copyright 2021 webtau maintainers
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

import java.util.List;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;

public class RadioButtonGetSelValueHandler implements PageElementGetSetValueHandler {
    @Override
    public boolean handles(HtmlNodeAndWebElementList htmlNodeAndWebElements, PageElement pageElement) {
        HtmlNode htmlNode = htmlNodeAndWebElements.firstHtmlNode();
        return htmlNode.getType().equalsIgnoreCase("radio");
    }

    @Override
    public void setValue(PageElementStepExecutor stepExecutor,
                         TokenizedMessage pathDescription,
                         HtmlNodeAndWebElementList htmlNodeAndWebElements,
                         PageElement pageElement,
                         Object value) {
        stepExecutor.execute(tokenizedMessage(action("setting radio button value to"), stringValue(value)).add(pathDescription),
                () -> tokenizedMessage(action("set radio button value to"), stringValue(value)).add(pathDescription),
                () -> {
                    List<String> values = htmlNodeAndWebElements.n
                            .map(nw -> nw.getHtmlNode().getValue())
                            .collect(Collectors.toList());
                    int idx = values.indexOf(value.toString());
                    if (idx == -1) {
                        throw new RuntimeException("no value found \"" + value + "\", available values: " +
                                String.join(", ", values));
                    }

//                    pageElement.
//                    return willClick;
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
