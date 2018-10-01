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

import com.twosigma.webtau.browser.page.ElementValue;
import com.twosigma.webtau.browser.page.NullWebElement;
import com.twosigma.webtau.browser.page.PageElement;
import com.twosigma.webtau.browser.page.path.filter.ByNumberElementsFilter;
import com.twosigma.webtau.browser.page.path.filter.ByRegexpElementsFilter;
import com.twosigma.webtau.browser.page.path.filter.ByTextElementsFilter;
import com.twosigma.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.TO;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.stringValue;
import static com.twosigma.webtau.reporter.TestStep.createAndExecuteStep;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class GenericPageElement implements PageElement {
    private WebDriver driver;
    private ElementPath path;
    private final TokenizedMessage pathDescription;
    private ElementValue<String> elementValue;
    private ElementValue<Integer> countValue;

    public GenericPageElement(WebDriver driver, ElementPath path) {
        this.driver = driver;
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
                Collections.singletonList(createNullElement()):
                webElements;
    }

    @Override
    public ElementValue<String> elementValue() {
        return elementValue;
    }

    @Override
    public ElementValue<List<String>> elementValues() {
        return new ElementValue<>(this, "all values", this::getUnderlyingValues);
    }

    @Override
    public void setValue(Object value) {
        execute(tokenizedMessage(action("setting value"), stringValue(value), TO).add(pathDescription),
                () -> tokenizedMessage(action("set value"), stringValue(value), TO).add(pathDescription),
                () -> setValueBasedOnType(value));
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
        return findElement().isDisplayed();
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
        return extractValue(findElement());
    }

    private List<String> getUnderlyingValues() {
        List<WebElement> webElements = findElements();
        return webElements.stream()
                .map(this::extractValue)
                .collect(Collectors.toList());
    }

    private String extractValue(WebElement webElement) {
        GenericElementType type = elementType(webElement);

        switch (type) {
            case INPUT:
            case INPUT_DATE:
            case TEXT_AREA:
                return webElement.getAttribute("value");
            case SELECT:
                return extractSelectOptionValue(webElement);
            default:
                return webElement.getText();
        }
    }

    private String extractSelectOptionValue(WebElement webElement) {
        Select select = new Select(webElement);
        return select.getFirstSelectedOption().getText();
    }

    private Integer getNumberOfElements() {
        List<WebElement> webElements = path.find(driver);
        return webElements.size();
    }

    private void setValueBasedOnType(Object value) {
        WebElement element = findElement();

        GenericElementType type = elementType(element);
        switch (type) {
            case SELECT:
                setSelectValue(element, value);
                break;
            case INPUT_DATE:
                setInputDateValue(element, value);
                break;
            default:
                setRegularValue(element, value);
                break;
        }
    }

    private void setSelectValue(WebElement webElement, Object value) {
        Select select = new Select(webElement);
        select.selectByValue(value.toString());
    }

    private void setRegularValue(WebElement webElement, Object value) {
        clear(webElement);
        sendKeys(webElement, value.toString());
    }

    private void setInputDateValue(WebElement webElement, Object value) {
        sendKeys(webElement, value.toString());
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

        return new GenericPageElement(driver, newPath);
    }

    private NullWebElement createNullElement() {
        return new NullWebElement(path.toString());
    }

    static private GenericElementType elementType(WebElement element) {
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
}
