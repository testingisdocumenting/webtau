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

package org.testingisdocumenting.webtau.browser.page.path;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testingisdocumenting.webtau.browser.AdditionalBrowserInteractions;
import org.testingisdocumenting.webtau.browser.BrowserConfig;
import org.testingisdocumenting.webtau.browser.page.*;
import org.testingisdocumenting.webtau.browser.page.path.filter.ByNumberElementsFilter;
import org.testingisdocumenting.webtau.browser.page.path.filter.ByRegexpElementsFilter;
import org.testingisdocumenting.webtau.browser.page.path.filter.ByTextElementsFilter;
import org.testingisdocumenting.webtau.browser.page.path.finder.ByCssFinder;
import org.testingisdocumenting.webtau.browser.page.PageElementValue;
import org.testingisdocumenting.webtau.browser.handlers.PageElementGetSetValueHandlers;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.createAndExecuteStep;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class GenericPageElement implements PageElement {
    private final WebDriver driver;
    private final AdditionalBrowserInteractions additionalBrowserInteractions;
    private final ElementPath path;
    private final TokenizedMessage pathDescription;
    private final PageElementValue<Object> elementValue;
    private final PageElementValue<Integer> countValue;

    public GenericPageElement(WebDriver driver, AdditionalBrowserInteractions additionalBrowserInteractions, ElementPath path) {
        this.driver = driver;
        this.additionalBrowserInteractions = additionalBrowserInteractions;
        this.path = path;
        this.pathDescription = path.describe();
        this.elementValue = new PageElementValue<>(this, "value", this::getUnderlyingValue);
        this.countValue = new PageElementValue<>(this, "count", this::getNumberOfElements);
    }

    @Override
    public PageElementValue<Integer> getCount() {
        return countValue;
    }

    @Override
    public ActualPath actualPath() {
        return createActualPath("pageElement");
    }

    @Override
    public TokenizedMessage describe() {
        return pathDescription;
    }

    @Override
    public void highlight() {
        additionalBrowserInteractions.flashWebElements(findElements());
    }

    public void click() {
        execute(tokenizedMessage(action("clicking")).add(pathDescription),
                () -> tokenizedMessage(action("clicked")).add(pathDescription),
                () -> findElement().click());
    }

    @Override
    public void shiftClick() {
        clickWithKey("shift", Keys.SHIFT);
    }

    @Override
    public void controlClick() {
        clickWithKey("control", Keys.CONTROL);
    }

    @Override
    public void commandClick() {
        clickWithKey("command", Keys.COMMAND);
    }

    @Override
    public void altClick() {
        clickWithKey("alt", Keys.ALT);
    }

    @Override
    public void rightClick() {
        execute(tokenizedMessage(action("right clicking")).add(pathDescription),
                () -> tokenizedMessage(action("right clicked")).add(pathDescription),
                () -> performActions("right click", Actions::contextClick));
    }

    @Override
    public void doubleClick() {
        execute(tokenizedMessage(action("double clicking")).add(pathDescription),
                () -> tokenizedMessage(action("double clicked")).add(pathDescription),
                () -> performActions("double click", Actions::doubleClick));
    }

    @Override
    public void hover() {
        execute(tokenizedMessage(action("moving mouse over")).add(pathDescription),
                () -> tokenizedMessage(action("moved mouse over")).add(pathDescription),
                () -> performActions("hover", Actions::moveToElement));
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
    public PageElementValue<Object> elementValue() {
        return elementValue;
    }

    @Override
    public PageElementValue<List<Object>> elementValues() {
        return new PageElementValue<>(this, "all values", this::extractValues);
    }

    @Override
    public void setValue(Object value) {
        execute(tokenizedMessage(action("setting value"), stringValue(value), TO).add(pathDescription),
                () -> tokenizedMessage(action("set value"), stringValue(value), TO).add(pathDescription),
                () -> setValueBasedOnType(value));
    }

    @Override
    public void sendKeys(CharSequence keys) {
        execute(tokenizedMessage(action("sending keys"), stringValue(keys), TO).add(pathDescription),
                () -> tokenizedMessage(action("sent keys"), stringValue(keys), TO).add(pathDescription),
                () -> findElement().sendKeys(keys));
    }

    @Override
    public void clear() {
        execute(tokenizedMessage(action("clearing")).add(pathDescription),
                () -> tokenizedMessage(action("cleared")).add(pathDescription),
                () -> findElement().clear());
    }

    @Override
    public void dragAndDropOver(PageElement pageElement) {
        execute(tokenizedMessage(action("dragging")).add(pathDescription).add(OVER).add(pageElement.locationDescription()),
                () -> tokenizedMessage(action("dropped")).add(pathDescription).add(OVER).add(pageElement.locationDescription()),
                () -> dragAndDropOverStep(pageElement));
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
        return withFilter(new ByTextElementsFilter(additionalBrowserInteractions, text));
    }

    @Override
    public PageElement get(int number) {
        return withFilter(new ByNumberElementsFilter(number));
    }

    @Override
    public PageElement get(Pattern regexp) {
        return withFilter(new ByRegexpElementsFilter(additionalBrowserInteractions, regexp));
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
    public boolean isSelected() {
        return findElement().isSelected();
    }

    @Override
    public boolean isPresent() {
        WebElement webElement = findElement();
        return !(webElement instanceof NullWebElement);
    }

    @Override
    public String toString() {
        return path.toString();
    }

    @Override
    public String getText() {
        return findElement().getText();
    }

    @Override
    public Object getUnderlyingValue() {
        List<WebElement> elements = path.find(driver);
        if (elements.isEmpty()) {
            return null;
        }

        List<Object> values = extractValues(Collections.singletonList(elements.get(0)));
        return values.isEmpty() ? null : values.get(0);
    }

    @Override
    public TokenizedMessage locationDescription() {
        return pathDescription;
    }

    @Override
    public void scrollIntoView() {
        execute(tokenizedMessage(action("scrolling into view ")).add(pathDescription),
                () -> tokenizedMessage(action("scrolled into view")).add(pathDescription),
                () -> ((JavascriptExecutor)driver).executeScript(
                        "arguments[0].scrollIntoView(true);", findElement())
        );
    }

    private void clickWithKey(String label, CharSequence key) {
        execute(tokenizedMessage(action(label + " clicking")).add(pathDescription),
                () -> tokenizedMessage(action(label + " clicked")).add(pathDescription),
                () -> new Actions(driver)
                        .keyDown(key)
                        .click(findElement())
                        .keyUp(key)
                        .build()
                        .perform());
    }

    private String getTagName() {
        return findElement().getTagName();
    }

    private String getAttribute(String name) {
        return findElement().getAttribute(name);
    }

    private List<Object> extractValues() {
        List<WebElement> elements = path.find(driver);
        return extractValues(elements);
    }

    private List<Object> extractValues(List<WebElement> elements) {
        List<Map<String, ?>> elementsMeta = handleStaleElement(() -> additionalBrowserInteractions.extractElementsMeta(elements),
                Collections.emptyList());

        if (elementsMeta.isEmpty()) {
            return Collections.emptyList();
        }

        List<Object> result = new ArrayList<>();

        for (int idx = 0; idx < elements.size(); idx++) {
            HtmlNode htmlNode = new HtmlNode(elementsMeta.get(idx));
            PageElement pageElementByIdx = get(idx + 1);

            result.add(handleStaleElement(() ->
                    PageElementGetSetValueHandlers.getValue(
                            htmlNode,
                            pageElementByIdx), null));
        }

        return result;
    }

    private Integer getNumberOfElements() {
        List<WebElement> webElements = path.find(driver);
        return webElements.size();
    }

    private void setValueBasedOnType(Object value) {
        HtmlNode htmlNode = findHtmlNode();
        PageElementGetSetValueHandlers.setValue(this::execute,
                pathDescription,
                htmlNode,
                this,
                value);
    }

    private void execute(TokenizedMessage inProgressMessage,
                         Supplier<TokenizedMessage> completionMessageSupplier,
                         Runnable action) {
        createAndExecuteStep(inProgressMessage, completionMessageSupplier,
                () -> repeatForStaleElement(() -> {
                    action.run();
                    return null;
                }));
    }

    private void execute(TokenizedMessage inProgressMessage,
                         Function<Object, TokenizedMessage> completionMessageFunc,
                         Supplier<Object> action) {
        createAndExecuteStep(inProgressMessage, completionMessageFunc,
                () -> repeatForStaleElement(action), StepReportOptions.REPORT_ALL);
    }

    private PageElement withFilter(ElementsFilter filter) {
        ElementPath newPath = path.copy();
        newPath.addFilter(filter);

        return new GenericPageElement(driver, additionalBrowserInteractions, newPath);
    }

    private PageElement withFinder(ElementsFinder finder) {
        ElementPath newPath = path.copy();
        newPath.addFinder(finder);

        return new GenericPageElement(driver, additionalBrowserInteractions, newPath);
    }

    private HtmlNode findHtmlNode() {
        List<WebElement> elements = path.find(driver);
        List<Map<String, ?>> elementsMeta = elements.isEmpty() ?
                Collections.emptyList():
                additionalBrowserInteractions.extractElementsMeta(elements);

        return elementsMeta.isEmpty() ? HtmlNode.NULL : new HtmlNode(elementsMeta.get(0));
    }

    private void performActions(String actionLabel, ActionsProvider actionsProvider) {
        WebElement element = findElement();
        ensureNotNullElement(element, actionLabel);

        Actions actions = new Actions(driver);
        actionsProvider.perform(actions, element);

        Action builtAction = actions.build();
        builtAction.perform();
    }

    private void dragAndDropOverStep(PageElement over) {
        WebElement source = findElement();
        ensureNotNullElement(source, "drag source");

        WebElement target = over.findElement();
        ensureNotNullElement(target, "drop target");

        Actions actions = new Actions(driver);
        actions.dragAndDrop(source, target).build().perform();
    }

    private void ensureNotNullElement(WebElement element, String actionLabel) {
        if (element instanceof NullWebElement) {
            ((NullWebElement) element).error(actionLabel);
        }
    }

    private NullWebElement createNullElement() {
        return new NullWebElement(path.toString());
    }

    private static <R> R handleStaleElement(Supplier<R> code, R valueInCaseOfStale) {
        try {
            return code.get();
        } catch (StaleElementReferenceException e) {
            return valueInCaseOfStale;
        }
    }

    private static Object repeatForStaleElement(Supplier<Object> code) {
        int numberOfAttemptsLeft = BrowserConfig.getStaleElementRetry();

        for (; numberOfAttemptsLeft >= 1; numberOfAttemptsLeft--) {
            try {
                return code.get();
            } catch (StaleElementReferenceException e) {
                if (numberOfAttemptsLeft == 1) {
                    throw new RuntimeException("element is stale, " +
                            "consider using waitTo beVisible matcher to make sure component fully appeared");
                }

                sleep(BrowserConfig.getStaleElementRetryWait());
            }
        }

        throw new IllegalStateException("shouldn't reach this point");
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private interface ActionsProvider {
        void perform(Actions actions, WebElement element);
    }
}
