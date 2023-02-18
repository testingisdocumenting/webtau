/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;

public class MultilineString implements PrettyPrintable {
    private final Object original;
    private final String text;
    private final String[] lines;
    private final boolean hasSlashR;

    private int firstFailedLineIdx;

    public MultilineString(Object value) {
        this.original = value;
        String originalAsText = value.toString();
        this.text = originalAsText.replaceAll("\r", "");
        this.hasSlashR = originalAsText.contains("\r");
        this.lines = text.split("\n", -1);
        this.firstFailedLineIdx = -1;
    }

    public String getLine(int idx) {
        return lines[idx];
    }

    public String getText() {
        return text;
    }

    public Object getOriginal() {
        return original;
    }

    public String[] getLines() {
        return lines;
    }

    public int getNumberOfLines() {
        return lines.length;
    }

    public boolean isSingleLine() {
        return lines.length == 1;
    }

    public boolean hasSlashR() {
        return hasSlashR;
    }

    public void setFirstFailedLineIdx(int firstFailedLineIdx) {
        this.firstFailedLineIdx = firstFailedLineIdx;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        prettyPrint(printer, ValuePath.UNDEFINED);
    }

    @Override
    public void prettyPrint(PrettyPrinter printer, ValuePath path) {
        if (lines.length > 1) {
            printMultiLines(printer);
        } else {
            printSingleLine(printer, path);
        }
    }

    private void printSingleLine(PrettyPrinter printer, ValuePath path) {
        printer.printStringPrimitive(path, text);
    }

    private void printMultiLines(PrettyPrinter printer) {
        // add an empty space in front of each line, but failed
        String eachLinePrefix = firstFailedLineIdx != -1 ? " " : "";

        String underscore = createLongestLineUnderscore();
        printer.printLine(Color.RESET, eachLinePrefix, underscore);

        int lineIdx = 0;
        for (String line : lines) {
            if (lineIdx == firstFailedLineIdx) {
                printer.printLine(Color.RED, '*', line, '*');
            } else {
                printer.printLine(Color.GREEN, eachLinePrefix, line);
            }

            lineIdx++;
        }

        printer.printLine(Color.RESET, eachLinePrefix, underscore);
    }

    private String createLongestLineUnderscore() {
        int maxLength = 0;
        for (String line : lines) {
            maxLength = Math.max(maxLength, line.length());
        }

        StringBuilder result = new StringBuilder();
        for (int idx = 0; idx < maxLength; idx++) {
            result.append('_');
        }

        return result.toString();
    }
}
