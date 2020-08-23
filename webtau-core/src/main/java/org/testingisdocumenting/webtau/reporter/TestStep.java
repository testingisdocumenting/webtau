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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils.renderStackTrace;
import static java.util.stream.Collectors.toList;

public class TestStep {
    private final Object context;
    private final String personaId;

    private final TokenizedMessage inProgressMessage;
    private final Function<Object, TokenizedMessage> completionMessageFunc;
    private final Supplier<Object> action;
    private TokenizedMessage completionMessage;

    private boolean isInProgress;
    private boolean isSuccessful;

    private final List<TestStep> children;
    private TestStep parent;
    private String stackTrace;

    private final List<TestStepPayload> payloads;

    private long startTime;
    private long elapsedTime;

    private static final ThreadLocal<TestStep> currentStep = new ThreadLocal<>();

    public static TestStep createStep(Object context,
                                      TokenizedMessage inProgressMessage,
                                      Supplier<TokenizedMessage> completionMessageSupplier,
                                      Runnable action) {
        return createStep(context, inProgressMessage, completionMessageSupplier, toSupplier(action));
    }

    public static TestStep createStep(Object context,
                                      TokenizedMessage inProgressMessage,
                                      Function<Object, TokenizedMessage> completionMessageFunc,
                                      Supplier<Object> action) {
        return createStep(context, 0, inProgressMessage, completionMessageFunc, action);
    }

    public static TestStep createStep(TokenizedMessage inProgressMessage,
                                      Supplier<TokenizedMessage> completionMessageSupplier,
                                      Runnable action) {
        return createStep(null, inProgressMessage, completionMessageSupplier, toSupplier(action));
    }

    public static TestStep createStep(Object context,
                                      TokenizedMessage inProgressMessage,
                                      Supplier<TokenizedMessage> completionMessageSupplier,
                                      Supplier<Object> action) {
        return createStep(context, 0, inProgressMessage, completionMessageSupplier, action);
    }

    public static TestStep createStep(Object context,
                                      long startTime,
                                      TokenizedMessage inProgressMessage,
                                      Supplier<TokenizedMessage> completionMessageSupplier,
                                      Supplier<Object> action) {
        return createStep(context, startTime, inProgressMessage,
                (stepResult) -> completionMessageSupplier.get(),
                action);
    }

    public static TestStep createStep(Object context,
                                      long startTime,
                                      TokenizedMessage inProgressMessage,
                                      Supplier<TokenizedMessage> completionMessageSupplier,
                                      Runnable action) {
        return createStep(context, startTime, inProgressMessage,
                (stepResult) -> completionMessageSupplier.get(),
                toSupplier(action));
    }

    public static TestStep createStep(Object context,
                                      long startTime,
                                      TokenizedMessage inProgressMessage,
                                      Function<Object, TokenizedMessage> completionMessageFunc,
                                      Supplier<Object> action) {
        TestStep step = new TestStep(context, startTime, inProgressMessage, completionMessageFunc, action);
        TestStep localCurrentStep = TestStep.currentStep.get();

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
        TestStep step = createStep(context, inProgressMessage, completionMessageFunc, action);
        step.execute(stepReportOptions);
    }

    public static void createAndExecuteStep(Object context,
                                            TokenizedMessage inProgressMessage,
                                            Supplier<TokenizedMessage> completionMessageSupplier,
                                            Runnable action,
                                            StepReportOptions stepReportOptions) {
        TestStep step = createStep(context, inProgressMessage, completionMessageSupplier, toSupplier(action));
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

    public static TestStep getCurrentStep() {
        return currentStep.get();
    }

    private TestStep(Object context,
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
    }

    private TestStep(Object context,
                     TokenizedMessage inProgressMessage,
                     Function<Object, TokenizedMessage> completionMessageFunc,
                     Supplier<Object> action) {
        this(context, 0, inProgressMessage, completionMessageFunc, action);
    }

    public Stream<TestStep> children() {
        return children.stream();
    }

    public Stream<TestStepPayload> getCombinedPayloads() {
        Stream<TestStepPayload> result = payloads.stream();
        Stream<TestStepPayload> childrenPayload = children.stream().flatMap(TestStep::getCombinedPayloads);

        return Stream.concat(result, childrenPayload);
    }

    @SuppressWarnings("unchecked")
    public <V extends TestStepPayload> Stream<V> getCombinedPayloadsOfType(Class<V> type) {
        return getCombinedPayloads()
                .filter(p -> p.getClass().isAssignableFrom(type))
                .map(p -> (V) p);
    }

    public void addPayload(TestStepPayload payload) {
        payloads.add(payload);
    }

    public boolean hasPayload(Class<? extends TestStepPayload> type) {
        return getCombinedPayloadsOfType(type).findAny().isPresent();
    }

    public boolean hasFailedChildrenSteps() {
        return children.stream().anyMatch(TestStep::isFailed);
    }

    public int calcNumberOfSuccessfulSteps() {
        return ((isSuccessful ? 1 : 0) +
                children.stream().map(TestStep::calcNumberOfSuccessfulSteps).reduce(0, Integer::sum));
    }

    public int calcNumberOfFailedSteps() {
        return ((isFailed() ? 1 : 0) +
                children.stream().map(TestStep::calcNumberOfFailedSteps).reduce(0, Integer::sum));
    }

    public Object getFirstAvailableContext() {
        if (context != null) {
            return context;
        }

        return children.stream()
                .map(TestStep::getFirstAvailableContext)
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public int getNumberOfParents() {
        int result = 0;
        TestStep step = this;
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

    public Object execute(StepReportOptions stepReportOptions) {
        try {
            if (stepReportOptions != StepReportOptions.SKIP_START) {
                StepReporters.onStart(this);
            }

            startClock();
            Object result = action.get();
            complete(completionMessageFunc.apply(result));
            stopClock();

            StepReporters.onSuccess(this);

            return result;
        } catch (Throwable e) {
            stopClock();

            fail(e);
            StepReporters.onFailure(this);
            throw e;
        } finally {
            TestStep localCurrentStep = TestStep.currentStep.get();
            if (localCurrentStep != null) {
                currentStep.set(localCurrentStep.parent);
            }
        }
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

        if (!children.isEmpty()) {
            result.put("children", children.stream().map(TestStep::toMap).collect(toList()));
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
