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

import org.testingisdocumenting.webtau.data.MultilineString;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.expectation.ExpectationHandler.Flow;
import org.testingisdocumenting.webtau.expectation.stepoutput.ValueMatcherStepOutput;
import org.testingisdocumenting.webtau.expectation.timer.ExpectationTimer;
import org.testingisdocumenting.webtau.reporter.*;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.*;

public class ActualValue implements ActualValueExpectations {
    private Object actualExtracted;
    private final Object actualGiven;
    private final TokenizedMessage valueDescription;
    private final ValuePath actualPath;
    private final StepReportOptions shouldReportOptions;
    private boolean isDisabledStepOutput;

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
        this.actualGiven = actual;
        this.actualPath = actualPath != ValuePath.UNDEFINED ? actualPath : extractPath(actual);
        this.valueDescription = extractDescription(actual, this.actualPath);
        this.shouldReportOptions = shouldReportOptions;
    }

    public void setDisabledStepOutput(boolean disabledStepOutput) {
        isDisabledStepOutput = disabledStepOutput;
    }

    @Override
    public void should(ValueMatcher valueMatcher) {
        executeStepWithDelayedStepReporting(valueMatcher, false,
                tokenizedMessage().action("expecting"),
                () -> shouldStepImpl(valueMatcher), shouldReportOptions);
    }

    @Override
    public void shouldNot(ValueMatcher valueMatcher) {
        executeStepWithDelayedStepReporting(valueMatcher, true,
                tokenizedMessage().action("expecting"),
                () -> shouldNotStepImpl(valueMatcher), shouldReportOptions);
    }

    @Override
    public void waitTo(ValueMatcher valueMatcher,
                       ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        waitToWithModifiedInnerStepsReporting(valueMatcher, false,
                () -> waitImpl(valueMatcher, expectationTimer, tickMillis, timeOutMillis, (result) -> result, false));
    }

    @Override
    public void waitToNot(ValueMatcher valueMatcher,
                          ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        waitToWithModifiedInnerStepsReporting(valueMatcher, true,
                () -> waitImpl(valueMatcher, expectationTimer, tickMillis, timeOutMillis, (result) -> ! result, true));
    }

    private void waitToWithModifiedInnerStepsReporting(ValueMatcher valueMatcher,
                                                       boolean isNegative,
                                                       Supplier<Object> expectationValidation) {
        WebTauStep waitStep = createShouldWaitStep(valueMatcher, isNegative,
                tokenizedMessage().action("waiting").forP(),
                expectationValidation);

        Runnable reportFirstAndLastStep = () -> reportFirstAndLastWaitChild(waitStep);
        waitStep.setOnBeforeSuccessReport(reportFirstAndLastStep);
        waitStep.setOnBeforeFailureReport(reportFirstAndLastStep);

        waitStep.execute(StepReportOptions.REPORT_ALL);
    }

    private static void reportFirstAndLastWaitChild(WebTauStep waitStep) {
        List<WebTauStep> children = waitStep.children().collect(Collectors.toList());
        if (children.isEmpty()) {
            return;
        }

        if (children.size() == 1) {
            reportStepAsIs(children.get(0));
        } else {
            reportFirstAndLastModified(children);
        }
    }

    private static void reportStepAsIs(WebTauStep step) {
        StepReporters.onStart(step);
        if (step.isFailed()) {
            StepReporters.onFailure(step);
        } else {
            StepReporters.onSuccess(step);
        }
    }

    private static void reportFirstAndLastModified(List<WebTauStep> children) {
        WebTauStep first = children.get(0);
        WebTauStep last = children.get(children.size() - 1);

        reportStepWithModifiedMessage(first, "[1/" + children.size() + "]");
        reportStepWithModifiedMessage(last, "[" + children.size() + "/" + children.size() + "]");

    }

    private static void reportStepWithModifiedMessage(WebTauStep step, String classifierPrefix) {
        Function<TokenizedMessage, TokenizedMessage> messageModifier = (message) -> tokenizedMessage().classifier(classifierPrefix).add(message);
        step.setInProgressMessageModifier(messageModifier);
        step.setCompletionMessageModifier(messageModifier);

        step.reReportStep();
    }

    private boolean shouldStepImpl(ValueMatcher valueMatcher) {
        boolean matches = valueMatcher.matches(actualPath, extractAndCacheActualValue(actualGiven, 0, 0, 0));

        if (matches) {
            handleMatch(valueMatcher);
        } else {
            handleMismatch(valueMatcher, mismatchMessage(valueMatcher, false));
        }

        return matches;
    }

    private boolean shouldNotStepImpl(ValueMatcher valueMatcher) {
        boolean matches = valueMatcher.negativeMatches(actualPath, extractAndCacheActualValue(actualGiven, 0, 0, 0));

        if (matches) {
            handleMatch(valueMatcher);
        } else {
            handleMismatch(valueMatcher, mismatchMessage(valueMatcher, true));
        }

        return matches;
    }

    private boolean waitImpl(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis,
                             Function<Boolean, Boolean> isMatchedFunc, boolean isNegative) {
        return StepReporters.withoutReporters(() -> {
            expectationTimer.start();

            int attemptIdx = 0;
            while (!expectationTimer.hasTimedOut(timeOutMillis)) {
                boolean matches = valueMatcher.matches(actualPath, extractAndCacheActualValue(actualGiven, attemptIdx, tickMillis, timeOutMillis));
                if (isMatchedFunc.apply(matches)) {
                    handleMatch(valueMatcher);
                    return true;
                }

                expectationTimer.tick(tickMillis);
                attemptIdx++;
            }

            handleMismatch(valueMatcher, mismatchMessage(valueMatcher, isNegative));
            return false;
        });
    }

    private void handleMatch(ValueMatcher valueMatcher) {
        ExpectationHandlers.onValueMatch(valueMatcher, actualPath, actualExtracted);
    }

    private void handleMismatch(ValueMatcher valueMatcher, TokenizedMessage message) {
        final Flow flow = ExpectationHandlers.onValueMismatch(valueMatcher, actualPath, actualExtracted, message);

        if (flow != Flow.Terminate) {
            throw new AssertionTokenizedError(message);
        }
    }

    private TokenizedMessage mismatchMessage(ValueMatcher matcher, boolean isNegative) {
        return isNegative ?
                matcher.negativeMismatchedTokenizedMessage(actualPath, actualExtracted):
                matcher.mismatchedTokenizedMessage(actualPath, actualExtracted);
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

    private Object extractAndCacheActualValue(Object actual, int attemptIdx, long tickMillis, long timeOutMillis) {
        if (actual instanceof ActualValueAware) {
            actualExtracted = ((ActualValueAware) actual).actualValue(attemptIdx, tickMillis, timeOutMillis);
        } else {
            actualExtracted = actual;
        }

        return actualExtracted;
    }

    private void executeStepWithDelayedStepReporting(ValueMatcher valueMatcher, boolean isNegative,
                                                     TokenizedMessage messageStart,
                                                     Supplier<Object> expectationValidation,
                                                     StepReportOptions stepReportOptions) {
        boolean delayedStepReporting = actualGiven instanceof ActualValueAware;

        Supplier<Object> wrappedValidation = delayedStepReporting ?
                () -> StepReporters.withoutReporters(expectationValidation) :
                expectationValidation;

        WebTauStep step = createShouldWaitStep(valueMatcher, isNegative, messageStart, wrappedValidation);

        if (delayedStepReporting) {
            Runnable reReportChildren = () -> step.children().forEach(WebTauStep::reReportStep);
            step.setOnBeforeSuccessReport(reReportChildren);
            step.setOnBeforeFailureReport(reReportChildren);
        }

        step.execute(stepReportOptions);
    }

    private WebTauStep createShouldWaitStep(ValueMatcher valueMatcher, boolean isNegative,
                                            TokenizedMessage messageStart,
                                            Supplier<Object> expectationValidation) {
        WebTauStep step = createStep(
                messageStart.add(valueDescription)
                        .add(isNegative ?
                                valueMatcher.negativeMatchingTokenizedMessage(actualPath, actualGiven):
                                valueMatcher.matchingTokenizedMessage(actualPath, actualGiven)),
                () -> tokenizedMessage(valueDescription)
                        .add(isNegative ?
                                valueMatcher.negativeMatchedTokenizedMessage(actualPath, actualGiven) :
                                valueMatcher.matchedTokenizedMessage(actualPath, actualGiven)),
                expectationValidation);
        step.setClassifier(WebTauStepClassifiers.MATCHER);
        step.setValueConverter(valueMatcher.valueConverter());

        if (isDisabledStepOutput) {
            return step;
        }

        step.setStepOutputFunc((matched) -> {
            ValueConverter valueConverter = step.getValueConverter();
            Object convertedActual = valueConverter.convertValue(actualPath, actualExtracted);

            // if we already displayed the actual value as part of mismatch message, we don't need to display it again even if it is pretty printable
            TokenizedMessage assertionTokenizedMessage = step.getExceptionTokenizedMessage();
            if (assertionTokenizedMessage.tokensStream()
                    .filter(MessageToken::isPrettyPrintValue)
                    .anyMatch(token -> matchAsPartOfMessage(convertedActual, token.value()))) {
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

        return step;
    }

    private boolean matchAsPartOfMessage(Object convertedActual, Object value) {
        if (value == actualExtracted || value == convertedActual) {
            return true;
        }

        if (value instanceof CharSequence valueCs && convertedActual instanceof MultilineString multilineString) {
            return multilineString.getText().contentEquals(valueCs);
        }

        if (value instanceof CharSequence valueCs && actualExtracted instanceof MultilineString multilineString) {
            return multilineString.getText().contentEquals(valueCs);
        }

        return false;
    }

    private static boolean keepRootActualPathDecorated(Object actual) {
        return PrettyPrinter.findPrettyPrintable(actual)
                .map(PrettyPrintable::handlesDecoration).orElse(false);
    }
}
