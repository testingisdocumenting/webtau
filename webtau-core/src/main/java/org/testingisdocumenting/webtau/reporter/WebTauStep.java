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

import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.console.ConsoleOutputs;
import org.testingisdocumenting.webtau.console.StringNoAnsiConsoleOutput;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.expectation.AssertionTokenizedError;
import org.testingisdocumenting.webtau.persona.Persona;
import org.testingisdocumenting.webtau.time.Time;
import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils.renderStackTrace;
import static java.util.stream.Collectors.toList;
import static org.testingisdocumenting.webtau.utils.FunctionUtils.*;

public class WebTauStep {
    private final String personaId;

    private final TokenizedMessage inProgressMessage;
    private final Function<Object, TokenizedMessage> completionMessageFunc;
    private final Function<WebTauStepContext, Object> action;
    private TokenizedMessage completionMessage;

    private TokenizedMessage exceptionTokenizedMessage;

    private boolean isInProgress;
    private boolean isSuccessful;

    private StepReportOptions usedReportOptions;

    private final List<WebTauStep> children;
    private WebTauStep parent;
    private String stackTrace;

    private WebTauStepInput input = WebTauStepInput.EMPTY;
    private final List<WebTauStepOutput> outputs = new ArrayList<>();

    private Function<Object, WebTauStepOutput> stepOutputFunc = null;

    private Runnable onBeforeSuccessReport;
    private Runnable onBeforeFailureReport;
    private Function<TokenizedMessage, TokenizedMessage> inProgressMessageModifier;
    private Function<TokenizedMessage, TokenizedMessage> completionMessageModifier;

    private long startTime;
    private long elapsedTime;

    private int totalNumberOfAttempts;

    private String classifier;

    // when true, nested matcher steps won't render extra details (step output pretty print) in case of failures
    // e.g. HTTP step consolidates all the failures/matches and renders a single response with details
    private boolean isMatcherOutputActualValueDisabled;

    // value converter that was used by this step, so pretty printers can use converted(actual/expected) values from e.g. matchers
    private ValueConverter valueConverter = ValueConverter.EMPTY;

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
        return createStepWithExplicitParent(WebTauStep.currentStep.get(), startTime,
                inProgressMessage, completionMessageFunc,
                action);
    }

    public static WebTauStep createStepWithExplicitParent(WebTauStep parent,
                                                          long startTime,
                                                          TokenizedMessage inProgressMessage,
                                                          Function<Object, TokenizedMessage> completionMessageFunc,
                                                          Function<WebTauStepContext, Object> action) {
        WebTauStep step = new WebTauStep(startTime, inProgressMessage, completionMessageFunc, action);

        step.parent = parent;
        if (parent != null) {
            parent.children.add(step);
        }
        currentStep.set(step);

        return step;
    }

    public static WebTauStep createRepeatStep(String label, int numberOfAttempts, Function<WebTauStepContext, Object> action) {
        WebTauStep step = WebTauStep.createStep(0,
                tokenizedMessage().action("repeat " + label).number(numberOfAttempts).classifier("times"),
                (ignored) -> tokenizedMessage().action("repeated " + label).number(numberOfAttempts).classifier("times"),
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
        createAndExecuteStep(tokenizedMessage(), completionMessageSupplier,
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
        this.exceptionTokenizedMessage = tokenizedMessage();
    }

    public WebTauStep getParent() {
        return parent;
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

    public void addOutput(WebTauStepOutput output) {
        this.outputs.add(output);
    }

    public Stream<WebTauStepOutput> getOutputsStream() {
        return outputs.stream();
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setOnBeforeSuccessReport(Runnable onBeforeSuccessReport) {
        this.onBeforeSuccessReport = onBeforeSuccessReport;
    }

    public void setOnBeforeFailureReport(Runnable onBeforeFailureReport) {
        this.onBeforeFailureReport = onBeforeFailureReport;
    }

    public void setInProgressMessageModifier(Function<TokenizedMessage, TokenizedMessage> inProgressMessageModifier) {
        this.inProgressMessageModifier = inProgressMessageModifier;
    }

    public void setCompletionMessageModifier(Function<TokenizedMessage, TokenizedMessage> completionMessageModifier) {
        this.completionMessageModifier = completionMessageModifier;
    }

    public TokenizedMessage getExceptionTokenizedMessage() {
        return exceptionTokenizedMessage;
    }

    public boolean isMatcherOutputActualValueDisabled() {
        return isMatcherOutputActualValueDisabled;
    }

    public void setMatcherOutputActualValueDisabled(boolean isMatcherOutputDisabled) {
        this.isMatcherOutputActualValueDisabled = isMatcherOutputDisabled;
    }

    public boolean hasParentWithDisabledMatcherOutputActualValue() {
        WebTauStep it = parent;
        while (it != null) {
            if (it.isMatcherOutputActualValueDisabled()) {
                return true;
            }
            it = it.parent;
        }

        return false;
    }

    public ValueConverter getValueConverter() {
        return valueConverter;
    }

    public void setValueConverter(ValueConverter valueConverter) {
        this.valueConverter = valueConverter;
    }

    public Stream<WebTauStepOutput> collectOutputs() {
        return Stream.concat(outputs.stream(), children.stream().flatMap(WebTauStep::collectOutputs));
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

    public void reReportStep() {
        if (usedReportOptions != StepReportOptions.SKIP_START) {
            StepReporters.onStart(this);
        }

        children().forEach(WebTauStep::reReportStep);
        if (isFailed()) {
            StepReporters.onFailure(this);
        } else {
            StepReporters.onSuccess(this);
        }
    }

    public <R> R execute(StepReportOptions stepReportOptions) {
        usedReportOptions = stepReportOptions;

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

            if (stepOutputFunc != null) {
                outputs.add(stepOutputFunc.apply(result));
            }

            if (stepReportOptions != StepReportOptions.SKIP_ALL) {
                if (onBeforeSuccessReport != null) {
                    onBeforeSuccessReport.run();
                }

                StepReporters.onSuccess(this);
            }

            return (R) result;
        } catch (Throwable e) {
            stopClock();

            fail(e);
            if (stepOutputFunc != null) {
                outputs.add(stepOutputFunc.apply(null));
            }

            if (onBeforeFailureReport != null) {
                onBeforeFailureReport.run();
            }

            StepReporters.onFailure(this);

            // to avoid full mismatch reports printing twice
            if (e instanceof AssertionTokenizedError) {
                throw new AssertionError(reduceOrEnhanceAssertionMessage(e.getMessage()));
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

    private String reduceOrEnhanceAssertionMessage(String message) {
        // we throw the full message if the details are not rendered to the console
        // or config explicitly asks for full messages at all times
        if (!StepReporters.isConsoleStepReporterActive() ||
                !ConsoleOutputs.isTerminalConsoleOutputActive() ||
                !StepReporters.defaultStepReporter.isWithinVerboseLevel(this) ||
                WebTauConfig.getCfg().isFullAssertionError()) {
            return renderStep(this);
        }

        String seeMoreLabel = "see the failed assertion details above";
        if (message.equals(seeMoreLabel)) {
            return message;
        }

        if (StringUtils.numberOfLines(message) == 1) {
            return message;
        }

        return seeMoreLabel;
    }

    private String renderStep(WebTauStep step) {
        StringNoAnsiConsoleOutput output = new StringNoAnsiConsoleOutput();
        ConsoleStepReporter reporter = new ConsoleStepReporter(output,
                TokenizedMessageToAnsiConverter.DEFAULT,
                () -> Integer.MAX_VALUE);
        reporter.printStepFailure(step, true);
        return output.getOut();
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
                TokenizedMessage repeatAction = tokenizedMessage().action("repeat #" + (finalAttemptIdx + 1));
                WebTauStep repeatedStep = WebTauStep.createStep(tokenizedMessage(repeatAction),
                        () -> tokenizedMessage().classifier("completed").add(repeatAction),
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
        return inProgressMessageModifier != null ?
                inProgressMessageModifier.apply(inProgressMessage) :
                inProgressMessage;
    }

    public TokenizedMessage getCompletionMessage() {
        return completionMessageModifier != null ?
                completionMessageModifier.apply(completionMessage):
                completionMessage;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("message", getCompletionMessage().toListOfMaps());
        result.put("startTime", startTime);
        result.put("elapsedTime", elapsedTime);
        result.put("isSuccessful", isSuccessful);

        if (!exceptionTokenizedMessage.isEmpty()) {
            result.put("exceptionTokenizedMessage", exceptionTokenizedMessage.toListOfMaps());
        }

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

        if (!outputs.isEmpty()) {
            Map<String, Object> outputMap = new LinkedHashMap<>();
            WebTauStepOutput first = outputs.get(0);
            outputMap.put("type", first.getClass().getSimpleName());
            outputMap.put("data", first.toMap());
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
        isInProgress = false;
        isSuccessful = false;

        stackTrace = renderStackTrace(t);

        completionMessage = new TokenizedMessage();
        completionMessage.add("error", "failed").add(replaceValuesFirstLinesOnlyWithFull(inProgressMessage));

        if (t instanceof AssertionTokenizedError) {
            exceptionTokenizedMessage = ((AssertionTokenizedError) t).getTokenizedMessage();
        } else {
            exceptionTokenizedMessage = tokenizedMessage().error(exceptionMessage(t));
        }
    }

    private String exceptionMessage(Throwable t) {
        String message = t.getMessage();
        return "<" + t.getClass().getSimpleName() + ">" + (message != null ? " " + message : "");
    }

    private TokenizedMessage replaceValuesFirstLinesOnlyWithFull(TokenizedMessage message) {
        TokenizedMessage copy = new TokenizedMessage();
        for (MessageToken messageToken : message) {
            copy.add(messageToken.type().equals(TokenizedMessage.TokenTypes.PRETTY_PRINT_VALUE_FIRST_LINES.getType()) ?
                    new MessageToken(TokenizedMessage.TokenTypes.PRETTY_PRINT_VALUE.getType(), messageToken.value()):
                    messageToken);
        }

        return copy;
    }
}
