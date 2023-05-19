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

import org.testingisdocumenting.webtau.browser.page.HtmlNode;
import org.testingisdocumenting.webtau.browser.page.HtmlNodeAndWebElementList;
import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.browser.page.PageElementStepExecutor;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

public class DefaultGetSetValueHandler implements PageElementGetSetValueHandler {
    @Override
    public boolean handles(HtmlNodeAndWebElementList htmlNodeAndWebElements, PageElement pageElement) {
        return true;
    }

    @Override
    public void setValue(PageElementStepExecutor stepExecutor,
                         TokenizedMessage pathDescription,
                         HtmlNodeAndWebElementList htmlNodeAndWebElements,
                         PageElement pageElement,
                         Object value,
                         boolean noLog) {
        pageElement.clear();
        if (noLog) {
            pageElement.sendKeysNoLog(value.toString());
        } else {
            pageElement.sendKeys(value.toString());
        }
    }

    @Override
    public Object getValue(HtmlNodeAndWebElementList htmlNodeAndWebElements, PageElement pageElement, int idx) {
        HtmlNode htmlNode = htmlNodeAndWebElements.firstHtmlNode();
        return htmlNode.tagName().equalsIgnoreCase("input") || htmlNode.tagName().equalsIgnoreCase("textarea") ?
                htmlNode.value():
                pageElement.getText();
    }
}
