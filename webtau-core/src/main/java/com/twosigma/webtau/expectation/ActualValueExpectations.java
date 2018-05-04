package com.twosigma.webtau.expectation;

import com.twosigma.webtau.expectation.timer.ExpectationTimer;
import com.twosigma.webtau.expectation.timer.ExpectationTimerConfigProvider;

public interface ActualValueExpectations {
    void should(ValueMatcher valueMatcher);
    void shouldNot(ValueMatcher valueMatcher);

    void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis);
    void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis);

    default void waitTo(ValueMatcher valueMatcher) {
        waitTo(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                ExpectationTimerConfigProvider.defaultTimeoutMillis());
    }

    default void waitTo(ValueMatcher valueMatcher, long tickMillis, long timeOutMillis) {
        waitTo(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                tickMillis,
                timeOutMillis);
    }

    default void waitTo(ValueMatcher valueMatcher, long timeOutMillis) {
        waitTo(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                timeOutMillis);
    }

    default void waitToNot(ValueMatcher valueMatcher) {
        waitToNot(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                ExpectationTimerConfigProvider.defaultTimeoutMillis());
    }

    default void waitToNot(ValueMatcher valueMatcher, long tickMillis, long timeOutMillis) {
        waitToNot(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                tickMillis,
                timeOutMillis);
    }

    default void waitToNot(ValueMatcher valueMatcher, long timeOutMillis) {
        waitToNot(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                timeOutMillis);
    }
}
