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

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;
import static org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils.renderStackTrace;
import static java.util.stream.Collectors.toList;

public class WebTauStep {
    private final Object context;
    private final String personaId;

    private final TokenizedMessage inProgressMessage;
    private final Function<Object, TokenizedMessage> completionMessageFunc;
    private final Supplier<Object> action;
    private TokenizedMessage completionMessage;

    private boolean isInProgress;
    private boolean isSuccessful;

    private final List<WebTauStep> children;
    private WebTauStep parent;
    private String stackTrace;

    private final List<WebTauStepPayload> payloads;

    private long startTime;
    private long elapsedTime;

    private int totalNumberOfAttempts;

    private static final ThreadLocal<WebTauStep> currentStep = new ThreadLocal<>();

    public static WebTauStep createStep(Object context,
                                        TokenizedMessage inProgressMessage,
                                        Supplier<TokenizedMessage> completionMessageSupplier,
                                        Runnable action) {
        return createStep(context, inProgressMessage, completionMessageSupplier, toSupplier(action));
    }

    public static WebTauStep createStep(Object context,
                                        TokenizedMessage inProgressMessage,
                                        Function<Object, TokenizedMessage> completionMessageFunc,
                                        Supplier<Object> action) {
        return createStep(context, 0, inProgressMessage, completionMessageFunc, action);
    }

    public static WebTauStep createStep(TokenizedMessage inProgressMessage,
                                        Supplier<TokenizedMessage> completionMessageSupplier,
                                        Runnable action) {
        return createStep(null, inProgressMessage, completionMessageSupplier, toSupplier(action));
    }

    public static WebTauStep createStep(Object context,
                                        TokenizedMessage inProgressMessage,
                                        Supplier<TokenizedMessage> completionMessageSupplier,
                                        Supplier<Object> action) {
        return createStep(context, 0, inProgressMessage, completionMessageSupplier, action);
    }

    public static WebTauStep createStep(Object context,
                                        long startTime,
                                        TokenizedMessage inProgressMessage,
                                        Supplier<TokenizedMessage> completionMessageSupplier,
                                        Supplier<Object> action) {
        return createStep(context, startTime, inProgressMessage,
                (stepResult) -> completionMessageSupplier.get(),
                action);
    }

    public static WebTauStep createStep(Object context,
                                        long startTime,
                                        TokenizedMessage inProgressMessage,
                                        Supplier<TokenizedMessage> completionMessageSupplier,
                                        Runnable action) {
        return createStep(context, startTime, inProgressMessage,
                (stepResult) -> completionMessageSupplier.get(),
                toSupplier(action));
    }

    public static WebTauStep createStep(Object context,
                                        long startTime,
                                        TokenizedMessage inProgressMessage,
                                        Function<Object, TokenizedMessage> completionMessageFunc,
                                        Supplier<Object> action) {
        WebTauStep step = new WebTauStep(context, startTime, inProgressMessage, completionMessageFunc, action);
        WebTauStep localCurrentStep = WebTauStep.currentStep.get();

        step.parent = localCurrentStep;
        if (localCurrentStep != null) {
            localCurrentStep.children.add(step);
        }
        currentStep.set(step);

        return step;
    }

    public static void createAndExecuteStep(Object context,
                                            TokenizedMessage inProgressMessage,
                                            Function<Object, TokenizedMessage> completionMessageFunc,
                                            Supplier<Object> action,
                                            StepReportOptions stepReportOptions) {
        WebTauStep step = createStep(context, inProgressMessage, completionMessageFunc, action);
        step.execute(stepReportOptions);
    }

    public static void createAndExecuteStep(Object context,
                                            TokenizedMessage inProgressMessage,
                                            Supplier<TokenizedMessage> completionMessageSupplier,
                                            Runnable action,
                                            StepReportOptions stepReportOptions) {
        WebTauStep step = createStep(context, inProgressMessage, completionMessageSupplier, toSupplier(action));
        step.execute(stepReportOptions);
    }

    public static void createAndExecuteStep(Object context,
                                            TokenizedMessage inProgressMessage,
                                            Supplier<TokenizedMessage> completionMessageSupplier,
                                            Runnable action) {
        createAndExecuteStep(context,
                inProgressMessage, completionMessageSupplier,
                action, StepReportOptions.REPORT_ALL);
    }

    public static void createAndExecuteStep(TokenizedMessage inProgressMessage,
                                            Supplier<TokenizedMessage> completionMessageSupplier,
                                            Runnable action) {
        createAndExecuteStep(null, inProgressMessage, completionMessageSupplier, action);
    }

    public static void createAndExecuteStep(Supplier<TokenizedMessage> completionMessageSupplier,
                                            Runnable action) {
        createAndExecuteStep(null, TokenizedMessage.tokenizedMessage(), completionMessageSupplier,
                action, StepReportOptions.SKIP_START);
    }

    public static WebTauStep getCurrentStep() {
        return currentStep.get();
    }

    private WebTauStep(Object context,
                       long startTime,
                       TokenizedMessage inProgressMessage,
                       Function<Object, TokenizedMessage> completionMessageFunc,
                       Supplier<Object> action) {
        this.context = context;
        this.personaId = Persona.getCurrentPersona().getId();
        this.startTime = startTime;
        this.children = new ArrayList<>();
        this.inProgressMessage = inProgressMessage;
        this.completionMessageFunc = completionMessageFunc;
        this.action = action;
        this.isInProgress = true;
        this.payloads = new ArrayList<>();
        this.totalNumberOfAttempts = 1;
    }

    public Stream<WebTauStep> children() {
        return children.stream();
    }

    public Stream<WebTauStepPayload> getCombinedPayloads() {
        Stream<WebTauStepPayload> result = payloads.stream();
        Stream<WebTauStepPayload> childrenPayload = children.stream().flatMap(WebTauStep::getCombinedPayloads);

        return Stream.concat(result, childrenPayload);
    }

    @SuppressWarnings("unchecked")
    public <V extends WebTauStepPayload> Stream<V> getCombinedPayloadsOfType(Class<V> type) {
        return getCombinedPayloads()
                .filter(p -> p.getClass().isAssignableFrom(type))
                .map(p -> (V) p);
    }

    public void setTotalNumberOfAttempts(int totalNumberOfAttempts) {
        this.totalNumberOfAttempts = totalNumberOfAttempts;
    }

    public void addPayload(WebTauStepPayload payload) {
        payloads.add(payload);
    }

    public boolean hasPayload(Class<? extends WebTauStepPayload> type) {
        return getCombinedPayloadsOfType(type).findAny().isPresent();
    }

    public boolean hasFailedChildrenSteps() {
        return children.stream().anyMatch(WebTauStep::isFailed);
    }

    public int calcNumberOfSuccessfulSteps() {
        return ((isSuccessful ? 1 : 0) +
                children.stream().map(WebTauStep::calcNumberOfSuccessfulSteps).reduce(0, Integer::sum));
    }

    public int calcNumberOfFailedSteps() {
        return ((isFailed() ? 1 : 0) +
                children.stream().map(WebTauStep::calcNumberOfFailedSteps).reduce(0, Integer::sum));
    }

    public Object getFirstAvailableContext() {
        if (context != null) {
            return context;
        }

        return children.stream()
                .map(WebTauStep::getFirstAvailableContext)
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
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
                                             Supplier<Object> actionToUse) {
        try {
            if (stepReportOptions != StepReportOptions.SKIP_START && stepReportOptions != StepReportOptions.SKIP_ALL) {
                StepReporters.onStart(this);
            }

            startClock();
            Object result = actionToUse.get();
            complete(completionMessageFunc.apply(result));
            stopClock();

            if (stepReportOptions != StepReportOptions.SKIP_ALL) {
                StepReporters.onSuccess(this);
            }

            return (R) result;
        } catch (Throwable e) {
            stopClock();

            fail(e);
            StepReporters.onFailure(this);
            throw e;
        } finally {
            WebTauStep localCurrentStep = WebTauStep.currentStep.get();
            if (localCurrentStep != null) {
                currentStep.set(localCurrentStep.parent);
            }
        }
    }

    private <R> R executeMultipleRuns(StepReportOptions stepReportOptions) {
        WebTauStep repeatRoot = getCurrentStep();
        R result = executeSingleRunWithAction(stepReportOptions, multipleRunsActionWrapper(stepReportOptions));

        reduceRepeatedChildren(repeatRoot);

        return result;
    }

    private Supplier<Object> multipleRunsActionWrapper(StepReportOptions stepReportOptions) {
        return () -> {
            int attemptIdx = 0;
            while (attemptIdx < totalNumberOfAttempts) {
                boolean reportStep = shouldReportStepDuringRepeat(attemptIdx);

                int finalAttemptIdx = attemptIdx;
                Runnable actionWithStep = () -> {
                    // TODO add out of total portion
                    MessageToken repeatAction = action("repeat #" + finalAttemptIdx);
                    WebTauStep.createAndExecuteStep(tokenizedMessage(repeatAction),
                            () -> tokenizedMessage(classifier("completed"), repeatAction),
                            this.action::get);
                };

                if (!reportStep) {
                    StepReporters.withoutReporters(() -> { actionWithStep.run(); return null; });
                } else {
                    actionWithStep.run();
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
            it.previous();
            idx--;
            if (!shouldReportStepDuringRepeat(idx)) {
                it.remove();
            }
        }
    }

    private boolean shouldReportStepDuringRepeat(int attemptIdx) {
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

        if (!personaId.isEmpty()) {
            result.put("personaId", personaId);
        }

        if (!children.isEmpty()) {
            result.put("children", children.stream().map(WebTauStep::toMap).collect(toList()));
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

    private static Supplier<Object> toSupplier(Runnable s) {
        return () -> {
            s.run();
            return null;
        };
    }
}
