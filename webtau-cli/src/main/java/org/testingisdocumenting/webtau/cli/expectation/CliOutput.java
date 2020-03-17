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

package com.twosigma.webtau.cli.expectation;

import com.twosigma.webtau.expectation.ActualPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CliOutput implements CliResultExpectations {
    private String id;
    private List<String> lines;

    private Set<Integer> matchedLinesIdx;

    private String full;

    public CliOutput(String id, List<String> lines) {
        this.id = id;
        this.lines = lines;
        this.matchedLinesIdx = new TreeSet<>();
        this.full = String.join("\n", lines);
    }

    @Override
    public ActualPath actualPath() {
        return new ActualPath(id);
    }

    public String get() {
        return full;
    }

    public List<String> getLines() {
        return lines;
    }

    public void registerMatchedLine(Integer idx) {
        matchedLinesIdx.add(idx);
    }

    public List<String> extractMatchedLines() {
        return matchedLinesIdx.stream().map(lines::get).collect(Collectors.toList());
    }
}
