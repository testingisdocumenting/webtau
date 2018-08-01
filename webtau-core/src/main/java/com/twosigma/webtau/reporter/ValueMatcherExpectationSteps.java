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

package com.twosigma.webtau.reporter;

import com.twosigma.webtau.expectation.ValueMatcher;
import com.twosigma.webtau.expectation.timer.ExpectationTimer;

import static com.twosigma.webtau.Ddjt.actual;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.TO;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.matcher;
import static com.twosigma.webtau.reporter.TestStep.createStep;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class ValueMatcherExpectationSteps {
    public static <C, V> void shouldStep(C context, V value, StepReportOptions stepReportOptions, TokenizedMessage valueDescription, ValueMatcher valueMatcher) {
        executeStep(context, value, valueDescription, valueMatcher, false,
                tokenizedMessage(action("expecting")),
                () -> actual(value).should(valueMatcher), stepReportOptions);
    }

    public static <C, V> void shouldNotStep(C context, V value, StepReportOptions stepReportOptions, TokenizedMessage valueDescription, ValueMatcher valueMatcher) {
        executeStep(context, value, valueDescription, valueMatcher, true,
                tokenizedMessage(action("expecting")),
                () -> actual(value).shouldNot(valueMatcher), stepReportOptions);
    }

    public static <C, V> void waitStep(C context, V value, StepReportOptions stepReportOptions,
                                       TokenizedMessage valueDescription, ValueMatcher valueMatcher,
                                       ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        executeStep(context, value, valueDescription, valueMatcher, false,
                tokenizedMessage(action("waiting"), TO),
                () -> actual(value).waitTo(valueMatcher, expectationTimer, tickMillis, timeOutMillis), stepReportOptions);
    }

    public static <C, V> void waitNotStep(C context, V value, StepReportOptions stepReportOptions,
                                          TokenizedMessage valueDescription, ValueMatcher valueMatcher,
                                          ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        executeStep(context, value, valueDescription, valueMatcher, true,
                tokenizedMessage(action("waiting"), TO),
                () -> actual(value).waitToNot(valueMatcher, expectationTimer, tickMillis, timeOutMillis),
                stepReportOptions);
    }

    private static <C, V> void executeStep(C context, V value, TokenizedMessage elementDescription,
                                           ValueMatcher valueMatcher, boolean isNegative,
                                           TokenizedMessage messageStart, Runnable expectationValidation,
                                           StepReportOptions stepReportOptions) {
        TestStep<C, Void> step = createStep(context,
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
