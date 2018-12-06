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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class PageElementGetSetValueHandlers {
    private static final Set<PageElementGetSetValueHandler> discovered =
            ServiceLoaderUtils.load(PageElementGetSetValueHandler.class);
    private static final List<PageElementGetSetValueHandler> added = new ArrayList<>();

    private static final PageElementGetSetValueHandler defaultHandler = new DefaultGetSetValueHandler();

    public static void add(PageElementGetSetValueHandler handler) {
        added.add(handler);
    }

    public static void remove(PageElementGetSetValueHandler handler) {
        added.remove(handler);
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
        return discoverHandlers().
                filter(h -> h.handles(htmlNode, webElement)).findFirst().
                orElseThrow(() -> noHandlerFound(htmlNode));
    }

    private static RuntimeException noHandlerFound(HtmlNode htmlNode) {
        return new RuntimeException("no PageElementGetSetValueHandler handler found for " + htmlNode);
    }

    private static Stream<PageElementGetSetValueHandler> discoverHandlers() {
        return Stream.concat(
                Stream.concat(added.stream(), discovered.stream()),
                Stream.of(defaultHandler));
    }
}
