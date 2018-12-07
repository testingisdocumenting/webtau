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

package com.twosigma.webtau.browser.page;

import com.twosigma.webtau.browser.page.path.ElementsFinder;
import com.twosigma.webtau.browser.page.value.ElementValue;
import com.twosigma.webtau.expectation.ActualValueExpectations;
import com.twosigma.webtau.expectation.ValueMatcher;
import com.twosigma.webtau.expectation.timer.ExpectationTimer;
import com.twosigma.webtau.reporter.StepReportOptions;
import com.twosigma.webtau.reporter.TokenizedMessage;
import com.twosigma.webtau.reporter.ValueMatcherExpectationSteps;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Pattern;

public interface PageElement extends ActualValueExpectations {
    ElementValue<Integer> getCount();

    WebElement findElement();
    List<WebElement> findElements();

    ElementValue<String> elementValue();
    ElementValue<List<String>> elementValues();

    void setValue(Object value);
    void sendKeys(String keys);
    void click();
    void clear();

    PageElement find(String css);
    PageElement find(ElementsFinder finder);
    PageElement get(String text);
    PageElement get(int number);
    PageElement get(Pattern regexp);

    boolean isVisible();
    boolean isEnabled();
    TokenizedMessage describe();

    String getText();

    void highlight();

    @Override
    default void should(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldStep(this, this,  StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher);
    }

    @Override
    default void shouldNot(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldNotStep(this, this,  StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher);
    }

    @Override
    default void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ValueMatcherExpectationSteps.waitStep(this, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    @Override
    default void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ValueMatcherExpectationSteps.waitNotStep(this, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }
}
