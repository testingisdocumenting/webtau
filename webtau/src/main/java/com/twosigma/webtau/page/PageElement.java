package com.twosigma.webtau.page;

import com.twosigma.webtau.expectation.ActualValueExpectations;
import com.twosigma.webtau.expectation.ShouldAndWaitProperty;
import com.twosigma.webtau.expectation.ValueMatcher;
import com.twosigma.webtau.expectation.timer.ExpectationTimer;
import com.twosigma.webtau.reporter.StepReportOptions;
import com.twosigma.webtau.reporter.TokenizedMessage;
import com.twosigma.webtau.reporter.ValueMatcherExpectationSteps;
import org.openqa.selenium.WebElement;

import java.util.regex.Pattern;

import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public interface PageElement extends ActualValueExpectations {
    PageElement all();
    ElementValue<Integer> getCount();
    WebElement findElement();
    ElementValue elementValue();
    void setValue(Object value);
    void sendKeys(String keys);
    void click();
    void clear();
    PageElement get(String text);
    PageElement get(int number);
    PageElement get(Pattern regexp);
    boolean isVisible();
    TokenizedMessage describe();

    @Override
    default void should(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldStep(this, this.elementValue(),  StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher);
    }

    @Override
    default void shouldNot(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldNotStep(this, this.elementValue(),  StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher);
    }

    @Override
    default void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ValueMatcherExpectationSteps.waitStep(this, this.elementValue(), StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    @Override
    default void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ValueMatcherExpectationSteps.waitNotStep(this, this.elementValue(), StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    default ShouldAndWaitProperty getShould() {
        return new ShouldAndWaitProperty<>(this, this::should);
    }

    default ShouldAndWaitProperty getShouldNot() {
        return new ShouldAndWaitProperty<>(this, this::shouldNot);
    }

    default ShouldAndWaitProperty getWaitTo() {
        return new ShouldAndWaitProperty<>(this, this::waitTo);
    }

    default ShouldAndWaitProperty getWaitToNot() {
        return new ShouldAndWaitProperty<>(this, this::waitToNot);
    }
}
