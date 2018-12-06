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
import com.twosigma.webtau.utils.ServiceLoaderUtils;
import org.openqa.selenium.WebElement;

import java.util.LinkedHashSet;
import java.util.Set;

public class PageElementGetSetValueHandlers {
    private static final Set<PageElementGetSetValueHandler> handlers = discoverHandlers();

    public static void add(PageElementGetSetValueHandler handler) {
        handlers.add(handler);
    }

    public static void remove(PageElementGetSetValueHandler handler) {
        handlers.remove(handler);
    }

    public static void setValue(PageElementStepExecutor stepExecutor,
                                TokenizedMessage pathDescription,
                                HtmlNode htmlNode,
                                WebElement webElement,
                                Object value) {
        PageElementGetSetValueHandler handler = findHandler(htmlNode, webElement);
        handler.setValue(stepExecutor, pathDescription, htmlNode, webElement, value);
    }

    public static String getValue(HtmlNode htmlNode, WebElement webElement) {
        PageElementGetSetValueHandler handler = findHandler(htmlNode, webElement);
        return handler.getValue(htmlNode, webElement);
    }

    private static PageElementGetSetValueHandler findHandler(HtmlNode htmlNode, WebElement webElement) {
        return handlers.stream().
                filter(h -> h.handles(htmlNode, webElement)).findFirst().
                orElseThrow(() -> noHandlerFound(htmlNode));
    }

    private static RuntimeException noHandlerFound(HtmlNode htmlNode) {
        return new RuntimeException("no PageElementGetSetValueHandler handler found for " + htmlNode);
    }

    private static Set<PageElementGetSetValueHandler> discoverHandlers() {
        LinkedHashSet<PageElementGetSetValueHandler> result =
                new LinkedHashSet<>(ServiceLoaderUtils.load(PageElementGetSetValueHandler.class));
        result.add(new DefaultGetSetValueHandler());

        return result;
    }
}
