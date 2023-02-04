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

package org.testingisdocumenting.webtau.reporter;

import org.testingisdocumenting.webtau.persona.Persona;
import org.testingisdocumenting.webtau.time.Time;
import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;
import static org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils.renderStackTrace;
import static java.util.stream.Collectors.toList;
import static org.testingisdocumenting.webtau.utils.FunctionUtils.*;

public class WebTauStep {
    private final String personaId;

    private final TokenizedMessage inProgressMessage;
    private final Function<Object, TokenizedMessage> completionMessageFunc;
    private final Function<WebTauStepContext, Object> action;
    private TokenizedMessage completionMessage;

    private boolean isInProgress;
    private boolean isSuccessful;

    private final List<WebTauStep> children;
    private WebTauStep parent;
    private String stackTrace;

    private WebTauStepInput input = WebTauStepInput.EMPTY;
    private WebTauStepOutput output = WebTauStepOutput.EMPTY;

    private Function<Object, WebTauStepOutput> stepOutputFunc = null;

    private long startTime;
    private long elapsedTime;

    private int totalNumberOfAttempts;

    private String classifier;

    // when true, nested matcher steps won't render extra details (step output pretty print) in case of failures
    // e.g. HTTP step consolidates all the failures/matches and renders a single response with details
    private boolean isMatcherOutputDisabled;

    private static final ThreadLocal<WebTauStep> currentStep = new ThreadLocal<>();

    public static WebTauStep createStep(TokenizedMessage inProgressMessage,
                                        Supplier<TokenizedMessage> completionMessageSupplier,
                                        Runnable action) {
        return createStep(inProgressMessage, completionMessageSupplier, toSupplier(action));
    }

    public static WebTauStep createStep(TokenizedMessage inProgressMessage,
                                        Function<Object, TokenizedMessage> completionMessageFunc,
                                        Supplier<Object> action) {
        return createStep(0, inProgressMessage, completionMessageFunc, toFunction(action));
    }

    public static WebTauStep createStep(TokenizedMessage inProgressMessage,
                                        Supplier<TokenizedMessage> completionMessageSupplier,
                                        Supplier<Object> action) {
        return createStep(0, inProgressMessage, completionMessageSupplier, action);
    }

    public static WebTauStep createStep(long startTime,
                                        TokenizedMessage inProgressMessage,
                                        Supplier<TokenizedMessage> completionMessageSupplier,
                                        Supplier<Object> action) {
        return createStep(startTime, inProgressMessage,
                (stepResult) -> completionMessageSupplier.get(),
                toFunction(action));
    }

    public static WebTauStep createStep(long startTime,
                                        TokenizedMessage inProgressMessage,
                                        Supplier<TokenizedMessage> completionMessageSupplier,
                                        Runnable action) {
        return createStep(startTime, inProgressMessage,
                (stepResult) -> completionMessageSupplier.get(),
                toFunction(action));
    }

    public static WebTauStep createStep(long startTime,
                                        TokenizedMessage inProgressMessage,
                                        Function<Object, TokenizedMessage> completionMessageFunc,
                                        Function<WebTauStepContext, Object> action) {
        WebTauStep step = new WebTauStep(startTime, inProgressMessage, completionMessageFunc, action);
        WebTauStep localCurrentStep = WebTauStep.currentStep.get();

        step.parent = localCurrentStep;
        if (localCurrentStep != null) {
            localCurrentStep.children.add(step);
        }
        currentStep.set(step);

        return step;
    }

    public static WebTauStep createRepeatStep(String label, int numberOfAttempts, Function<WebTauStepContext, Object> action) {
        WebTauStep step = WebTauStep.createStep(0,
                tokenizedMessage(action("repeat " + label), numberValue(numberOfAttempts), classifier("times")),
                (ignored) -> tokenizedMessage(action("repeated " + label), numberValue(numberOfAttempts), classifier("times")),
                action);
        step.setTotalNumberOfAttempts(numberOfAttempts);

        return step;
    }

    public static void createAndExecuteStep(TokenizedMessage inProgressMessage,
                                            Function<Object, TokenizedMessage> completionMessageFunc,
                                            Supplier<Object> action,
                                            StepReportOptions stepReportOptions) {
        WebTauStep step = createStep(inProgressMessage, completionMessageFunc, action);
        step.execute(stepReportOptions);
    }

    public static void createAndExecuteStep(TokenizedMessage inProgressMessage,
                                            Supplier<TokenizedMessage> completionMessageSupplier,
                                            Runnable action,
                                            StepReportOptions stepReportOptions) {
        WebTauStep step = createStep(inProgressMessage, completionMessageSupplier, toSupplier(action));
        step.execute(stepReportOptions);
    }

    public static void createAndExecuteStep(TokenizedMessage inProgressMessage,
                                            Supplier<TokenizedMessage> completionMessageSupplier,
                                            Runnable action) {
        createAndExecuteStep(inProgressMessage, completionMessageSupplier,
                action, StepReportOptions.REPORT_ALL);
    }

    public static void createAndExecuteStep(TokenizedMessage inProgressMessage,
                                            WebTauStepInput input,
                                            Supplier<TokenizedMessage> completionMessageSupplier,
                                            Runnable action) {
        WebTauStep step = createStep(inProgressMessage, completionMessageSupplier, toSupplier(action));
        step.setInput(input);
        step.execute(StepReportOptions.REPORT_ALL);
    }

    public static void createAndExecuteStep(TokenizedMessage inProgressMessage,
                                            WebTauStepInput input,
                                            Supplier<TokenizedMessage> completionMessageSupplier,
                                            Function<Object, WebTauStepOutput> output,
                                            Runnable action) {
        WebTauStep step = createStep(inProgressMessage, completionMessageSupplier, toSupplier(action));
        step.setInput(input);
        step.setStepOutputFunc(output);
        step.execute(StepReportOptions.REPORT_ALL);
    }

    public static void createAndExecuteStep(Supplier<TokenizedMessage> completionMessageSupplier,
                                            Runnable action) {
        createAndExecuteStep(TokenizedMessage.tokenizedMessage(), completionMessageSupplier,
                action, StepReportOptions.SKIP_START);
    }

    public static WebTauStep getCurrentStep() {
        return currentStep.get();
    }

    private WebTauStep(long startTime,
                       TokenizedMessage inProgressMessage,
                       Function<Object, TokenizedMessage> completionMessageFunc,
                       Function<WebTauStepContext, Object> action) {
        this.personaId = Persona.getCurrentPersona().getId();
        this.startTime = startTime;
        this.children = new ArrayList<>();
        this.inProgressMessage = inProgressMessage;
        this.completionMessageFunc = completionMessageFunc;
        this.action = action;
        this.isInProgress = true;
        this.totalNumberOfAttempts = 1;
        this.classifier = "";
    }

    public Stream<WebTauStep> children() {
        return children.stream();
    }

    public void setInput(WebTauStepInput input) {
        this.input = input;
    }

    public WebTauStepInput getInput() {
        return input;
    }

    public void setStepOutputFunc(Function<Object, WebTauStepOutput> stepOutputFunc) {
        this.stepOutputFunc = stepOutputFunc;
    }

    public void setOutput(WebTauStepOutput output) {
        this.output = output;
    }

    public WebTauStepOutput getOutput() {
        return output;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getClassifier() {
        return classifier;
    }

    public boolean isMatcherOutputDisabled() {
        return isMatcherOutputDisabled;
    }

    public void setMatcherOutputDisabled(boolean isMatcherOutputDisabled) {
        this.isMatcherOutputDisabled = isMatcherOutputDisabled;
    }

    public boolean hasParentWithDisabledMatcherOutput() {
        WebTauStep it = parent;
        while (it != null) {
            if (it.isMatcherOutputDisabled()) {
                return true;
            }
            it = parent.parent;
        }

        return false;
    }

    public Stream<WebTauStepOutput> collectOutputs() {
        Stream<WebTauStepOutput> result = output.isEmpty() ? Stream.empty() : Stream.of(output);
        Stream<WebTauStepOutput> childrenOutputs = children.stream().flatMap(WebTauStep::collectOutputs);

        return Stream.concat(result, childrenOutputs);
    }

    @SuppressWarnings("unchecked")
    public <V extends WebTauStepOutput> Stream<V> collectOutputsOfType(Class<V> type) {
        return collectOutputs()
                .filter(p -> p.getClass().isAssignableFrom(type))
                .map(p -> (V) p);
    }

    public boolean hasOutput(Class<? extends WebTauStepOutput> type) {
        return collectOutputsOfType(type).findAny().isPresent();
    }

    public Stream<WebTauStep> stepsWithClassifier(String classifier) {
        Stream<WebTauStep> self = this.classifier.equals(classifier) ? Stream.of(this) : Stream.empty();
        Stream<WebTauStep> children = children().flatMap(childStep -> childStep.stepsWithClassifier(classifier));

        return Stream.concat(self, children);
    }

    public boolean hasFailedChildrenSteps() {
        return children.stream().anyMatch(WebTauStep::isFailed);
    }

    public Optional<WebTauStep> findFailedChildStep() {
        return children.stream().filter(WebTauStep::isFailed).findFirst();
    }

    public void setTotalNumberOfAttempts(int totalNumberOfAttempts) {
        this.totalNumberOfAttempts = totalNumberOfAttempts;
    }

    public int calcNumberOfSuccessfulSteps() {
        return ((isSuccessful ? 1 : 0) +
                children.stream().map(WebTauStep::calcNumberOfSuccessfulSteps).reduce(0, Integer::sum));
    }

    public int calcNumberOfFailedSteps() {
        return ((isFailed() ? 1 : 0) +
                children.stream().map(WebTauStep::calcNumberOfFailedSteps).reduce(0, Integer::sum));
    }

    public int getNumberOfParents() {
        int result = 0;
        WebTauStep step = this;
        while (step.parent != null) {
            result++;
            step = step.parent;
        }

        return result;
    }

    public String getPersonaId() {
        return personaId;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public boolean isFailed() {
        return !isSuccessful;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public <R> R execute(StepReportOptions stepReportOptions) {
        if (totalNumberOfAttempts == 1) {
            return executeSingleRun(stepReportOptions);
        } else {
            return executeMultipleRuns(stepReportOptions);
        }
    }

    private <R> R executeSingleRun(StepReportOptions stepReportOptions) {
        return executeSingleRunWithAction(stepReportOptions, action);
    }

    @SuppressWarnings("unchecked")
    private <R> R executeSingleRunWithAction(StepReportOptions stepReportOptions,
                                             Function<WebTauStepContext, Object> actionToUse) {
        try {
            if (stepReportOptions != StepReportOptions.SKIP_START && stepReportOptions != StepReportOptions.SKIP_ALL) {
                StepReporters.onStart(this);
            }

            startClock();
            Object result = actionToUse.apply(WebTauStepContext.SINGLE_RUN);
            complete(completionMessageFunc.apply(result));
            stopClock();

            if (!output.isEmpty() && stepOutputFunc != null) {
                throw new IllegalStateException("output and outputSupplier is provided before test is executed, only one is allowed");
            }

            if (stepOutputFunc != null) {
                output = stepOutputFunc.apply(result);
            }

            if (stepReportOptions != StepReportOptions.SKIP_ALL) {
                StepReporters.onSuccess(this);
            }

            return (R) result;
        } catch (Throwable e) {
            stopClock();

            fail(e);
            if (stepOutputFunc != null) {
                output = stepOutputFunc.apply(null);
            }

            StepReporters.onFailure(this);

            // to avoid full mismatch reports printing twice
            if (e instanceof AssertionError) {
                throw new AssertionError(reduceMismatchedMessage(e.getMessage()));
            } else {
                throw e;
            }
        } finally {
            WebTauStep localCurrentStep = WebTauStep.currentStep.get();
            if (localCurrentStep != null) {
                currentStep.set(localCurrentStep.parent);
            }
        }
    }

    private String reduceMismatchedMessage(String message) {
        if (StringUtils.numberOfLines(message) == 1) {
            return message;
        }

        String seeMoreLabel = "see the failed assertion details above";
        if (message.equals(seeMoreLabel)) {
            return message;
        }

        return seeMoreLabel;
    }

    private <R> R executeMultipleRuns(StepReportOptions stepReportOptions) {
        WebTauStep repeatRoot = getCurrentStep();
        R result = executeSingleRunWithAction(stepReportOptions, multipleRunsActionWrapper(stepReportOptions));

        reduceRepeatedChildren(repeatRoot);

        return result;
    }

    private Function<WebTauStepContext, Object> multipleRunsActionWrapper(StepReportOptions stepReportOptions) {
        return (context) -> {
            int attemptIdx = 0;
            while (attemptIdx < totalNumberOfAttempts) {
                boolean reportStep = shouldReportStepAttemptDuringRepeat(attemptIdx);

                int finalAttemptIdx = attemptIdx;
                MessageToken repeatAction = action("repeat #" + (finalAttemptIdx + 1));
                WebTauStep repeatedStep = WebTauStep.createStep(tokenizedMessage(repeatAction),
                        () -> tokenizedMessage(classifier("completed"), repeatAction),
                        () -> action.apply(new WebTauStepContext(finalAttemptIdx, totalNumberOfAttempts)));

                if (!reportStep) {
                    StepReporters.onStepRepeatStart(repeatedStep, attemptIdx, totalNumberOfAttempts);
                    try {
                        StepReporters.withoutReporters(() -> { repeatedStep.execute(stepReportOptions); return null; });
                        StepReporters.onStepRepeatSuccess(repeatedStep, attemptIdx, totalNumberOfAttempts);
                    } catch (Throwable e) {
                        StepReporters.onStepRepeatFailure(repeatedStep, attemptIdx, totalNumberOfAttempts);
                    }
                } else {
                    repeatedStep.execute(stepReportOptions);
                }

                attemptIdx++;
            }

            return null;
        };
    }

    private void reduceRepeatedChildren(WebTauStep repeatRoot) {
        ListIterator<WebTauStep> it = repeatRoot.children.listIterator(repeatRoot.children.size());
        int idx = repeatRoot.children.size();
        while (it.hasPrevious()) {
            WebTauStep step = it.previous();
            idx--;
            if (step.isSuccessful && !shouldReportStepAttemptDuringRepeat(idx)) {
                it.remove();
            }
        }
    }

    private boolean shouldReportStepAttemptDuringRepeat(int attemptIdx) {
        return attemptIdx == 0 || attemptIdx == (totalNumberOfAttempts - 1);
    }

    private void startClock() {
        if (startTime == 0) {
            startTime = Time.currentTimeMillis();
        }
    }

    private void stopClock() {
        elapsedTime = Time.currentTimeMillis() - startTime;
    }

    public TokenizedMessage getInProgressMessage() {
        return inProgressMessage;
    }

    public TokenizedMessage getCompletionMessage() {
        return completionMessage;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("message", completionMessage.toListOfMaps());
        result.put("startTime", startTime);
        result.put("elapsedTime", elapsedTime);

        if (!classifier.isEmpty()) {
            result.put("classifier", classifier);
        }

        if (!personaId.isEmpty()) {
            result.put("personaId", personaId);
        }

        if (!children.isEmpty()) {
            result.put("children", children.stream().map(WebTauStep::toMap).collect(toList()));
        }

        if (input != WebTauStepInput.EMPTY) {
            Map<String, Object> inputMap = new LinkedHashMap<>();
            inputMap.put("type", input.getClass().getSimpleName());
            inputMap.put("data", input.toMap());
            result.put("input", inputMap);
        }

        if (output != WebTauStepOutput.EMPTY) {
            Map<String, Object> outputMap = new LinkedHashMap<>();
            outputMap.put("type", output.getClass().getSimpleName());
            outputMap.put("data", output.toMap());
            result.put("output", outputMap);
        }

        return result;
    }

    private void complete(TokenizedMessage message) {
        isInProgress = false;
        isSuccessful = true;
        completionMessage = message;
    }

    private void fail(Throwable t) {
        stackTrace = renderStackTrace(t);
        completionMessage = new TokenizedMessage();
        completionMessage.add("error", "failed").add(inProgressMessage).add("delimiter", ":")
                .add("error", t.getMessage());
    }
}
