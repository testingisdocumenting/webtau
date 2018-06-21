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

import com.twosigma.webtau.expectation.ExpectationHandler.Flow;
import com.twosigma.webtau.expectation.timer.ExpectationTimer;

import java.util.function.Function;

import static com.twosigma.webtau.Ddjt.createActualPath;

public class ActualValue implements ActualValueExpectations {
    private Object actual;

    public ActualValue(Object actual) {
        this.actual = actual;
    }

    @Override
    public void should(ValueMatcher valueMatcher) {
        ActualPath actualPath = extractPath(actual);
        boolean matches = valueMatcher.matches(actualPath, actual);

        if (!matches) {
            handleMismatch(valueMatcher, mismatchMessage(valueMatcher, actualPath, false), actualPath);
        }
    }

    @Override
    public void shouldNot(ValueMatcher valueMatcher) {
        ActualPath actualPath = extractPath(actual);
        boolean matches = valueMatcher.negativeMatches(actualPath, actual);

        if (!matches) {
            handleMismatch(valueMatcher, mismatchMessage(valueMatcher, actualPath, true), actualPath);
        }
    }

    @Override
    public void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        waitImpl(valueMatcher, expectationTimer, tickMillis, timeOutMillis, (result) -> result, false);
    }

    @Override
    public void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        waitImpl(valueMatcher, expectationTimer, tickMillis, timeOutMillis, (result) -> ! result, true);
    }

    private void waitImpl(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis,
                         Function<Boolean, Boolean> terminate, boolean isNegative) {
        ActualPath actualPath = extractPath(actual);

        expectationTimer.start();
        while (! expectationTimer.hasTimedOut(timeOutMillis)) {
            boolean matches = valueMatcher.matches(actualPath, actual);
            if (terminate.apply(matches)) {
                return;
            }

            expectationTimer.tick(tickMillis);
        }

        handleMismatch(valueMatcher, mismatchMessage(valueMatcher, actualPath, isNegative), actualPath);
    }

    private void handleMismatch(ValueMatcher valueMatcher, String message, ActualPath actualPath) {
        final Flow flow = ExpectationHandlers.onValueMismatch(valueMatcher, actualPath, actual, message);

        if (flow != Flow.Terminate) {
            throw new AssertionError("\n" + message);
        }
    }

    private ActualPath extractPath(Object actual) {
        return (actual instanceof ActualPathAware) ?
            (((ActualPathAware) actual).actualPath()):
            createActualPath("[value]");
    }

    private String mismatchMessage(ValueMatcher matcher, ActualPath actualPath, boolean isNegative) {
        return isNegative ?
                matcher.negativeMismatchedMessage(actualPath, actual):
                matcher.mismatchedMessage(actualPath, actual);
    }
}
