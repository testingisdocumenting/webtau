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

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testingisdocumenting.webtau.browser.AdditionalBrowserInteractions;
import org.testingisdocumenting.webtau.browser.handlers.PageElementGetSetValueHandlers;
import org.testingisdocumenting.webtau.browser.handlers.PageElementGetSkipValue;
import org.testingisdocumenting.webtau.browser.page.path.PageElementFinder;
import org.testingisdocumenting.webtau.browser.page.path.PageElementPath;
import org.testingisdocumenting.webtau.browser.page.path.PageElementsFilter;
import org.testingisdocumenting.webtau.browser.page.path.filter.ByNumberPageElementsFilter;
import org.testingisdocumenting.webtau.browser.page.path.filter.ByRegexpPageElementsFilter;
import org.testingisdocumenting.webtau.browser.page.path.filter.ByTextPageElementsFilter;
import org.testingisdocumenting.webtau.browser.page.path.finder.ByCssPageElementFinder;
import org.testingisdocumenting.webtau.browser.page.path.finder.ParentPageElementFinder;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.WebTauStepInput;
import org.testingisdocumenting.webtau.reporter.WebTauStepInputKeyValue;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.browser.page.stale.StaleElementHandler.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.*;

public class PageElement implements
        ActualValueExpectations,
        PrettyPrintable,
        ActualPathAndDescriptionAware {
    private final static TokenizedMessage HIDDEN_MESSAGE_STRING_VALUE = tokenizedMessage().string("*****");

    public final PageElementValue<Object> value;
    public final PageElementValue<List<Object>> valuesList;

    public final PageElementValue<Integer> count;

    public final PageElementValue<Integer> scrollTop;
    public final PageElementValue<Integer> scrollLeft;
    public final PageElementValue<Integer> scrollHeight;
    public final PageElementValue<Integer> scrollWidth;

    public final PageElementValue<Integer> offsetHeight;
    public final PageElementValue<Integer> offsetWidth;
    public final PageElementValue<Integer> clientHeight;
    public final PageElementValue<Integer> clientWidth;

    private final WebDriver driver;
    private final AdditionalBrowserInteractions additionalBrowserInteractions;
    private final PageElementPath path;
    private final TokenizedMessage pathDescription;

    private final boolean isMarkedAsAll;

    public PageElement(WebDriver driver,
                       AdditionalBrowserInteractions additionalBrowserInteractions,
                       PageElementPath path,
                       boolean isMarkedAsAll) {
        this.driver = driver;
        this.additionalBrowserInteractions = additionalBrowserInteractions;
        this.path = path;
        this.pathDescription = path.describe();
        this.isMarkedAsAll = isMarkedAsAll;
        this.value = new PageElementValue<>(this, "value", this::extractSingleValue);
        this.valuesList = new PageElementValue<>(this, "value", this::extractListOfValues);
        this.count = new PageElementValue<>(this, "count", this::extractNumberOfElements);
        this.scrollTop = new PageElementValue<>(this, "scrollTop", fetchIntElementPropertyFunc("scrollTop"));
        this.scrollLeft = new PageElementValue<>(this, "scrollLeft", fetchIntElementPropertyFunc("scrollLeft"));
        this.scrollHeight = new PageElementValue<>(this, "scrollHeight", fetchIntElementPropertyFunc("scrollHeight"));
        this.scrollWidth = new PageElementValue<>(this, "scrollWidth", fetchIntElementPropertyFunc("scrollWidth"));
        this.offsetHeight = new PageElementValue<>(this, "offsetHeight", fetchIntElementPropertyFunc("offsetHeight"));
        this.offsetWidth = new PageElementValue<>(this, "offsetWidth", fetchIntElementPropertyFunc("offsetWidth"));
        this.clientHeight = new PageElementValue<>(this, "clientHeight", fetchIntElementPropertyFunc("clientHeight"));
        this.clientWidth = new PageElementValue<>(this, "clientWidth", fetchIntElementPropertyFunc("clientWidth"));
    }

    public PageElementValue<String> attribute(String name) {
        return new PageElementValue<>(this, "attribute \"" + name + "\"", () -> {
            List<HtmlNode> htmlNodes = extractHtmlNodes();
            if (htmlNodes.isEmpty()) {
                return null;
            }

            return htmlNodes.get(0).attributes().get(name);
        });
    }

    public ValuePath actualPath() {
        return createActualPath("pageElement");
    }

    public TokenizedMessage describe() {
        return pathDescription;
    }

    public void highlight() {
        additionalBrowserInteractions.flashWebElements(findElements());
    }

    public void click() {
        execute(tokenizedMessage().action("clicking").add(pathDescription),
                () -> tokenizedMessage().action("clicked").add(pathDescription),
                () -> findElement().click());
    }

    public void shiftClick() {
        clickWithKey("shift", Keys.SHIFT);
    }

    public void controlClick() {
        clickWithKey("control", Keys.CONTROL);
    }

    public void commandClick() {
        clickWithKey("command", Keys.COMMAND);
    }

    public void altClick() {
        clickWithKey("alt", Keys.ALT);
    }

    public void commandOrControlClick() {
        if (BrowserConditions.isMac()) {
            commandClick();
        } else {
            controlClick();
        }
    }

    public void rightClick() {
        execute(tokenizedMessage().action("right clicking").add(pathDescription),
                () -> tokenizedMessage().action("right clicked").add(pathDescription),
                () -> performActions("right click", Actions::contextClick));
    }

    public void doubleClick() {
        execute(tokenizedMessage().action("double clicking").add(pathDescription),
                () -> tokenizedMessage().action("double clicked").add(pathDescription),
                () -> performActions("double click", Actions::doubleClick));
    }

    public void hover() {
        execute(tokenizedMessage().action("moving mouse over").add(pathDescription),
                () -> tokenizedMessage().action("moved mouse over").add(pathDescription),
                () -> performActions("hover", Actions::moveToElement));
    }

    public WebElement findElement() {
        List<WebElement> webElements = findElements();
        return webElements.isEmpty() ? createNullElement() : webElements.get(0);
    }

    public List<WebElement> findElements() {
        return path.find(driver);
    }

    public PageElement all() {
        return new PageElement(driver, additionalBrowserInteractions, path, true);
    }

    public boolean isMarkedAsAll() {
        return isMarkedAsAll;
    }

    public void setValue(Object value) {
        String renderedKeys = BrowserKeysRenderer.renderKeys(value.toString());
        execute(tokenizedMessage().action("setting value").string(renderedKeys).to().add(pathDescription),
                () -> tokenizedMessage().action("set value").string(renderedKeys).to().add(pathDescription),
                () -> setValueBasedOnType(value, false));
    }

    public void setValueNoLog(Object value) {
        execute(tokenizedMessage().action("setting value").add(HIDDEN_MESSAGE_STRING_VALUE).to().add(pathDescription),
                () -> tokenizedMessage().action("set value").add(HIDDEN_MESSAGE_STRING_VALUE).to().add(pathDescription),
                () -> setValueBasedOnType(value, true));
    }

    public void sendKeys(CharSequence keys) {
        String renderedKeys = BrowserKeysRenderer.renderKeys(keys);
        execute(tokenizedMessage().action("sending keys").string(renderedKeys).to().add(pathDescription),
                () -> tokenizedMessage().action("sent keys").string(renderedKeys).to().add(pathDescription),
                () -> findElement().sendKeys(keys));
    }

    public void sendKeysNoLog(CharSequence keys) {
        execute(tokenizedMessage().action("sending keys").add(HIDDEN_MESSAGE_STRING_VALUE).to().add(pathDescription),
                () -> tokenizedMessage().action("sent keys").add(HIDDEN_MESSAGE_STRING_VALUE).to().add(pathDescription),
                () -> findElement().sendKeys(keys));
    }

    public void clear() {
        execute(tokenizedMessage().action("clearing").add(pathDescription),
                () -> tokenizedMessage().action("cleared").add(pathDescription),
                () -> findElement().clear());
    }

    public void dragAndDropOver(PageElement target) {
        execute(tokenizedMessage().action("dragging").add(pathDescription).over().add(target.locationDescription()),
                () -> tokenizedMessage().action("dropped").add(pathDescription).over().add(target.locationDescription()),
                () -> dragAndDropOverStep(target));
    }

    public void dragAndDropBy(int offsetX, int offsetY) {
        execute(tokenizedMessage().action("dragging").add(pathDescription),
                map("offsetX", offsetX, "offsetY", offsetY),
                () -> tokenizedMessage().action("dropped").add(pathDescription),
                () -> dragAndDropByStep(offsetX, offsetY));
    }

    public PageElement find(String css) {
        return find(new ByCssPageElementFinder(css));
    }

    public PageElement find(PageElementFinder finder) {
        return withFinder(finder);
    }

    public PageElement parent() {
        return withFinder(new ParentPageElementFinder());
    }

    public PageElement get(String text) {
        return withFilter(new ByTextPageElementsFilter(additionalBrowserInteractions, text));
    }

    public PageElement get(int number) {
        return withFilter(new ByNumberPageElementsFilter(number));
    }

    public PageElement get(Pattern regexp) {
        return withFilter(new ByRegexpPageElementsFilter(additionalBrowserInteractions, regexp));
    }

    public boolean isVisible() {
        return getValueForStaleElement(() -> findElement().isDisplayed(), false);
    }

    public boolean isEnabled() {
        return getValueForStaleElement(() -> findElement().isEnabled(), false);
    }

    public boolean isSelected() {
        return findElement().isSelected();
    }

    public boolean isPresent() {
        WebElement webElement = findElement();
        return !(webElement instanceof NullWebElement);
    }

    public String toString() {
        return path.toString();
    }

    public String getText() {
        return findElement().getText();
    }

    public TokenizedMessage locationDescription() {
        return pathDescription;
    }

    public void scrollIntoView() {
        execute(tokenizedMessage().action("scrolling into view").add(pathDescription),
                () -> tokenizedMessage().action("scrolled into view").add(pathDescription),
                () -> checkNotNullAndExecuteScriptOnElement("scroll into view",
                        "arguments[0].scrollIntoView(true);"));
    }

    public void scrollToTop() {
        execute(tokenizedMessage().action("scrolling to top").of().add(pathDescription),
                () -> tokenizedMessage().action("scrolled to top").of().add(pathDescription),
                () -> checkNotNullAndExecuteScriptOnElement("scroll to top",
                        "arguments[0].scrollTo(arguments[0].scrollLeft, 0);"));
    }

    public void scrollToBottom() {
        execute(tokenizedMessage().action("scrolling to bottom").of().add(pathDescription),
                () -> tokenizedMessage().action("scrolled to bottom").of().add(pathDescription),
                () -> checkNotNullAndExecuteScriptOnElement("scroll to bottom",
                        "arguments[0].scrollTo(arguments[0].scrollLeft, arguments[0].scrollHeight);"));
    }

    public void scrollToLeft() {
        execute(tokenizedMessage().action("scrolling to left").of().add(pathDescription),
                () -> tokenizedMessage().action("scrolled to left").of().add(pathDescription),
                () -> checkNotNullAndExecuteScriptOnElement("scroll to left",
                        "arguments[0].scrollTo(0, arguments[0].scrollTop);"));
    }

    public void scrollToRight() {
        execute(tokenizedMessage().action("scrolling to right").of().add(pathDescription),
                () -> tokenizedMessage().action("scrolled to right").of().add(pathDescription),
                () -> checkNotNullAndExecuteScriptOnElement("scroll to right",
                        "arguments[0].scrollTo(arguments[0].scrollWidth, arguments[0].scrollTop);"));
    }

    public void scrollTo(int x, int y) {
        execute(tokenizedMessage().action("scrolling to").number(x).comma().number(y).of().add(pathDescription),
                () -> tokenizedMessage().action("scrolled to").number(x).colon().number(y).of().add(pathDescription),
                () -> checkNotNullAndExecuteScriptOnElement("scroll to position",
                        "arguments[0].scrollTo(arguments[1], arguments[2]);", x, y));
    }

    @Override
    public StepReportOptions shouldReportOption() {
        return StepReportOptions.REPORT_ALL;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        new PageElementPrettyPrinter(this, 10).prettyPrint(printer);
    }

    List<HtmlNode> extractHtmlNodes() {
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


    private Object extractSingleValue() {
        List<WebElement> elements = path.find(driver);
        if (elements.isEmpty()) {
            return null;
        }

        List<Object> values = extractListOfValues();
        return values.isEmpty() ? null : values.get(0);
    }

    private List<Object> extractListOfValues() {
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

    private Integer extractNumberOfElements() {
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
        if (value instanceof Long jsScrollTop) {
            return Math.toIntExact(jsScrollTop);
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

        return new PageElement(driver, additionalBrowserInteractions, newPath, false);
    }

    private PageElement withFinder(PageElementFinder finder) {
        PageElementPath newPath = path.copy();
        newPath.addFinder(finder);

        return new PageElement(driver, additionalBrowserInteractions, newPath, false);
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
