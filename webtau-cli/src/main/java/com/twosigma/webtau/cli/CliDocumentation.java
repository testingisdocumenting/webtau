/*
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

package com.twosigma.webtau.cli;

import com.twosigma.webtau.cli.expectation.CliOutput;
import com.twosigma.webtau.documentation.DocumentationArtifactsLocation;
import com.twosigma.webtau.utils.FileUtils;

import java.nio.file.Path;
import java.util.List;

public class CliDocumentation {
    public void capture(String artifactName) {
        Capture capture = new Capture(artifactName);
        capture.capture();
    }

    private static class Capture {
        private final Path path;
        private final CliValidationResult lastValidationResult;

        public Capture(String artifactName) {
            this.path = DocumentationArtifactsLocation.resolve(artifactName);

            this.lastValidationResult = Cli.cli.getLastValidationResult();
            if (this.lastValidationResult == null) {
                throw new IllegalStateException("no cli calls were made yet");
            }
        }

        public void capture() {
            captureCommand();
            captureOut();
            captureErr();
            captureOutMatchedLines();
            captureErrMatchedLines();
        }

        private void captureCommand() {
            FileUtils.writeTextContent(path.resolve("command.txt"), lastValidationResult.getCommand());

        }

        private void captureOut() {
            capture("out.txt", lastValidationResult.getOut());
        }

        private void captureErr() {
            capture("err.txt", lastValidationResult.getErr());
        }

        private void capture(String fileName, CliOutput output) {
            String out = output.get();
            if (!out.isEmpty()) {
                FileUtils.writeTextContent(path.resolve(fileName), out);
            }
        }

        private void captureOutMatchedLines() {
            captureMatched("out.matched.txt", lastValidationResult.getOut());
        }

        private void captureErrMatchedLines() {
            captureMatched("err.matched.txt", lastValidationResult.getErr());
        }

        private void captureMatched(String fileName, CliOutput output) {
            List<String> lines = output.extractMatchedLines();
            if (!lines.isEmpty()) {
                FileUtils.writeTextContent(path.resolve(fileName), String.join("\n", lines));
            }
        }
    }
}
