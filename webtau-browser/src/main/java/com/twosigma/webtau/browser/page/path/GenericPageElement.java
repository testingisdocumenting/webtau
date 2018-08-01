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

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.TO;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.stringValue;
import static com.twosigma.webtau.reporter.TestStep.createAndExecuteStep;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class GenericPageElement implements PageElement {
    private boolean isMultipleElements;
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

    public PageElement all() {
        GenericPageElement element = new GenericPageElement(driver, path);
        element.isMultipleElements = true;

        return element;
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
        List<WebElement> webElements = path.find(driver);
        return webElements.isEmpty() ?
                new NullWebElement(path.toString()) :
                webElements.get(0);
    }

    @Override
    public ElementValue<String> elementValue() {
        return elementValue;
    }

    @Override
    public void setValue(Object value) {
        execute(tokenizedMessage(action("setting value"), stringValue(value), TO).add(pathDescription),
                () -> tokenizedMessage(action("set value"), stringValue(value), TO).add(pathDescription),
                () -> {
                    clear();
                    sendKeys(value.toString());
                });
    }

    @Override
    public void sendKeys(String keys) {
        execute(tokenizedMessage(action("sending keys"), stringValue(keys), TO).add(pathDescription),
                () -> tokenizedMessage(action("sent value"), stringValue(keys), TO).add(pathDescription),
                () -> findElement().sendKeys(keys));
    }

    @Override
    public void clear() {
        execute(tokenizedMessage(action("clearing")).add(pathDescription),
                () -> tokenizedMessage(action("cleared")).add(pathDescription),
                () -> findElement().clear());
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
        WebElement webElement = findElement();
        String tagName = webElement.getTagName().toUpperCase();
        return (tagName.equals("INPUT") || tagName.equals("TEXTAREA")) ?
                webElement.getAttribute("value") : webElement.getText();
    }

    private Integer getNumberOfElements() {
        List<WebElement> webElements = path.find(driver);
        return webElements.size();
    }

    @Override
    public String toString() {
        return path.toString();
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
}
