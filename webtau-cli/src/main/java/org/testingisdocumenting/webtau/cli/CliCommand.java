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

import org.testingisdocumenting.webtau.cli.expectation.CliValidationExitCodeOutputHandler;
import org.testingisdocumenting.webtau.cli.expectation.CliValidationOutputOnlyHandler;

import java.util.function.Supplier;

public class CliCommand {
    private Supplier<String> commandBaseSupplier;
    private String commandBase;

    CliCommand(String commandBase) {
        this.commandBase = commandBase;
    }

    CliCommand(Supplier<String> commandBaseSupplier) {
        this.commandBaseSupplier = commandBaseSupplier;
    }

    public void run() {
        run("", ProcessEnv.EMPTY, CliValidationOutputOnlyHandler.NO_OP);
    }

    public void run(CliValidationOutputOnlyHandler handler) {
        run("", ProcessEnv.EMPTY, handler);
    }

    public void run(CliValidationExitCodeOutputHandler handler) {
        run("", ProcessEnv.EMPTY, handler);
    }

    public void run(String args) {
        run(args, ProcessEnv.EMPTY, CliValidationOutputOnlyHandler.NO_OP);
    }

    public void run(String args, CliValidationOutputOnlyHandler handler) {
        run(args, ProcessEnv.EMPTY, handler);
    }

    public void run(String args, CliValidationExitCodeOutputHandler handler) {
        run(args, ProcessEnv.EMPTY, handler);
    }

    public void run(ProcessEnv env) {
        run("", env, CliValidationOutputOnlyHandler.NO_OP);
    }

    public void run(ProcessEnv env, CliValidationOutputOnlyHandler handler) {
        run("", env, handler);
    }

    public void run(ProcessEnv env, CliValidationExitCodeOutputHandler handler) {
        run("", env, handler);
    }

    public void run(String args, ProcessEnv env) {
        run(args, env, CliValidationOutputOnlyHandler.NO_OP);
    }

    public void run(String args, ProcessEnv env, CliValidationOutputOnlyHandler handler) {
        new CliForegroundCommand().run(fullCommand(args), env, handler);
    }

    public void run(String args, ProcessEnv env, CliValidationExitCodeOutputHandler handler) {
        new CliForegroundCommand().run(fullCommand(args), env, handler);
    }

    public CliBackgroundCommand runInBackground() {
        return runInBackground("", ProcessEnv.EMPTY);
    }

    public CliBackgroundCommand runInBackground(String args) {
        return runInBackground(args, ProcessEnv.EMPTY);
    }

    public CliBackgroundCommand runInBackground(String args, ProcessEnv env) {
        CliBackgroundCommand backgroundCommand = new CliBackgroundCommand(fullCommand(args), env);
        backgroundCommand.start();

        return backgroundCommand;
    }

    private synchronized String fullCommand(String args) {
        if (commandBase == null) {
            commandBase = commandBaseSupplier.get();
        }

        return args.isEmpty() ?
                commandBase:
                commandBase + " " + args;
    }
}
