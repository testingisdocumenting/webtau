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

package org.testingisdocumenting.webtau.cli;

import org.testingisdocumenting.webtau.cli.expectation.CliValidationExitCodeOutputHandler;
import org.testingisdocumenting.webtau.cli.expectation.CliValidationOutputOnlyHandler;
import org.testingisdocumenting.webtau.utils.CollectionUtils;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;

public class Cli {
    public static final Cli cli = new Cli();

    private final ThreadLocal<CliDocumentationArtifact> lastDocumentationArtifact = new ThreadLocal<>();

    public final CliDocumentation doc = new CliDocumentation();

    private Cli() {
    }

    public CliProcessConfig env(Map<String, CharSequence> env) {
        return new CliProcessConfig().env(env);
    }

    public CliProcessConfig env(CharSequence... keyValue) {
        return new CliProcessConfig().env(CollectionUtils.aMapOf((Object[]) keyValue));
    }

    public CliProcessConfig workingDir(String workingDir) {
        return new CliProcessConfig().workingDir(workingDir);
    }

    public CliProcessConfig workingDir(Path workingDir) {
        return new CliProcessConfig().workingDir(workingDir);
    }

    public CliProcessConfig config() {
        return new CliProcessConfig();
    }

    public CliCommand command(String commandBase) {
        return new CliCommand(commandBase);
    }

    public CliCommand command(Supplier<String> commandBaseSupplier) {
        return new CliCommand(commandBaseSupplier);
    }

    public CliRunResult run(String command, CliValidationOutputOnlyHandler handler) {
        return run(command, CliProcessConfig.EMPTY, handler);
    }

    public CliRunResult run(String command) {
        return run(command, CliProcessConfig.EMPTY, CliValidationOutputOnlyHandler.NO_OP);
    }

    public CliRunResult run(String command, CliProcessConfig config, CliValidationOutputOnlyHandler handler) {
        return new CliForegroundCommand().run(command, config, handler);
    }

    public CliRunResult run(String command, CliValidationExitCodeOutputHandler handler) {
        return run(command, CliProcessConfig.EMPTY, handler);
    }

    public CliRunResult run(String command, CliProcessConfig config, CliValidationExitCodeOutputHandler handler) {
        return new CliForegroundCommand().run(command, config, handler);
    }

    public CliBackgroundCommand runInBackground(String command, CliProcessConfig config) {
        CliBackgroundCommand backgroundCommand = new CliBackgroundCommand(command, config);
        backgroundCommand.run();

        return backgroundCommand;
    }

    public CliBackgroundCommand runInBackground(String command) {
        return runInBackground(command, CliProcessConfig.EMPTY);
    }

    void setLastDocumentationArtifact(CliDocumentationArtifact documentationArtifact) {
        lastDocumentationArtifact.set(documentationArtifact);
    }

    CliDocumentationArtifact getLastDocumentationArtifact() {
        return lastDocumentationArtifact.get();
    }
}
