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
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.id;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

/**
 * centralized place to register cleanup actions at the end of all test runs
 * as a catch-all cleans not cleaned things on exit
 */
public class CleanupRegistration implements TestListener {
    private static final List<CleanupEntry> registered = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void afterAllTests() {
        cleanup();
    }

    public static void registerForCleanup(String action,
                                          String actionCompleted,
                                          String id,
                                          Supplier<Boolean> isValid,
                                          Runnable cleanupCode) {
        registered.add(new CleanupEntry(action, actionCompleted, id, isValid, cleanupCode));

        // lazy shutdown hook init to avoid nested shutdowns in case of
        // JUnit4 runner that calls afterAllTests in shutdown hook
        ShutdownHook.INSTANCE.noOp();
    }

    private static void cleanup() {
        registered.stream()
                .filter(CleanupEntry::isValid)
                .forEach(CleanupRegistration::cleanup);
        registered.removeIf(CleanupEntry::isValid);
    }

    private static void cleanup(CleanupEntry entry) {
        WebTauStep.createAndExecuteStep(
                tokenizedMessage(action(entry.action), id(entry.id)),
                () -> tokenizedMessage(action(entry.actionCompleted), id(entry.id)),
                entry.cleanupCode);
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(CleanupRegistration::cleanup));
    }

    private static class CleanupEntry {
        String action;
        String actionCompleted;
        String id;
        Supplier<Boolean> isValid;
        Runnable cleanupCode;

        public CleanupEntry(String action,
                            String actionCompleted,
                            String id,
                            Supplier<Boolean> isValid,
                            Runnable cleanupCode) {
            this.action = action;
            this.actionCompleted = actionCompleted;
            this.id = id;
            this.isValid = isValid;
            this.cleanupCode = cleanupCode;
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
