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

import java.util.Map;
import java.util.function.Supplier;

public class Cli {
    public static final Cli cli = new Cli();

    private final ThreadLocal<CliDocumentationArtifact> lastDocumentationArtifact = new ThreadLocal<>();

    public final CliDocumentation doc = new CliDocumentation();

    private Cli() {
    }

    public ProcessEnv env(Map<String, String> env) {
        return new ProcessEnv(env);
    }

    public ProcessEnv env(String... keyValue) {
        return new ProcessEnv(CollectionUtils.aMapOf((Object[]) keyValue));
    }

    public CliCommand command(String commandBase) {
        return new CliCommand(commandBase);
    }

    public CliCommand command(Supplier<String> commandBaseSupplier) {
        return new CliCommand(commandBaseSupplier);
    }

    public void run(String command, CliValidationOutputOnlyHandler handler) {
        run(command, ProcessEnv.EMPTY, handler);
    }

    public void run(String command) {
        run(command, ProcessEnv.EMPTY, CliValidationOutputOnlyHandler.NO_OP);
    }

    public void run(String command, ProcessEnv env, CliValidationOutputOnlyHandler handler) {
        new CliForegroundCommand().run(command, env, handler);
    }

    public void run(String command, CliValidationExitCodeOutputHandler handler) {
        run(command, ProcessEnv.EMPTY, handler);
    }

    public void run(String command, ProcessEnv env, CliValidationExitCodeOutputHandler handler) {
        new CliForegroundCommand().run(command, env, handler);
    }

    public CliBackgroundCommand runInBackground(String command, ProcessEnv env) {
        CliBackgroundCommand backgroundCommand = new CliBackgroundCommand(command, env);
        backgroundCommand.start();

        return backgroundCommand;
    }

    public CliBackgroundCommand runInBackground(String command) {
        return runInBackground(command, ProcessEnv.EMPTY);
    }

    void setLastDocumentationArtifact(CliDocumentationArtifact documentationArtifact) {
        lastDocumentationArtifact.set(documentationArtifact);
    }

    CliDocumentationArtifact getLastDocumentationArtifact() {
        return lastDocumentationArtifact.get();
    }
}
