/*
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

package com.twosigma.webtau.browser;

import com.twosigma.webtau.utils.ResourceUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

public class BrowserInjectedJavaScript implements AdditionalBrowserInteractions {
    private final String injectionScript = ResourceUtils.textContent("browser/injection.js");

    private JavascriptExecutor javascriptExecutor;

    BrowserInjectedJavaScript(JavascriptExecutor javascriptExecutor) {
        this.javascriptExecutor = javascriptExecutor;
    }

    @Override
    public void flashWebElements(List<WebElement> webElements) {
        injectScript();
        javascriptExecutor.executeScript(oneArgFunc("flashElements"), webElements);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, ?>> extractElementsMeta(List<WebElement> webElements) {
        injectScript();
        Object result = javascriptExecutor.executeScript(returnOneArgFunc("elementsMeta"), webElements);
        return (List<Map<String, ?>>) result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<WebElement> filterByText(List<WebElement> webElements, String text) {
        injectScript();
        return (List<WebElement>) javascriptExecutor.executeScript(returnTwoArgFunc("filterByText"),
                webElements, text);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<WebElement> filterByRegexp(List<WebElement> webElements, String regexp) {
        injectScript();
        return (List<WebElement>) javascriptExecutor.executeScript(returnTwoArgFunc("filterByRegexp"),
                webElements, regexp);
    }

    private static String oneArgFunc(String name) {
        return func(name) + "(arguments[0])";
    }

    private static String returnOneArgFunc(String name) {
        return "return " + oneArgFunc(name);
    }

    private static String twoArgFunc(String name) {
        return func(name) + "(arguments[0], arguments[1])";
    }

    private static String returnTwoArgFunc(String name) {
        return "return " + twoArgFunc(name);
    }

    private static String func(String name) {
        return "window._webtau." + name;
    }

    private void injectScript() {
        javascriptExecutor.executeScript(injectionScript);
    }
}
