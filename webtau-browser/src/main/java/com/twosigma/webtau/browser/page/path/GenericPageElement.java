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

package com.twosigma.webtau.browser.page.path;

import com.twosigma.webtau.browser.InjectedJavaScript;
import com.twosigma.webtau.browser.page.*;
import com.twosigma.webtau.browser.page.path.filter.ByNumberElementsFilter;
import com.twosigma.webtau.browser.page.path.filter.ByRegexpElementsFilter;
import com.twosigma.webtau.browser.page.path.filter.ByTextElementsFilter;
import com.twosigma.webtau.browser.page.path.finder.ByCssFinder;
import com.twosigma.webtau.browser.page.value.ElementValue;
import com.twosigma.webtau.browser.page.value.handlers.PageElementGetSetValueHandlers;
import com.twosigma.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.TO;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.stringValue;
import static com.twosigma.webtau.reporter.TestStep.createAndExecuteStep;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class GenericPageElement implements PageElement {
    private WebDriver driver;
    private final InjectedJavaScript injectedJavaScript;
    private ElementPath path;
    private final TokenizedMessage pathDescription;
    private ElementValue<String> elementValue;
    private ElementValue<Integer> countValue;

    public GenericPageElement(WebDriver driver, InjectedJavaScript injectedJavaScript, ElementPath path) {
        this.driver = driver;
        this.injectedJavaScript = injectedJavaScript;
        this.path = path;
        this.pathDescription = path.describe();
        this.elementValue = new ElementValue<>(this, "value", this::getUnderlyingValue);
        this.countValue = new ElementValue<>(this, "count", this::getNumberOfElements);
    }

    @Override
    public ElementValue<Integer> getCount() {
        return countValue;
    }

    @Override
    public TokenizedMessage describe() {
        return pathDescription;
    }

    @Override
    public void highlight() {
        injectedJavaScript.flashWebElements(findElements());
    }

    public void click() {
        execute(tokenizedMessage(action("clicking")).add(pathDescription),
                () -> tokenizedMessage(action("clicked")).add(pathDescription),
                () -> findElement().click());
    }

    public WebElement findElement() {
        List<WebElement> webElements = findElements();
        return webElements.get(0);
    }

    @Override
    public List<WebElement> findElements() {
        List<WebElement> webElements = path.find(driver);
        return webElements.isEmpty() ?
                Collections.singletonList(createNullElement()) :
                webElements;
    }

    @Override
    public ElementValue<String> elementValue() {
        return elementValue;
    }

    @Override
    public ElementValue<List<String>> elementValues() {
        return new ElementValue<>(this, "all values", this::extractValues);
    }

    @Override
    public void setValue(Object value) {
        execute(tokenizedMessage(action("setting value"), stringValue(value), TO).add(pathDescription),
                () -> tokenizedMessage(action("set value"), stringValue(value), TO).add(pathDescription),
                () -> staleFreeSetValueBasedOnType(value));
    }

    @Override
    public void sendKeys(String keys) {
        sendKeys(findElement(), keys);
    }

    @Override
    public void clear() {
        clear(findElement());
    }

    @Override
    public PageElement find(String css) {
        return find(new ByCssFinder(css));
    }

    @Override
    public PageElement find(ElementsFinder finder) {
        return withFinder(finder);
    }

    @Override
    public PageElement get(String text) {
        return withFilter(new ByTextElementsFilter(text));
    }

    @Override
    public PageElement get(int number) {
        return withFilter(new ByNumberElementsFilter(number));
    }

    @Override
    public PageElement get(Pattern regexp) {
        return withFilter(new ByRegexpElementsFilter(regexp));
    }

    @Override
    public boolean isVisible() {
        return handleStaleElement(() -> findElement().isDisplayed(), false);
    }

    @Override
    public boolean isEnabled() {
        return handleStaleElement(() -> findElement().isEnabled(), false);
    }

    @Override
    public String toString() {
        return path.toString();
    }

    private String getText() {
        return findElement().getText();
    }

    private String getTagName() {
        return findElement().getTagName();
    }

    private String getAttribute(String name) {
        return findElement().getAttribute(name);
    }

    private String getUnderlyingValue() {
        List<String> values = extractValues();
        return values.isEmpty() ? null : values.get(0);
    }

    private List<String> extractValues() {
        List<WebElement> elements = path.find(driver);
        List<Map<String, String>> elementsMeta = handleStaleElement(() -> injectedJavaScript.extractElementsMeta(elements),
                Collections.emptyList());

        if (elementsMeta.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();

        for (int idx = 0; idx < elements.size(); idx++) {
            HtmlNode htmlNode = new HtmlNode(elementsMeta.get(idx));
            WebElement webElement = elements.get(idx);

            result.add(handleStaleElement(() ->
                    PageElementGetSetValueHandlers.getValue(
                            htmlNode,
                            webElement), null));
        }

        return result;
    }

    private Integer getNumberOfElements() {
        List<WebElement> webElements = path.find(driver);
        return webElements.size();
    }

    private void staleFreeSetValueBasedOnType(Object value) {
        handleStaleElement(() -> setValueBasedOnType(value));
    }

    private void setValueBasedOnType(Object value) {
        HtmlNodeAndWebElement htmlNodeAndWebElement = findHtmlNodeAndWebElement();
        PageElementGetSetValueHandlers.setValue(this::execute, pathDescription,
                htmlNodeAndWebElement.htmlNode,
                htmlNodeAndWebElement.webElement,
                value);
    }

    private void clear(WebElement webElement) {
        execute(tokenizedMessage(action("clearing")).add(pathDescription),
                () -> tokenizedMessage(action("cleared")).add(pathDescription),
                webElement::clear);
    }

    private void sendKeys(WebElement webElement, String keys) {
        execute(tokenizedMessage(action("sending keys"), stringValue(keys), TO).add(pathDescription),
                () -> tokenizedMessage(action("sent keys"), stringValue(keys), TO).add(pathDescription),
                () -> webElement.sendKeys(keys));
    }

    private void execute(TokenizedMessage inProgressMessage,
                         Supplier<TokenizedMessage> completionMessageSupplier,
                         Runnable action) {
        createAndExecuteStep(this, inProgressMessage, completionMessageSupplier, action);
    }

    private PageElement withFilter(ElementsFilter filter) {
        ElementPath newPath = path.copy();
        newPath.addFilter(filter);

        return new GenericPageElement(driver, injectedJavaScript, newPath);
    }

    private PageElement withFinder(ElementsFinder finder) {
        ElementPath newPath = path.copy();
        newPath.addFinder(finder);

        return new GenericPageElement(driver, injectedJavaScript, newPath);
    }

    private HtmlNodeAndWebElement findHtmlNodeAndWebElement() {
        List<WebElement> elements = findElements();
        List<Map<String, String>> elementsMeta = injectedJavaScript.extractElementsMeta(elements);

        HtmlNode htmlNode = elementsMeta.isEmpty() ? HtmlNode.NULL : new HtmlNode(elementsMeta.get(0));
        WebElement webElement = elements.isEmpty() ? createNullElement() : elements.get(0);
        return new HtmlNodeAndWebElement(htmlNode, webElement);
    }

    private NullWebElement createNullElement() {
        return new NullWebElement(path.toString());
    }

    private static GenericElementType staleFreeElementType(WebElement element) {
        return handleStaleElement(() -> elementType(element), GenericElementType.STALE);
    }

    private static GenericElementType elementType(WebElement element) {
        String tag = element.getTagName().toUpperCase();
        String typeOrNull = element.getAttribute("type");
        String type = typeOrNull != null ? typeOrNull.toUpperCase() : "";

        switch (tag) {
            case "SELECT":
                return GenericElementType.SELECT;
            case "INPUT":
                return type.equals("DATE") ?
                        GenericElementType.INPUT_DATE:
                        GenericElementType.INPUT;
            case "TEXTAREA":
                return GenericElementType.TEXT_AREA;
            default:
                return GenericElementType.OTHER;
        }
    }

    static private <R> R handleStaleElement(Supplier<R> code, R valueInCaseOfStale) {
        try {
            return code.get();
        } catch (StaleElementReferenceException e) {
            return valueInCaseOfStale;
        }
    }

    static private void handleStaleElement(Runnable code) {
        try {
            code.run();
        } catch (StaleElementReferenceException e) {
            throw new RuntimeException("element is stale, consider using waitTo beVisible matcher to make sure component fully appeared");
        }
    }

    class HtmlNodeAndWebElement {
        private HtmlNode htmlNode;
        private WebElement webElement;

        HtmlNodeAndWebElement(HtmlNode htmlNode, WebElement webElement) {
            this.htmlNode = htmlNode;
            this.webElement = webElement;
        }
    }
}
