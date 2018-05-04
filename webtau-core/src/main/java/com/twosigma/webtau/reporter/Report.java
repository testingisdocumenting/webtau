/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.reporter;

import com.twosigma.webtau.console.ansi.AutoResetAnsiString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Report {
    private final List<Report> children;
    private final List<List<ReportToken>> lines;
    private List<ReportToken> currentLine;

    private final String scopeId;
    private int nestLevel;

    public Report(String scopeId, int nestLevel) {
        this.scopeId = scopeId;
        this.children = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.currentLine = new ArrayList<>();
        this.nestLevel = nestLevel;
    }

    public Report newScope(String scopeId) {
        Report report = new Report(scopeId, nestLevel + 1);
        children.add(report);

        return report;
    }

    public Report identifier(String identifier) {
        return addToken("identifier", identifier);
    }

    public Report text(String text) {
        return addToken("text", text);
    }

    public Report number(Number number) {
        return addToken("number", number.toString());
    }

    public Report string(String string) {
        return addToken("number", string);
    }

    public Report newLine() {
        lines.add(currentLine);
        currentLine = new ArrayList<>();
        return addToken("newLine", "");
    }

    private Report addToken(String type, String textRepresentation) {
        currentLine.add(new ReportToken(type, textRepresentation));
        return this;
    }

    public List<String> toAnsiStrings() {
        return lines.stream().map(this::toAnsiString).collect(toList());
    }

    private String toAnsiString(List<ReportToken> tokens) {
        return new AutoResetAnsiString(tokens.stream().flatMap(this::tokenToAnsi)).toString();
    }

    private Stream<?> tokenToAnsi(ReportToken reportToken) {
        switch (reportToken.getType()) {
            default:
                return Stream.of(reportToken.getTextRepresentation());
        }
    }
}
