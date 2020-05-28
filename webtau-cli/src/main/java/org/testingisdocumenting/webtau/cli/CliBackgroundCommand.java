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

import org.testingisdocumenting.webtau.cli.expectation.CliOutput;

import java.io.IOException;

public class CliBackgroundCommand {
    private final String command;
    private final ProcessEnv env;
    private ProcessBackgroundRunResult backgroundRunResult;

    public CliBackgroundCommand(String command, ProcessEnv env) {
        this.command = command;
        this.env = env;
    }

    public void start() {
        if (backgroundRunResult != null) {
            return;
        }

        try {
            backgroundRunResult = ProcessUtils.runInBackground(command, env.getEnv());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CliOutput getOutput() {
        return backgroundRunResult.getOutput();
    }

    public CliOutput getError() {
        return backgroundRunResult.getError();
    }

    public void stop() {
        backgroundRunResult.destroy();
        backgroundRunResult = null;
    }

    public void restart() {
        stop();
        start();
    }
}
