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

package org.testingisdocumenting.webtau.browser.page;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testingisdocumenting.webtau.browser.AdditionalBrowserInteractions;
import org.testingisdocumenting.webtau.browser.handlers.PageElementGetSkipValue;
import org.testingisdocumenting.webtau.browser.page.path.PageElementPath;
import org.testingisdocumenting.webtau.browser.page.path.PageElementsFilter;
import org.testingisdocumenting.webtau.browser.page.path.PageElementsFinder;
import org.testingisdocumenting.webtau.browser.page.path.filter.ByNumberPageElementsFilter;
import org.testingisdocumenting.webtau.browser.page.path.filter.ByRegexpPageElementsFilter;
import org.testingisdocumenting.webtau.browser.page.path.filter.ByTextPageElementsFilter;
import org.testingisdocumenting.webtau.browser.page.path.finder.ByCssFinderPage;
import org.testingisdocumenting.webtau.browser.handlers.PageElementGetSetValueHandlers;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.reporter.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.browser.page.stale.StaleElementHandler.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.createAndExecuteStep;
import static org.testingisdocumenting.webtau.WebTauCore.tokenizedMessage;

public class GenericPageElement implements PageElement {
    private final static TokenizedMessage HIDDEN_MESSAGE_STRING_VALUE = tokenizedMessage().string("*****");

    private final WebDriver driver;
    private final AdditionalBrowserInteractions additionalBrowserInteractions;
    private final PageElementPath path;
    private final TokenizedMessage pathDescription;
    private final PageElementValue<Object> elementValue;
    private final PageElementValue<Integer> countValue;
    private final PageElementValue<Integer> scrollTopValue;
    private final PageElementValue<Integer> scrollLeftValue;
    private final PageElementValue<Integer> scrollHeight;
    private final PageElementValue<Integer> scrollWidth;
    private final PageElementValue<Integer> offsetHeight;
    private final PageElementValue<Integer> offsetWidth;
    private final PageElementValue<Integer> clientHeight;
    private final PageElementValue<Integer> clientWidth;

    private final boolean isMarkedAsAll;

    public GenericPageElement(WebDriver driver,
                              AdditionalBrowserInteractions additionalBrowserInteractions,
                              PageElementPath path,
                              boolean isMarkedAsAll) {
        this.driver = driver;
        this.additionalBrowserInteractions = additionalBrowserInteractions;
        this.path = path;
        this.pathDescription = path.describe();
        this.isMarkedAsAll = isMarkedAsAll;
        this.elementValue = new PageElementValue<>(this, "value", this::getUnderlyingValue);
        this.countValue = new PageElementValue<>(this, "count", this::getNumberOfElements);
        this.scrollTopValue = new PageElementValue<>(this, "scrollTop", fetchIntElementPropertyFunc("scrollTop"));
        this.scrollLeftValue = new PageElementValue<>(this, "scrollLeft", fetchIntElementPropertyFunc("scrollLeft"));
        this.scrollHeight = new PageElementValue<>(this, "scrollHeight", fetchIntElementPropertyFunc("scrollHeight"));
        this.scrollWidth = new PageElementValue<>(this, "scrollWidth", fetchIntElementPropertyFunc("scrollWidth"));
        this.offsetHeight = new PageElementValue<>(this, "offsetHeight", fetchIntElementPropertyFunc("offsetHeight"));
        this.offsetWidth = new PageElementValue<>(this, "offsetWidth", fetchIntElementPropertyFunc("offsetWidth"));
        this.clientHeight = new PageElementValue<>(this, "clientHeight", fetchIntElementPropertyFunc("clientHeight"));
        this.clientWidth = new PageElementValue<>(this, "clientWidth", fetchIntElementPropertyFunc("clientWidth"));
    }

    @Override
    public PageElementValue<Integer> getCount() {
        return countValue;
    }

    @Override
    public PageElementValue<Integer> getScrollTop() {
        return scrollTopValue;
    }

    @Override
    public PageElementValue<Integer> getScrollLeft() {
        return scrollLeftValue;
    }

    @Override
    public PageElementValue<Integer> getScrollHeight() {
        return scrollHeight;
    }

    @Override
    public PageElementValue<Integer> getScrollWidth() {
        return scrollWidth;
    }

    @Override
    public PageElementValue<Integer> getOffsetHeight() {
        return offsetHeight;
    }

    @Override
    public PageElementValue<Integer> getOffsetWidth() {
        return offsetWidth;
    }

    @Override
    public PageElementValue<Integer> getClientHeight() {
        return clientHeight;
    }

    @Override
    public PageElementValue<Integer> getClientWidth() {
        return clientWidth;
    }

    @Override
    public ValuePath actualPath() {
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
        execute(tokenizedMessage().action("clicking").add(pathDescription),
                () -> tokenizedMessage().action("clicked").add(pathDescription),
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
        execute(tokenizedMessage().action("right clicking").add(pathDescription),
                () -> tokenizedMessage().action("right clicked").add(pathDescription),
                () -> performActions("right click", Actions::contextClick));
    }

    @Override
    public void doubleClick() {
        execute(tokenizedMessage().action("double clicking").add(pathDescription),
                () -> tokenizedMessage().action("double clicked").add(pathDescription),
                () -> performActions("double click", Actions::doubleClick));
    }

    @Override
    public void hover() {
        execute(tokenizedMessage().action("moving mouse over").add(pathDescription),
                () -> tokenizedMessage().action("moved mouse over").add(pathDescription),
                () -> performActions("hover", Actions::moveToElement));
    }

    public WebElement findElement() {
        List<WebElement> webElements = findElements();
        return webElements.isEmpty() ? createNullElement() : webElements.get(0);
    }

    @Override
    public List<WebElement> findElements() {
        return path.find(driver);
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
    public PageElement all() {
        return new GenericPageElement(driver, additionalBrowserInteractions, path, true);
    }

    @Override
    public boolean isMarkedAsAll() {
        return isMarkedAsAll;
    }

    @Override
    public void setValue(Object value) {
        execute(tokenizedMessage().action("setting value").string(value).to().add(pathDescription),
                () -> tokenizedMessage().action("set value").string(value).to().add(pathDescription),
                () -> setValueBasedOnType(value, false));
    }

    @Override
    public void setValueNoLog(Object value) {
        execute(tokenizedMessage().action("setting value").add(HIDDEN_MESSAGE_STRING_VALUE).to().add(pathDescription),
                () -> tokenizedMessage().action("set value").add(HIDDEN_MESSAGE_STRING_VALUE).to().add(pathDescription),
                () -> setValueBasedOnType(value, true));
    }

    @Override
    public void sendKeys(CharSequence keys) {
        String renderedKeys = BrowserKeysRenderer.renderKeys(keys);
        execute(tokenizedMessage().action("sending keys").string(renderedKeys).to().add(pathDescription),
                () -> tokenizedMessage().action("sent keys").string(renderedKeys).to().add(pathDescription),
                () -> findElement().sendKeys(keys));
    }

    @Override
    public void sendKeysNoLog(CharSequence keys) {
        execute(tokenizedMessage().action("sending keys").add(HIDDEN_MESSAGE_STRING_VALUE).to().add(pathDescription),
                () -> tokenizedMessage().action("sent keys").add(HIDDEN_MESSAGE_STRING_VALUE).to().add(pathDescription),
                () -> findElement().sendKeys(keys));
    }

    @Override
    public void clear() {
        execute(tokenizedMessage().action("clearing").add(pathDescription),
                () -> tokenizedMessage().action("cleared").add(pathDescription),
                () -> findElement().clear());
    }

    @Override
    public void dragAndDropOver(PageElement target) {
        execute(tokenizedMessage().action("dragging").add(pathDescription).over().add(target.locationDescription()),
                () -> tokenizedMessage().action("dropped").add(pathDescription).over().add(target.locationDescription()),
                () -> dragAndDropOverStep(target));
    }

    @Override
    public void dragAndDropBy(int offsetX, int offsetY) {
        execute(tokenizedMessage().action("dragging").add(pathDescription),
                map("offsetX", offsetX, "offsetY", offsetY),
                () -> tokenizedMessage().action("dropped").add(pathDescription),
                () -> dragAndDropByStep(offsetX, offsetY));
    }

    @Override
    public PageElement find(String css) {
        return find(new ByCssFinderPage(css));
    }

    @Override
    public PageElement find(PageElementsFinder finder) {
        return withFinder(finder);
    }

    @Override
    public PageElement get(String text) {
        return withFilter(new ByTextPageElementsFilter(additionalBrowserInteractions, text));
    }

    @Override
    public PageElement get(int number) {
        return withFilter(new ByNumberPageElementsFilter(number));
    }

    @Override
    public PageElement get(Pattern regexp) {
        return withFilter(new ByRegexpPageElementsFilter(additionalBrowserInteractions, regexp));
    }

    @Override
    public boolean isVisible() {
        return getValueForStaleElement(() -> findElement().isDisplayed(), false);
    }

    @Override
    public boolean isEnabled() {
        return getValueForStaleElement(() -> findElement().isEnabled(), false);
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

        List<Object> values = extractValues();
        return values.isEmpty() ? null : values.get(0);
    }

    @Override
    public TokenizedMessage locationDescription() {
        return pathDescription;
    }

    @Override
    public void scrollIntoView() {
        execute(tokenizedMessage().action("scrolling into view").add(pathDescription),
                () -> tokenizedMessage().action("scrolled into view").add(pathDescription),
                () -> checkNotNullAndExecuteScriptOnElement("scroll into view",
                        "arguments[0].scrollIntoView(true);"));
    }

    @Override
    public void scrollToTop() {
        execute(tokenizedMessage().action("scrolling to top").of().add(pathDescription),
                () -> tokenizedMessage().action("scrolled to top").of().add(pathDescription),
                () -> checkNotNullAndExecuteScriptOnElement("scroll to top",
                        "arguments[0].scrollTo(arguments[0].scrollLeft, 0);"));
    }

    @Override
    public void scrollToBottom() {
        execute(tokenizedMessage().action("scrolling to bottom").of().add(pathDescription),
                () -> tokenizedMessage().action("scrolled to bottom").of().add(pathDescription),
                () -> checkNotNullAndExecuteScriptOnElement("scroll to bottom",
                        "arguments[0].scrollTo(arguments[0].scrollLeft, arguments[0].scrollHeight);"));
    }

    @Override
    public void scrollToLeft() {
        execute(tokenizedMessage().action("scrolling to left").of().add(pathDescription),
                () -> tokenizedMessage().action("scrolled to left").of().add(pathDescription),
                () -> checkNotNullAndExecuteScriptOnElement("scroll to left",
                        "arguments[0].scrollTo(0, arguments[0].scrollTop);"));
    }

    @Override
    public void scrollToRight() {
        execute(tokenizedMessage().action("scrolling to right").of().add(pathDescription),
                () -> tokenizedMessage().action("scrolled to right").of().add(pathDescription),
                () -> checkNotNullAndExecuteScriptOnElement("scroll to right",
                        "arguments[0].scrollTo(arguments[0].scrollWidth, arguments[0].scrollTop);"));
    }

    @Override
    public void scrollTo(int x, int y) {
        execute(tokenizedMessage().action("scrolling to").number(x).comma().number(y).of().add(pathDescription),
                () -> tokenizedMessage().action("scrolled to").number(x).colon().number(y).of().add(pathDescription),
                () -> checkNotNullAndExecuteScriptOnElement("scroll to position",
                        "arguments[0].scrollTo(arguments[1], arguments[2]);", x, y));
    }

    @Override
    public List<HtmlNode> extractHtmlNodes() {
        return extractHtmlNodes(findElements());
    }

    private List<HtmlNode> extractHtmlNodes(List<WebElement> elements) {
        return getValueForStaleElement(
                () -> additionalBrowserInteractions.extractHtmlNodes(elements),
                Collections.emptyList());
    }

    private void clickWithKey(String label, CharSequence key) {
        execute(tokenizedMessage().action(label + " clicking").add(pathDescription),
                () -> tokenizedMessage().action(label + " clicked").add(pathDescription),
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
        HtmlNodeAndWebElementList htmlNodeAndWebElements = findHtmlNodesAndWebElements();

        if (htmlNodeAndWebElements.isEmpty()) {
            return Collections.emptyList();
        }

        List<Object> result = new ArrayList<>();

        for (int idx = 0; idx < htmlNodeAndWebElements.size(); idx++) {
            PageElement pageElementByIdx = get(idx + 1);

            int finalIdx = idx;
            Object value = getValueForStaleElement(() ->
                    PageElementGetSetValueHandlers.getValue(
                            htmlNodeAndWebElements,
                            pageElementByIdx,
                            finalIdx), null);

            if (value != PageElementGetSkipValue.INSTANCE) {
                result.add(value);
            }
        }

        return result;
    }

    private Integer getNumberOfElements() {
        return getValueForStaleElement(() -> {
            List<WebElement> webElements = path.find(driver);
            return webElements.size();
        }, -1);
    }

    private PageElementValueFetcher<Integer> fetchIntElementPropertyFunc(String prop) {
        return () -> fetchIntElementProperty(prop);
    }

    private Integer fetchIntElementProperty(String prop) {
        List<WebElement> elements = findElements();
        if (elements.isEmpty()) {
            return null;
        }

        Object value = ((JavascriptExecutor) driver).executeScript(
                "return arguments[0]." + prop + ";", elements.get(0));
        if (value instanceof Long) {
            Long scrollTop = (Long) value;
            return Math.toIntExact(scrollTop);
        }

        return ((Double) value).intValue();
    }

    private void setValueBasedOnType(Object value, boolean noLog) {
        HtmlNodeAndWebElementList htmlNodeAndWebElements = findHtmlNodesAndWebElements();
        PageElementGetSetValueHandlers.setValue(this::execute,
                pathDescription,
                htmlNodeAndWebElements,
                this,
                value,
                noLog);
    }

    private void execute(TokenizedMessage inProgressMessage,
                         Supplier<TokenizedMessage> completionMessageSupplier,
                         Runnable action) {
        execute(inProgressMessage, Collections.emptyMap(), completionMessageSupplier, action);
    }

    private void execute(TokenizedMessage inProgressMessage,
                         Map<String, Object> stepInputData,
                         Supplier<TokenizedMessage> completionMessageSupplier,
                         Runnable action) {
        WebTauStepInput stepInput = stepInputData.isEmpty() ?
                WebTauStepInput.EMPTY :
                WebTauStepInputKeyValue.stepInput(stepInputData);

        createAndExecuteStep(inProgressMessage,
                stepInput,
                completionMessageSupplier,
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

    private PageElement withFilter(PageElementsFilter filter) {
        PageElementPath newPath = path.copy();
        newPath.addFilter(filter);

        return new GenericPageElement(driver, additionalBrowserInteractions, newPath, false);
    }

    private PageElement withFinder(PageElementsFinder finder) {
        PageElementPath newPath = path.copy();
        newPath.addFinder(finder);

        return new GenericPageElement(driver, additionalBrowserInteractions, newPath, false);
    }

    private HtmlNodeAndWebElementList findHtmlNodesAndWebElements() {
        List<WebElement> elements = findElements();

        if (elements.isEmpty()) {
            return HtmlNodeAndWebElementList.empty();
        }

        List<HtmlNode> htmlNodes = extractHtmlNodes(elements);

        List<HtmlNodeAndWebElement> htmlNodeAndWebElements =
                IntStream.range(0, Math.min(elements.size(), htmlNodes.size()))
                        .mapToObj((idx) -> new HtmlNodeAndWebElement(htmlNodes.get(idx), elements.get(idx)))
                        .collect(Collectors.toList());

        return new HtmlNodeAndWebElementList(htmlNodeAndWebElements);
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

    private void dragAndDropByStep(int offsetX, int offsetY) {
        WebElement source = findElement();
        ensureNotNullElement(source, "drag source");

        Actions actions = new Actions(driver);
        actions.dragAndDropBy(source, offsetX, offsetY).build().perform();
    }

    private void ensureNotNullElement(WebElement element, String actionLabel) {
        if (element instanceof NullWebElement) {
            ((NullWebElement) element).error(actionLabel);
        }
    }

    private NullWebElement createNullElement() {
        return new NullWebElement(path.toString());
    }

    private void checkNotNullAndExecuteScriptOnElement(String actionLabel, String script, Object... args) {
        WebElement element = findElement();
        ensureNotNullElement(element, actionLabel);

        ArrayList<Object> argsList = new ArrayList<>();
        argsList.add(element);
        argsList.addAll(Arrays.asList(args));

        ((JavascriptExecutor) driver).executeScript(script, argsList.toArray(new Object[0]));
    }

    private interface ActionsProvider {
        void perform(Actions actions, WebElement element);
    }
}
