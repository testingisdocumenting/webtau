package com.twosigma.webtau.http.datanode;

import com.twosigma.webtau.expectation.ActualPathAware;
import com.twosigma.webtau.expectation.ActualValueExpectations;
import com.twosigma.webtau.expectation.ShouldAndWaitProperty;
import com.twosigma.webtau.expectation.ValueMatcher;
import com.twosigma.webtau.expectation.timer.ExpectationTimer;
import com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder;
import com.twosigma.webtau.reporter.StepReportOptions;
import com.twosigma.webtau.reporter.ValueMatcherExpectationSteps;

import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

interface DataNodeExpectations extends ActualValueExpectations, ActualPathAware {
    @Override
    default void should(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldStep(null, this, StepReportOptions.SKIP_START,
                tokenizedMessage(IntegrationTestsMessageBuilder.id(actualPath().getPath())), valueMatcher);
    }

    @Override
    default void shouldNot(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldNotStep(null, this, StepReportOptions.SKIP_START,
                tokenizedMessage(IntegrationTestsMessageBuilder.id(actualPath().getPath())), valueMatcher);
    }

    @Override
    default void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        throw new UnsupportedOperationException();
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
