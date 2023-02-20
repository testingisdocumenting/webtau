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

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.expectation.ExpectationHandler.Flow;
import org.testingisdocumenting.webtau.expectation.stepoutput.ValueMatcherStepOutput;
import org.testingisdocumenting.webtau.expectation.timer.ExpectationTimer;
import org.testingisdocumenting.webtau.reporter.*;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.*;

public class ActualValue implements ActualValueExpectations {
    private final Object actual;
    private final TokenizedMessage valueDescription;
    private final ValuePath actualPath;
    private final StepReportOptions shouldReportOptions;

    public ActualValue(Object actual) {
        this(actual, ValuePath.UNDEFINED);
    }

    public ActualValue(Object actual, ValuePath actualPath) {
        this(actual, actualPath, StepReportOptions.SKIP_START);
    }

    public ActualValue(Object actual, StepReportOptions shouldReportOptions) {
        this(actual, ValuePath.UNDEFINED, shouldReportOptions);
    }

    public ActualValue(Object actual, ValuePath actualPath, StepReportOptions shouldReportOptions) {
        this.actual = extractActualValue(actual);
        this.actualPath = actualPath != ValuePath.UNDEFINED ? actualPath : extractPath(actual);
        this.valueDescription = extractDescription(actual, this.actualPath);
        this.shouldReportOptions = shouldReportOptions;
    }

    @Override
    public void should(ValueMatcher valueMatcher) {
        executeStep(valueMatcher, false,
                tokenizedMessage().action("expecting"),
                () -> shouldStep(valueMatcher), shouldReportOptions);
    }

    @Override
    public void shouldNot(ValueMatcher valueMatcher) {
        executeStep(valueMatcher, true,
                tokenizedMessage().action("expecting"),
                () -> shouldNotStep(valueMatcher), shouldReportOptions);
    }

    @Override
    public void waitTo(ValueMatcher valueMatcher,
                     ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        executeStep(valueMatcher, false,
                tokenizedMessage().action("waiting").forP(),
                () -> waitToStep(valueMatcher, expectationTimer, tickMillis, timeOutMillis),
                StepReportOptions.REPORT_ALL);
    }

    @Override
    public void waitToNot(ValueMatcher valueMatcher,
                          ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        executeStep(valueMatcher, true,
                tokenizedMessage().action("waiting").forP(),
                () -> waitToNotStep(valueMatcher, expectationTimer, tickMillis, timeOutMillis),
                StepReportOptions.REPORT_ALL);
    }

    private boolean shouldStep(ValueMatcher valueMatcher) {
        boolean matches = valueMatcher.matches(actualPath, actual);

        if (matches) {
            handleMatch(valueMatcher);
        } else {
            handleMismatch(valueMatcher, mismatchMessage(valueMatcher, false));
        }

        return matches;
    }

    private boolean shouldNotStep(ValueMatcher valueMatcher) {
        boolean matches = valueMatcher.negativeMatches(actualPath, actual);

        if (matches) {
            handleMatch(valueMatcher);
        } else {
            handleMismatch(valueMatcher, mismatchMessage(valueMatcher, true));
        }

        return matches;
    }

    private boolean waitToStep(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        return waitImpl(valueMatcher, expectationTimer, tickMillis, timeOutMillis, (result) -> result, false);
    }

    private boolean waitToNotStep(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        return waitImpl(valueMatcher, expectationTimer, tickMillis, timeOutMillis, (result) -> ! result, true);
    }

    private boolean waitImpl(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis,
                         Function<Boolean, Boolean> isMatchedFunc, boolean isNegative) {
        expectationTimer.start();
        while (! expectationTimer.hasTimedOut(timeOutMillis)) {
            boolean matches = valueMatcher.matches(actualPath, actual);
            if (isMatchedFunc.apply(matches)) {
                handleMatch(valueMatcher);
                return true;
            }

            expectationTimer.tick(tickMillis);
        }

        handleMismatch(valueMatcher, mismatchMessage(valueMatcher, isNegative));
        return false;
    }

    private void handleMatch(ValueMatcher valueMatcher) {
        ExpectationHandlers.onValueMatch(valueMatcher, actualPath, actual);
    }

    private void handleMismatch(ValueMatcher valueMatcher, TokenizedMessage message) {
        final Flow flow = ExpectationHandlers.onValueMismatch(valueMatcher, actualPath, actual, message);

        if (flow != Flow.Terminate) {
            throw new AssertionTokenizedError(message);
        }
    }

    private TokenizedMessage mismatchMessage(ValueMatcher matcher, boolean isNegative) {
        return isNegative ?
                matcher.negativeMismatchedTokenizedMessage(actualPath, actual):
                matcher.mismatchedTokenizedMessage(actualPath, actual);
    }

    private static ValuePath extractPath(Object actual) {
        return (actual instanceof ActualPathAndDescriptionAware) ?
                (((ActualPathAndDescriptionAware) actual).actualPath()):
                createActualPath("[value]");
    }

    private static TokenizedMessage extractDescription(Object actual, ValuePath path) {
        return (actual instanceof ActualPathAndDescriptionAware) ?
                (((ActualPathAndDescriptionAware) actual).describe()):
                tokenizedMessage().id(path.getPath());
    }

    private Object extractActualValue(Object actual) {
        if (actual instanceof ActualValueAware) {
            return ((ActualValueAware) actual).actualValue();
        }

        return actual;
    }

    private void executeStep(ValueMatcher valueMatcher, boolean isNegative,
                             TokenizedMessage messageStart,
                             Supplier<Object> expectationValidation,
                             StepReportOptions stepReportOptions) {
        WebTauStep step = createStep(
                messageStart.add(valueDescription)
                        .add(isNegative ? valueMatcher.negativeMatchingTokenizedMessage(actualPath, actual): valueMatcher.matchingTokenizedMessage(actualPath, actual)),
                () -> tokenizedMessage(valueDescription)
                        .add(isNegative ?
                                valueMatcher.negativeMatchedTokenizedMessage(null, actual) :
                                valueMatcher.matchedTokenizedMessage(null, actual)),
                expectationValidation);
        step.setClassifier(WebTauStepClassifiers.MATCHER);

        step.setStepOutputFunc((matched) -> {
            ValueConverter valueConverter = valueMatcher.valueConverter();
            Object convertedActual = valueConverter.convertValue(actualPath, actual);

            // if we already displayed the actual value as part of mismatch message, we don't need to display it again even if it is pretty printable
            TokenizedMessage assertionTokenizedMessage = step.getExceptionTokenizedMessage();
            if (assertionTokenizedMessage.tokensStream()
                    .filter(MessageToken::isPrettyPrintValue)
                    .anyMatch(token -> token.getValue() == actual || token.getValue() == convertedActual)) {
                return WebTauStepOutput.EMPTY;
            }

            if (Boolean.TRUE.equals(matched) || !PrettyPrinter.isPrettyPrintable(convertedActual) || step.hasParentWithDisabledMatcherOutputActualValue()) {
                return WebTauStepOutput.EMPTY;
            }

            Set<ValuePath> pathsToDecorate = isNegative ?
                    valueMatcher.matchedPaths() :
                    valueMatcher.mismatchedPaths();

            if (!keepRootActualPathDecorated(convertedActual)) {
                pathsToDecorate.remove(actualPath);
            }

            return new ValueMatcherStepOutput(actualPath,
                    convertedActual,
                    valueConverter,
                    pathsToDecorate);
        });

        step.execute(stepReportOptions);
    }

    private static boolean keepRootActualPathDecorated(Object actual) {
        return PrettyPrinter.findPrettyPrintable(actual)
                .map(PrettyPrintable::handlesDecoration).orElse(false);
    }
}
