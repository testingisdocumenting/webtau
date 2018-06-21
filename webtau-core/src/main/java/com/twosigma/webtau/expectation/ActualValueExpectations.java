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

package com.twosigma.webtau.expectation;

import com.twosigma.webtau.expectation.timer.ExpectationTimer;
import com.twosigma.webtau.expectation.timer.ExpectationTimerConfigProvider;

public interface ActualValueExpectations {
    void should(ValueMatcher valueMatcher);
    void shouldNot(ValueMatcher valueMatcher);

    void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis);
    void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis);

    default void shouldBe(ValueMatcher valueMatcher) {
        should(valueMatcher);
    }

    default void shouldNotBe(ValueMatcher valueMatcher) {
        shouldNot(valueMatcher);
    }

    default void waitTo(ValueMatcher valueMatcher) {
        waitTo(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                ExpectationTimerConfigProvider.defaultTimeoutMillis());
    }

    default void waitToBe(ValueMatcher valueMatcher) {
        waitTo(valueMatcher);
    }

    default void waitTo(ValueMatcher valueMatcher, long tickMillis, long timeOutMillis) {
        waitTo(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                tickMillis,
                timeOutMillis);
    }

    default void waitToBe(ValueMatcher valueMatcher, long tickMillis, long timeOutMillis) {
        waitTo(valueMatcher, tickMillis, timeOutMillis);
    }

    default void waitTo(ValueMatcher valueMatcher, long timeOutMillis) {
        waitTo(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                timeOutMillis);
    }

    default void waitToBe(ValueMatcher valueMatcher, long timeOutMillis) {
        waitTo(valueMatcher, timeOutMillis);
    }

    default void waitToNot(ValueMatcher valueMatcher) {
        waitToNot(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                ExpectationTimerConfigProvider.defaultTimeoutMillis());
    }

    default void waitToNotBe(ValueMatcher valueMatcher) {
        waitToNot(valueMatcher);
    }

    default void waitToNot(ValueMatcher valueMatcher, long tickMillis, long timeOutMillis) {
        waitToNot(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                tickMillis,
                timeOutMillis);
    }

    default void waitToNotBe(ValueMatcher valueMatcher, long tickMillis, long timeOutMillis) {
        waitToNot(valueMatcher, tickMillis, timeOutMillis);
    }

    default void waitToNot(ValueMatcher valueMatcher, long timeOutMillis) {
        waitToNot(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                timeOutMillis);
    }

    default void waitToNotBe(ValueMatcher valueMatcher, long timeOutMillis) {
        waitToNot(valueMatcher, timeOutMillis);
    }
}
