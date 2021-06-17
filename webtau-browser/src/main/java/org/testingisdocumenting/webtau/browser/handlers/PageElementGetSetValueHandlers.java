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
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PageElementGetSetValueHandlers {
    private static final List<PageElementGetSetValueHandler> discovered =
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
                                HtmlNodeAndWebElementList htmlNodeAndWebElements,
                                PageElement pageElement,
                                Object value) {
        PageElementGetSetValueHandler handler = findHandler(htmlNodeAndWebElements, pageElement);
        handler.setValue(stepExecutor, pathDescription, htmlNodeAndWebElements, pageElement, value);
    }

    public static Object getValue(HtmlNodeAndWebElementList htmlNodeAndWebElements, PageElement pageElement, int idx) {
        PageElementGetSetValueHandler handler = findHandler(htmlNodeAndWebElements, pageElement);
        return handler.getValue(htmlNodeAndWebElements, pageElement, idx);
    }

    private static PageElementGetSetValueHandler findHandler(HtmlNodeAndWebElementList htmlNodeAndWebElements, PageElement pageElement) {
        if (htmlNodeAndWebElements.isEmpty()) {
            throw new RuntimeException("no elements found");
        }

        return discoverHandlers().
                filter(h -> h.handles(htmlNodeAndWebElements, pageElement)).findFirst().
                orElseThrow(() -> noHandlerFound(htmlNodeAndWebElements));
    }

    private static RuntimeException noHandlerFound(HtmlNodeAndWebElementList htmlNodeAndWebElements) {
        return new RuntimeException("no PageElementGetSetValueHandler handler found for:\n" +
                htmlNodeAndWebElements.nodesStream().map(HtmlNode::toString).collect(Collectors.joining("\n")));
    }

    private static Stream<PageElementGetSetValueHandler> discoverHandlers() {
        return Stream.concat(
                Stream.concat(added.stream(), discovered.stream()),
                Stream.of(defaultHandler));
    }
}
