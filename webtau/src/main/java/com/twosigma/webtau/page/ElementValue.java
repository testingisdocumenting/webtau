package com.twosigma.webtau.page;

import com.twosigma.webtau.expectation.ActualValueExpectations;
import com.twosigma.webtau.expectation.ShouldAndWaitProperty;
import com.twosigma.webtau.expectation.ValueMatcher;
import com.twosigma.webtau.expectation.timer.ExpectationTimer;
import com.twosigma.webtau.reporter.StepReportOptions;
import com.twosigma.webtau.reporter.TokenizedMessage;
import com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder;
import com.twosigma.webtau.reporter.ValueMatcherExpectationSteps;

import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.OF;

public class ElementValue<E> implements ActualValueExpectations {
    private PageElement parent;
    private String name;
    private ElementValueFetcher<E> valueFetcher;

    private TokenizedMessage description;

    public ElementValue(PageElement parent, String name, ElementValueFetcher<E> valueFetcher) {
        this.parent = parent;
        this.name = name;
        this.valueFetcher = valueFetcher;
        this.description = tokenizedMessage(IntegrationTestsMessageBuilder.classifier(name)).add(OF).add(parent.describe());
    }

    public PageElement getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public E get() {
        return valueFetcher.fetch();
    }

    public TokenizedMessage describe() {
        return this.description;
    }

    @Override
    public void should(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldStep(this.parent, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher);
    }

    @Override
    public void shouldNot(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldNotStep(this.parent, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher);
    }

    @Override
    public void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ValueMatcherExpectationSteps.waitStep(this.parent, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    @Override
    public void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ValueMatcherExpectationSteps.waitNotStep(this.parent, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    public ShouldAndWaitProperty getShould() {
        return new ShouldAndWaitProperty<>(this, this::should);
    }

    public ShouldAndWaitProperty getShouldNot() {
        return new ShouldAndWaitProperty<>(this, this::shouldNot);
    }

    public ShouldAndWaitProperty getWaitTo() {
        return new ShouldAndWaitProperty<>(this, this::waitTo);
    }

    public ShouldAndWaitProperty getWaitToNot() {
        return new ShouldAndWaitProperty<>(this, this::waitToNot);
    }
}
