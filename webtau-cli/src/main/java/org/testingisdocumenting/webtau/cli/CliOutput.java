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

import org.testingisdocumenting.webtau.cli.expectation.CliResultExpectations;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.RegexpUtils;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.WebTauCore.tokenizedMessage;

public class CliOutput implements CliResultExpectations {
    private final String id;
    private final StreamGobbler streamGobbler;

    private final Set<Integer> matchedLinesIdx;
    private final String command;

    private int lastClearNextLineIdxMarker;

    public CliOutput(String command, String id, StreamGobbler streamGobbler) {
        this.command = command;
        this.id = id;
        this.streamGobbler = streamGobbler;

        this.matchedLinesIdx = new TreeSet<>();
    }

    @Override
    public ValuePath actualPath() {
        return new ValuePath(id);
    }

    public String extractByRegexp(String regexp) {
        return extractByRegexp(Pattern.compile(regexp));
    }

    public String extractByRegexp(Pattern regexp) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("extracting text").classifier("by regexp").string(regexp)
                        .from().url(command).classifier(id),
                (r) -> tokenizedMessage().action("extracted text").classifier("by regexp").string(regexp)
                        .from().url(command).classifier(id).colon().string(r),
                    () -> RegexpUtils.extractByRegexp(get(), regexp));

            return step.execute(StepReportOptions.SKIP_START);
    }

    public String get() {
        return streamGobbler.joinLinesStartingAt(lastClearNextLineIdxMarker);
    }

    public List<String> copyLines() {
        return copyLinesStartingAtIdx(lastClearNextLineIdxMarker);
    }

    public List<String> copyLinesStartingAtIdx(int idx) {
        return streamGobbler.copyLinesStartingAt(idx);
    }

    public IOException getException() {
        return streamGobbler.getException();
    }

    public void registerMatchedLine(Integer idx) {
        matchedLinesIdx.add(idx);
    }

    public void clear() {
        matchedLinesIdx.clear();
        lastClearNextLineIdxMarker = streamGobbler.getLines().size();
    }

    public int getNumberOfLines() {
        return streamGobbler.getNumberOfLines();
    }

    public List<String> extractMatchedLines() {
        List<String> lines = streamGobbler.getLines();
        return matchedLinesIdx.stream().map(lines::get).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return streamGobbler.joinLines();
    }
}
