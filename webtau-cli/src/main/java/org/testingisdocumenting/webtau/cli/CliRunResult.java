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

import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.RegexpUtils;

import java.util.regex.Pattern;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.stringValue;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class CliRunResult {
    private final String command;
    private final int exitCode;
    private final String output;
    private final String error;

    public CliRunResult(String command, int exitCode, String output, String error) {
        this.command = command;
        this.exitCode = exitCode;
        this.output = output;
        this.error = error;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getOutput() {
        return output;
    }

    public String getError() {
        return error;
    }

    public String extractFromOutputByRegexp(String regexp) {
        return extractFromOutputByRegexp(Pattern.compile(regexp));
    }

    public String extractFromOutputByRegexp(Pattern regexp) {
        return extractFromSourceByRegexp("stdout", output, regexp);
    }

    public String extractFromErrorByRegexp(String regexp) {
        return extractFromErrorByRegexp(Pattern.compile(regexp));
    }

    public String extractFromErrorByRegexp(Pattern regexp) {
        return extractFromSourceByRegexp("stderr", error, regexp);
    }

    private String extractFromSourceByRegexp(String sourceLabel, String source, Pattern regexp) {
        WebTauStep step = WebTauStep.createStep(null,
                tokenizedMessage(action("extracting text"), classifier("by regexp"), stringValue(regexp),
                        FROM, urlValue(command), classifier(sourceLabel)),
                (r) -> tokenizedMessage(action("extracted text"), classifier("by regexp"), stringValue(regexp),
                        FROM, urlValue(command), classifier(sourceLabel), COLON, stringValue(r)),
                () -> extractByRegexpStepImpl(sourceLabel, source, regexp));

        return step.execute(StepReportOptions.SKIP_START);
    }

    private String extractByRegexpStepImpl(String sourceLabel, String source, Pattern regexp) {
        String extracted = RegexpUtils.extractByRegexp(source, regexp);
        if (extracted == null) {
            throw new RuntimeException("can't find content to extract using regexp <" + regexp + "> from " +
                    sourceLabel + " of " + command);
        }

        return extracted;
    }
}
