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
import org.testingisdocumenting.webtau.data.ResourceNameAware;

import java.util.function.Supplier;

public class CliCommand implements ResourceNameAware {
    private Supplier<String> commandBaseSupplier;
    private String commandBase;

    CliCommand(String commandBase) {
        this.commandBase = commandBase;
    }

    CliCommand(Supplier<String> commandBaseSupplier) {
        this.commandBaseSupplier = commandBaseSupplier;
    }

    public CliRunResult run() {
        return run("", CliProcessConfig.EMPTY, CliValidationOutputOnlyHandler.NO_OP);
    }

    public CliRunResult run(CliValidationOutputOnlyHandler handler) {
        return run("", CliProcessConfig.EMPTY, handler);
    }

    public CliRunResult run(CliValidationExitCodeOutputHandler handler) {
        return run("", CliProcessConfig.EMPTY, handler);
    }

    public CliRunResult run(String args) {
        return run(args, CliProcessConfig.EMPTY, CliValidationOutputOnlyHandler.NO_OP);
    }

    public CliRunResult run(String args, CliValidationOutputOnlyHandler handler) {
        return run(args, CliProcessConfig.EMPTY, handler);
    }

    public CliRunResult run(String args, CliValidationExitCodeOutputHandler handler) {
        return run(args, CliProcessConfig.EMPTY, handler);
    }

    public CliRunResult run(CliProcessConfig config) {
        return run("", config, CliValidationOutputOnlyHandler.NO_OP);
    }

    public CliRunResult run(CliProcessConfig config, CliValidationOutputOnlyHandler handler) {
        return run("", config, handler);
    }

    public CliRunResult run(CliProcessConfig config, CliValidationExitCodeOutputHandler handler) {
        return run("", config, handler);
    }

    public CliRunResult run(String args, CliProcessConfig config) {
        return run(args, config, CliValidationOutputOnlyHandler.NO_OP);
    }

    public CliRunResult run(String args, CliProcessConfig config, CliValidationOutputOnlyHandler handler) {
        return new CliForegroundCommand().run(fullCommand(args), config, handler);
    }

    public CliRunResult run(String args, CliProcessConfig config, CliValidationExitCodeOutputHandler handler) {
        return new CliForegroundCommand().run(fullCommand(args), config, handler);
    }

    public CliBackgroundCommand runInBackground() {
        return runInBackground("", CliProcessConfig.EMPTY);
    }

    public CliBackgroundCommand runInBackground(String args) {
        return runInBackground(args, CliProcessConfig.EMPTY);
    }

    public CliBackgroundCommand runInBackground(String args, CliProcessConfig config) {
        CliBackgroundCommand backgroundCommand = new CliBackgroundCommand(fullCommand(args), config);
        backgroundCommand.run();

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

    @Override
    public String resourceName() {
        return commandBase;
    }
}
