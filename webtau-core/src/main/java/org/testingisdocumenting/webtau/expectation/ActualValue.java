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

package org.testingisdocumenting.webtau.expectation;

import org.testingisdocumenting.webtau.expectation.ExpectationHandler.Flow;
import org.testingisdocumenting.webtau.expectation.timer.ExpectationTimer;
import org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.util.function.Function;

import static org.testingisdocumenting.webtau.WebTauCore.createActualPath;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.*;

public class ActualValue implements ActualValueExpectations {
    private final Object actual;
    private final TokenizedMessage valueDescription;
    private final ActualPath actualPath;
    private final StepReportOptions shouldReportOptions;

    public ActualValue(Object actual) {
        this(actual, StepReportOptions.SKIP_START);
    }

    public ActualValue(Object actual, StepReportOptions shouldReportOptions) {
        this.actual = actual;
        this.actualPath = extractPath(actual);
        this.valueDescription = extractDescription(actual, this.actualPath);
        this.shouldReportOptions = shouldReportOptions;
    }

    @Override
    public void should(ValueMatcher valueMatcher) {
        executeStep(actual, valueDescription, valueMatcher, false,
                tokenizedMessage(action("expecting")),
                () -> shouldStep(valueMatcher), shouldReportOptions);
    }

    @Override
    public void shouldNot(ValueMatcher valueMatcher) {
        executeStep(actual, valueDescription, valueMatcher, true,
                tokenizedMessage(action("expecting")),
                () -> shouldNotStep(valueMatcher), shouldReportOptions);
    }

    @Override
    public void waitTo(ValueMatcher valueMatcher,
                     ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        executeStep(actual, valueDescription, valueMatcher, false,
                tokenizedMessage(action("waiting"), TO),
                () -> waitToStep(valueMatcher, expectationTimer, tickMillis, timeOutMillis),
                StepReportOptions.REPORT_ALL);
    }

    @Override
    public void waitToNot(ValueMatcher valueMatcher,
                          ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        executeStep(actual, valueDescription, valueMatcher, true,
                tokenizedMessage(action("waiting"), TO),
                () -> waitToNotStep(valueMatcher, expectationTimer, tickMillis, timeOutMillis),
                StepReportOptions.REPORT_ALL);
    }

    private void shouldStep(ValueMatcher valueMatcher) {
        boolean matches = valueMatcher.matches(actualPath, actual);

        if (matches) {
            handleMatch(valueMatcher);
        } else {
            handleMismatch(valueMatcher, mismatchMessage(valueMatcher, false));
        }
    }

    private void shouldNotStep(ValueMatcher valueMatcher) {
        boolean matches = valueMatcher.negativeMatches(actualPath, actual);

        if (matches) {
            handleMatch(valueMatcher);
        } else {
            handleMismatch(valueMatcher, mismatchMessage(valueMatcher, true));
        }
    }

    private void waitToStep(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        waitImpl(valueMatcher, expectationTimer, tickMillis, timeOutMillis, (result) -> result, false);
    }

    private void waitToNotStep(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        waitImpl(valueMatcher, expectationTimer, tickMillis, timeOutMillis, (result) -> ! result, true);
    }

    private void waitImpl(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis,
                         Function<Boolean, Boolean> terminate, boolean isNegative) {
        expectationTimer.start();
        while (! expectationTimer.hasTimedOut(timeOutMillis)) {
            boolean matches = valueMatcher.matches(actualPath, actual);
            if (terminate.apply(matches)) {
                handleMatch(valueMatcher);
                return;
            }

            expectationTimer.tick(tickMillis);
        }

        handleMismatch(valueMatcher, mismatchMessage(valueMatcher, isNegative));
    }

    private void handleMatch(ValueMatcher valueMatcher) {
        ExpectationHandlers.onValueMatch(valueMatcher, actualPath, actual);
    }

    private void handleMismatch(ValueMatcher valueMatcher, String message) {
        final Flow flow = ExpectationHandlers.onValueMismatch(valueMatcher, actualPath, actual, message);

        if (flow != Flow.Terminate) {
            throw new AssertionError("\n" + message);
        }
    }

    private String mismatchMessage(ValueMatcher matcher, boolean isNegative) {
        return isNegative ?
                matcher.negativeMismatchedMessage(actualPath, actual):
                matcher.mismatchedMessage(actualPath, actual);
    }

    private static ActualPath extractPath(Object actual) {
        return (actual instanceof ActualPathAndDescriptionAware) ?
                (((ActualPathAndDescriptionAware) actual).actualPath()):
                createActualPath("[value]");
    }

    private static TokenizedMessage extractDescription(Object actual, ActualPath path) {
        return (actual instanceof ActualPathAndDescriptionAware) ?
                (((ActualPathAndDescriptionAware) actual).describe()):
                TokenizedMessage.tokenizedMessage(IntegrationTestsMessageBuilder.id(path.getPath()));
    }

    private static void executeStep(Object value, TokenizedMessage elementDescription,
                                    ValueMatcher valueMatcher, boolean isNegative,
                                    TokenizedMessage messageStart, Runnable expectationValidation,
                                    StepReportOptions stepReportOptions) {
        WebTauStep step = createStep(
                messageStart.add(elementDescription)
                        .add(matcher(isNegative ? valueMatcher.negativeMatchingMessage() : valueMatcher.matchingMessage())),
                () -> tokenizedMessage(elementDescription)
                        .add(matcher(isNegative ?
                                valueMatcher.negativeMatchedMessage(null, value) :
                                valueMatcher.matchedMessage(null, value))),
                expectationValidation);

        step.execute(stepReportOptions);
    }
}
