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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.twosigma.webtau.reporter.stacktrace.StackTraceUtils.renderStackTrace;
import static java.util.stream.Collectors.toList;

public class TestStep<C, R> {
    private C context;

    private TokenizedMessage inProgressMessage;
    private Supplier<TokenizedMessage> completionMessageSupplier;
    private Supplier<R> action;
    private TokenizedMessage completionMessage;

    private boolean isInProgress;
    private boolean isSuccessful;

    private List<TestStep<?, ?>> children;
    private TestStep<?, ?> parent;
    private String stackTrace;

    private List<TestStepPayload> payloads;

    private static ThreadLocal<TestStep<?, ?>> currentStep = new ThreadLocal<>();

    public static <C> TestStep<C, Void> createStep(C context,
                                                   TokenizedMessage inProgressMessage,
                                                   Supplier<TokenizedMessage> completionMessageSupplier,
                                                   Runnable action) {
        return createStep(context, inProgressMessage, completionMessageSupplier, toSupplier(action));
    }

    public static <C> void createAndExecuteStep(C context,
                                                TokenizedMessage inProgressMessage,
                                                Supplier<TokenizedMessage> completionMessageSupplier,
                                                Runnable action) {
        TestStep<C, Void> step = createStep(context, inProgressMessage, completionMessageSupplier, toSupplier(action));
        step.execute(StepReportOptions.REPORT_ALL);
    }

    public static <C, R> TestStep<C, R> createStep(C context,
                                                   TokenizedMessage inProgressMessage,
                                                   Supplier<TokenizedMessage> completionMessageSupplier,
                                                   Supplier<R> action) {
        TestStep<C, R> step = new TestStep<>(context, inProgressMessage, completionMessageSupplier, action);
        TestStep<?, ?> localCurrentStep = TestStep.currentStep.get();

        step.parent = localCurrentStep;
        if (localCurrentStep != null) {
            localCurrentStep.children.add(step);
        }
        currentStep.set(step);

        return step;
    }

    public static TestStep<?, ?> getCurrentStep() {
        return currentStep.get();
    }

    private TestStep(C context,
                     TokenizedMessage inProgressMessage,
                     Supplier<TokenizedMessage> completionMessageSupplier,
                     Supplier<R> action) {
        this.context = context;
        this.children = new ArrayList<>();
        this.inProgressMessage = inProgressMessage;
        this.completionMessageSupplier = completionMessageSupplier;
        this.action = action;
        this.isInProgress = true;
        this.payloads = new ArrayList<>();
    }

    public Stream<TestStep<?, ?>> children() {
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

    @SuppressWarnings("unchecked")
    public C getFirstAvailableContext() {
        if (context != null) {
            return context;
        }

        return (C) children.stream()
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

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public boolean isFailed() {
        return !isSuccessful;
    }

    public R execute(StepReportOptions stepReportOptions) {
        try {
            if (stepReportOptions != StepReportOptions.SKIP_START) {
                StepReporters.onStart(this);
            }

            R r = action.get();

            complete(completionMessageSupplier.get());
            StepReporters.onSuccess(this);

            return r;
        } catch (Throwable e) {
            fail(e);
            StepReporters.onFailure(this);
            throw e;
        } finally {
            TestStep<?, ?> localCurrentStep = TestStep.currentStep.get();
            if (localCurrentStep != null) {
                currentStep.set(localCurrentStep.parent);
            }
        }
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

    private static Supplier<Void> toSupplier(Runnable s) {
        return () -> {
            s.run();
            return null;
        };
    }
}
