/*
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

package org.testingisdocumenting.webtau.expectation;

import org.testingisdocumenting.webtau.expectation.timer.ExpectationTimer;
import org.testingisdocumenting.webtau.expectation.timer.ExpectationTimerConfigProvider;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;

public interface ActualValueExpectations {
    default StepReportOptions shouldReportOption() {
        return StepReportOptions.SKIP_START;
    }

    default boolean isDisabledMatcherStepOutput() {
        return false;
    }

    default ExpectationTimer createExpectationTimer() {
        return ExpectationTimerConfigProvider.createExpectationTimer();
    }

    default void should(ValueMatcher valueMatcher) {
        ActualValue actualValue = new ActualValue(this, shouldReportOption());
        actualValue.setDisabledStepOutput(isDisabledMatcherStepOutput());

        actualValue.should(valueMatcher);
    }

    default void shouldNot(ValueMatcher valueMatcher) {
        ActualValue actualValue = new ActualValue(this, shouldReportOption());
        actualValue.setDisabledStepOutput(isDisabledMatcherStepOutput());

        actualValue.shouldNot(valueMatcher);
    }

    default void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ActualValue actualValue = new ActualValue(this);
        actualValue.setDisabledStepOutput(isDisabledMatcherStepOutput());

        actualValue.waitTo(valueMatcher, expectationTimer, tickMillis, timeOutMillis);
    }

    default void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ActualValue actualValue = new ActualValue(this);
        actualValue.setDisabledStepOutput(isDisabledMatcherStepOutput());

        actualValue.waitToNot(valueMatcher, expectationTimer, tickMillis, timeOutMillis);
    }

    default void shouldBe(ValueMatcher valueMatcher) {
        should(valueMatcher);
    }

    default void shouldNotBe(ValueMatcher valueMatcher) {
        shouldNot(valueMatcher);
    }

    default void waitTo(ValueMatcher valueMatcher) {
        waitTo(valueMatcher, createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                ExpectationTimerConfigProvider.defaultTimeoutMillis());
    }

    default void waitToBe(ValueMatcher valueMatcher) {
        waitTo(valueMatcher);
    }

    default void waitTo(ValueMatcher valueMatcher, long tickMillis, long timeOutMillis) {
        waitTo(valueMatcher, createExpectationTimer(),
                tickMillis,
                timeOutMillis);
    }

    default void waitToBe(ValueMatcher valueMatcher, long tickMillis, long timeOutMillis) {
        waitTo(valueMatcher, tickMillis, timeOutMillis);
    }

    default void waitTo(ValueMatcher valueMatcher, long timeOutMillis) {
        waitTo(valueMatcher, createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                timeOutMillis);
    }

    default void waitToBe(ValueMatcher valueMatcher, long timeOutMillis) {
        waitTo(valueMatcher, timeOutMillis);
    }

    default void waitToNot(ValueMatcher valueMatcher) {
        waitToNot(valueMatcher, createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                ExpectationTimerConfigProvider.defaultTimeoutMillis());
    }

    default void waitToNotBe(ValueMatcher valueMatcher) {
        waitToNot(valueMatcher);
    }

    default void waitToNot(ValueMatcher valueMatcher, long tickMillis, long timeOutMillis) {
        waitToNot(valueMatcher, createExpectationTimer(),
                tickMillis,
                timeOutMillis);
    }

    default void waitToNotBe(ValueMatcher valueMatcher, long tickMillis, long timeOutMillis) {
        waitToNot(valueMatcher, tickMillis, timeOutMillis);
    }

    default void waitToNot(ValueMatcher valueMatcher, long timeOutMillis) {
        waitToNot(valueMatcher, createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                timeOutMillis);
    }

    default void waitToNotBe(ValueMatcher valueMatcher, long timeOutMillis) {
        waitToNot(valueMatcher, timeOutMillis);
    }
}
