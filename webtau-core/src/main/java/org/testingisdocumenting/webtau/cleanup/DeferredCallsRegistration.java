/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.cleanup;

import org.testingisdocumenting.webtau.TestListener;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.reporter.WebTauTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.id;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

/**
 * centralized place to register actions at the end of all test runs
 * and at the end of individual test runs
 */
public class DeferredCallsRegistration implements TestListener {
    private static final List<DeferredGlobalCodeEntry> globalRegistered = Collections.synchronizedList(new ArrayList<>());
    private static final ThreadLocal<List<DeferredLocalCodeEntry>> localRegistered = ThreadLocal.withInitial(ArrayList::new);
    private static String testRunnerId = "";

    private static final AtomicBoolean isCalledAfterAllTests = new AtomicBoolean(false);

    @Override
    public void afterAllTests() {
        callAfterAllTestsRegisteredCode();
    }

    public static void callAfterAllTests(String action,
                                         String actionCompleted,
                                         String id,
                                         Supplier<Boolean> isValid,
                                         Runnable code) {
        globalRegistered.add(new DeferredGlobalCodeEntry(action, actionCompleted, id, isValid, code));

        // lazy shutdown hook init to avoid nested shutdowns in case of
        // JUnit4 runner that calls afterAllTests in shutdown hook
        ShutdownHook.INSTANCE.noOp();
    }

    /**
     * need an explicit registration that a run is part of a test runner.
     * Things like local cleanup require a test listener to signal when to cleanup.
     * Without runner registration we need to throw an exception to let users know
     * that certain things won't work
     * @param id test runner id
     */
    public static void registerTestRunnerId(String id) {
        testRunnerId = id;
    }

    public static void callAfterATest(String label, Runnable code) {
        if (testRunnerId.isEmpty()) {
            throw new IllegalStateException("can't register code to be executed after a test run as the code runs outside of a test runner " +
                    "or no WebTau runner/extension is specified");
        }

        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(action("register test deferred block"), id(label)),
                () -> tokenizedMessage(action("registered test deferred block"), id(label)),
                () -> localRegistered.get().add(new DeferredLocalCodeEntry(label, code)));
        step.execute(StepReportOptions.SKIP_START);
    }

    private synchronized static void callAfterAllTestsRegisteredCode() {
        if (isCalledAfterAllTests.compareAndSet(true, true)) {
            return;
        }

        globalRegistered.stream()
                .filter(DeferredGlobalCodeEntry::isValid)
                .forEach(DeferredCallsRegistration::callAfterAllTestsRegisteredCode);
        globalRegistered.removeIf(DeferredGlobalCodeEntry::isValid);
    }

    public static void executeLocalCleanup(WebTauTest test) {
        List<DeferredLocalCodeEntry> codeList = localRegistered.get();
        if (codeList.isEmpty()) {
            return;
        }

        try {
            for (DeferredLocalCodeEntry codeEntry : codeList) {
                WebTauStep.createAndExecuteStep(
                        tokenizedMessage(action("executing deferred block"), id(codeEntry.label)),
                        () -> tokenizedMessage(action("executed deferred block"), id(codeEntry.label)),
                        () -> codeEntry.code.run());
            }
        }
        catch (Throwable e) {
            test.setException(e);
        }
        finally {
            codeList.clear();
        }
    }

    private static void callAfterAllTestsRegisteredCode(DeferredGlobalCodeEntry entry) {
        WebTauStep.createAndExecuteStep(
                tokenizedMessage(action(entry.action), id(entry.id)),
                () -> tokenizedMessage(action(entry.actionCompleted), id(entry.id)),
                entry.code);
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(DeferredCallsRegistration::callAfterAllTestsRegisteredCode));
    }

    private static class DeferredLocalCodeEntry {
        String label;
        Runnable code;

        public DeferredLocalCodeEntry(String label, Runnable code) {
            this.label = label;
            this.code = code;
        }
    }

    private static class DeferredGlobalCodeEntry {
        String action;
        String actionCompleted;
        String id;
        Supplier<Boolean> isValid;
        Runnable code;

        public DeferredGlobalCodeEntry(String action,
                                       String actionCompleted,
                                       String id,
                                       Supplier<Boolean> isValid,
                                       Runnable code) {
            this.action = action;
            this.actionCompleted = actionCompleted;
            this.id = id;
            this.isValid = isValid;
            this.code = code;
        }

        boolean isValid() {
            return this.isValid.get();
        }
    }

    private static class ShutdownHook {
        final static ShutdownHook INSTANCE = new ShutdownHook();

        private ShutdownHook() {
            registerShutdownHook();
        }

        public void noOp() {
        }
    }
}
