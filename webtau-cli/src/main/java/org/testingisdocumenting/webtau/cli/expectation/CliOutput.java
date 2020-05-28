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

package org.testingisdocumenting.webtau.cli.expectation;

import org.testingisdocumenting.webtau.cli.StreamGobbler;
import org.testingisdocumenting.webtau.expectation.ActualPath;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CliOutput implements CliResultExpectations {
    private final String id;
    private final StreamGobbler streamGobbler;

    private final Set<Integer> matchedLinesIdx;

    public CliOutput(String id, StreamGobbler streamGobbler) {
        this.id = id;
        this.streamGobbler = streamGobbler;

        this.matchedLinesIdx = new TreeSet<>();
    }

    @Override
    public ActualPath actualPath() {
        return new ActualPath(id);
    }

    public String get() {
        return streamGobbler.getFull();
    }

    public List<String> getLines() {
        return streamGobbler.getLines();
    }

    public IOException getException() {
        return streamGobbler.getException();
    }

    public void registerMatchedLine(Integer idx) {
        matchedLinesIdx.add(idx);
    }

    public List<String> extractMatchedLines() {
        List<String> lines = getLines();
        return matchedLinesIdx.stream().map(lines::get).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return streamGobbler.getFull();
    }
}
