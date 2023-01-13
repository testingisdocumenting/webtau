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

import org.testingisdocumenting.webtau.browser.page.path.PageElementsFinder;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.reporter.*;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public interface PageElement extends
        ActualValueExpectations,
        PrettyPrintable,
        ActualPathAndDescriptionAware {

    PageElementValue<Integer> getCount();

    WebElement findElement();
    List<WebElement> findElements();

    PageElementValue<Object> elementValue();
    PageElementValue<List<Object>> elementValues();

    /**
     * mark this element as to be treated as list of elements when otherwise it will be ambiguous,
     * e.g.
     * <pre>
     *     $("button").should contain("sub text")
     *     $("ul li a").all().should contain("concrete item")
     * </pre>
     * @return PageElement marked as all
     */
    PageElement all();

    boolean isMarkedAsAll();

    void setValue(Object value);
    void setValueNoLog(Object value);
    void sendKeys(CharSequence keys);
    void sendKeysNoLog(CharSequence keys);
    void click();
    void shiftClick();
    void controlClick();
    void commandClick();
    void altClick();
    void rightClick();
    void doubleClick();
    void hover();
    void clear();

    void dragAndDropOver(PageElement target);
    void dragAndDropBy(int offsetX, int offsetY);

    /**
     * uses command on mac os x, and control on other OSes
     */
    default void commandOrControlClick() {
        if (BrowserConditions.isMac()) {
            commandClick();
        } else {
            controlClick();
        }
    }

    PageElement find(String css);
    PageElement find(PageElementsFinder finder);
    PageElement get(String text);
    PageElement get(int number);
    PageElement get(Pattern regexp);

    boolean isVisible();
    boolean isEnabled();
    boolean isSelected();
    boolean isPresent();

    String getText();
    Object getUnderlyingValue();

    TokenizedMessage locationDescription();

    void scrollIntoView();
    void scrollToTop();
    void scrollToBottom();
    void scrollToLeft();
    void scrollToRight();
    void scrollTo(int x, int y);

    /**
     * element scroll from the top
     * @return scrollTop element value
     * @see PageElementValue
     */
    PageElementValue<Integer> getScrollTop();

    /**
     * element scroll from the left
     * @return scrollLeft element value
     * @see PageElementValue
     */
    PageElementValue<Integer> getScrollLeft();

    /**
     * element overall height that can be scrolled
     * @return scrollHeight element value
     * @see PageElementValue
     */
    PageElementValue<Integer> getScrollHeight();

    /**
     * element overall width that can be scrolled
     * @return scrollWidth element value
     * @see PageElementValue
     */
    PageElementValue<Integer> getScrollWidth();

    /**
     * element offset height
     * @return offsetHeight element value
     * @see PageElementValue
     */
    PageElementValue<Integer> getOffsetHeight();

    /**
     * element offset width
     * @return offsetWidth element value
     * @see PageElementValue
     */
    PageElementValue<Integer> getOffsetWidth();

    /**
     * element client height
     * @return clientHeight element value
     * @see PageElementValue
     */
    PageElementValue<Integer> getClientHeight();

    /**
     * element client width
     * @return clientWidth element value
     * @see PageElementValue
     */
    PageElementValue<Integer> getClientWidth();

    void highlight();

    @Override
    default StepReportOptions shouldReportOption() {
        return StepReportOptions.REPORT_ALL;
    }

    @Override
    default void prettyPrint(PrettyPrinter printer) {
        TokenizedMessageToAnsiConverter toAnsiConverter = IntegrationTestsMessageBuilder.getConverter();

        if (!isPresent()) {
            printer.printLine(Stream.concat(
                    Stream.of(Color.RED, "element is not present: "),
                    toAnsiConverter.convert(locationDescription()).stream()).toArray());
            return;
        }

        printer.printLine(Stream.concat(
                Stream.of(Color.GREEN, "element is found: "),
                toAnsiConverter.convert(locationDescription()).stream()).toArray());

        printer.printLine(Color.YELLOW, "           getText(): ", Color.GREEN, getText());
        printer.printLine(Color.YELLOW, "getUnderlyingValue(): ", Color.GREEN, getUnderlyingValue());
        Integer count = getCount().get();
        if (count > 1) {
            printer.printLine(Color.YELLOW, "               count: ", Color.GREEN, count);
        }
    }
}
