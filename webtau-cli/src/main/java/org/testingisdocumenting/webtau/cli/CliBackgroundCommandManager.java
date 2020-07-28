/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.cli;

import org.testingisdocumenting.webtau.reporter.TestListener;
import org.testingisdocumenting.webtau.reporter.TestResultPayload;
import org.testingisdocumenting.webtau.reporter.WebTauTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CliBackgroundCommandManager implements TestListener {
    // holds all the commands for all the tests
    private static final Map<Integer, CliBackgroundCommand> runningCommands = new ConcurrentHashMap<>();

    // only commands that were running or started during a test
    // need to track for reporting
    // list won't be cleared as soon as the commands exits
    // and will remain for a test run
    private static final ThreadLocal<Map<Integer, CliBackgroundCommand>> localRunningCommands =
            ThreadLocal.withInitial(LinkedHashMap::new);

    static {
        registerShutdown();
    }

    static void register(CliBackgroundCommand backgroundCommand) {
        validateProcessActive(backgroundCommand);
        int pid = backgroundCommand.getBackgroundProcess().getPid();
        runningCommands.put(pid, backgroundCommand);
        localRunningCommands.get().put(pid, backgroundCommand);
    }

    static void remove(CliBackgroundCommand backgroundCommand) {
        validateProcessActive(backgroundCommand);
        runningCommands.remove(backgroundCommand.getBackgroundProcess().getPid());
    }

    @Override
    public void afterAllTests() {
        destroyActiveProcesses();
        runningCommands.clear();
    }

    @Override
    public void beforeTestRun(WebTauTest test) {
        localRunningCommands.get().clear();
        localRunningCommands.get().putAll(runningCommands);

        runningCommands.values().forEach(CliBackgroundCommand::clearThreadLocal);
    }

    @Override
    public void afterTestRun(WebTauTest test) {
        Map<Integer, CliBackgroundCommand> combinedCommands = new LinkedHashMap<>(runningCommands);
        combinedCommands.putAll(localRunningCommands.get());

        List<Map<String, ?>> backgroundCommands = combinedCommands.values()
                .stream()
                .map(CliBackgroundCommand::toMap)
                .collect(Collectors.toList());

        test.addTestResultPayload(new TestResultPayload("cliBackground", backgroundCommands));
    }

    private static void destroyActiveProcesses() {
        runningCommands.forEach((pid, process) -> process.stop());
    }

    private static void validateProcessActive(CliBackgroundCommand backgroundCommand) {
        if (backgroundCommand.getBackgroundProcess() == null) {
            throw new IllegalStateException("process should not be null");
        }
    }

    // afterAllTests may not be called if this is used outside of test runner
    private static void registerShutdown() {
        Runtime.getRuntime().addShutdownHook(
                new Thread(CliBackgroundCommandManager::destroyActiveProcesses));
    }
}
