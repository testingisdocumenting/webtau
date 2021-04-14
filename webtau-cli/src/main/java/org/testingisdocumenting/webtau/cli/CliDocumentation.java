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

import org.testingisdocumenting.webtau.documentation.DocumentationArtifacts;
import org.testingisdocumenting.webtau.documentation.DocumentationArtifactsLocation;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.FileUtils;

import java.nio.file.Path;
import java.util.List;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;

public class CliDocumentation {
    public void capture(String artifactName) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(classifier("documentation"), action("capturing last"), classifier("cli"),
                        action("call"), AS, urlValue(artifactName)),
                (path) -> tokenizedMessage(classifier("documentation"), action("captured last"), classifier("cli"),
                        action("call"), AS, urlValue(((Path) path).toAbsolutePath())),
                () -> {
                    Capture capture = new Capture(artifactName);
                    capture.capture();

                    return capture.path;
                });

        step.execute(StepReportOptions.REPORT_ALL);
    }

    private static class Capture {
        private final Path path;
        private final CliDocumentationArtifact documentationArtifact;

        public Capture(String artifactName) {
            DocumentationArtifacts.registerName(artifactName);

            this.path = DocumentationArtifactsLocation.resolve(artifactName);

            this.documentationArtifact = Cli.cli.getLastDocumentationArtifact();
            if (this.documentationArtifact == null) {
                throw new IllegalStateException("no cli calls were made yet");
            }
        }

        public void capture() {
            captureCommand();
            captureOut();
            captureErr();
            captureOutMatchedLines();
            captureErrMatchedLines();
            captureExitCode();
        }

        private void captureCommand() {
            FileUtils.writeTextContent(path.resolve("command.txt"), documentationArtifact.getFullCommand());

        }

        private void captureOut() {
            capture("out.txt", documentationArtifact.getOutput());
        }

        private void captureErr() {
            capture("err.txt", documentationArtifact.getError());
        }

        private void capture(String fileName, CliOutput output) {
            String out = output.get();
            if (!out.isEmpty()) {
                FileUtils.writeTextContent(path.resolve(fileName), out);
            }
        }

        private void captureOutMatchedLines() {
            captureMatched("out.matched.txt", documentationArtifact.getOutput());
        }

        private void captureErrMatchedLines() {
            captureMatched("err.matched.txt", documentationArtifact.getError());
        }

        private void captureMatched(String fileName, CliOutput output) {
            List<String> lines = output.extractMatchedLines();
            if (!lines.isEmpty()) {
                FileUtils.writeTextContent(path.resolve(fileName), String.join("\n", lines));
            }
        }

        private void captureExitCode() {
            if (documentationArtifact.getExitCode() == null) {
                return;
            }

            FileUtils.writeTextContent(path.resolve("exitcode.txt"),
                    String.valueOf(documentationArtifact.getExitCode().get()));
        }
    }
}
