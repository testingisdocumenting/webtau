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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CliBackgroundCommandManager implements TestListener {
    private static final Map<Integer, CliBackgroundCommand> runningCommands = new ConcurrentHashMap<>();
    static {
        registerShutdown();
    }

    static void register(CliBackgroundCommand backgroundCommand) {
        validateProcess(backgroundCommand);
        runningCommands.put(backgroundCommand.getBackgroundProcess().getPid(), backgroundCommand);
    }

    static void remove(CliBackgroundCommand backgroundCommand) {
        validateProcess(backgroundCommand);
        runningCommands.remove(backgroundCommand.getBackgroundProcess().getPid());
    }

    @Override
    public void afterAllTests() {
        destroyActiveProcesses();
        runningCommands.clear();
    }

    private static void destroyActiveProcesses() {
        runningCommands.forEach((pid, process) -> process.stop());
    }

    private static void validateProcess(CliBackgroundCommand backgroundCommand) {
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
