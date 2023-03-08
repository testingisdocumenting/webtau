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

package org.testingisdocumenting.webtau.data;

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.data.render.PrettyPrinterDecorationToken;
import org.testingisdocumenting.webtau.utils.StringUtils;

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
        prettyPrint(printer, ValuePath.UNDEFINED, PrettyPrinterDecorationToken.EMPTY);
    }

    public void prettyPrint(PrettyPrinter printer, ValuePath rootPath) {
        prettyPrint(printer, rootPath, PrettyPrinterDecorationToken.EMPTY);
    }

    @Override
    public boolean handlesDecoration() {
        return true;
    }

    @Override
    public boolean printAsBlock() {
        return getNumberOfLines() > 1;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer, ValuePath path, PrettyPrinterDecorationToken decorationToken) {
        if (getNumberOfLines() > 1) {
            printMultiLines(printer, decorationToken);
        } else {
            printSingleLine(printer, decorationToken);
        }
    }

    private void printSingleLine(PrettyPrinter printer, PrettyPrinterDecorationToken decorationToken) {
        if (decorationToken.isEmpty()) {
            printer.print(PrettyPrinter.STRING_COLOR, quoteString(getText()));
        } else {
            printer.print(decorationToken.getColor(), decorationToken.getWrapWith(), quoteString(getText()), decorationToken.getWrapWith());
        }
    }

    private void printMultiLines(PrettyPrinter printer, PrettyPrinterDecorationToken decorationToken) {
        // add an empty space in front of each line, but failed
        String indentation = firstFailedLineIdx != -1 || !decorationToken.isEmpty() ?
                StringUtils.createIndentation(decorationToken.getWrapWith().length()):
                "";

        String underscore = createLongestLineUnderscore();
        printMultilineBorderLine(printer, decorationToken, indentation, underscore, false);

        int lineIdx = 0;
        for (String line : getLines()) {
            if (lineIdx == firstFailedLineIdx) {
                printer.printLine(decorationToken.getColor(), decorationToken.getWrapWith(), line, decorationToken.getWrapWith());
            } else {
                printer.printLine(Color.GREEN, indentation, line);
            }

            lineIdx++;
        }

        printMultilineBorderLine(printer, decorationToken, indentation, underscore, true);
    }

    private void printMultilineBorderLine(PrettyPrinter printer,
                                          PrettyPrinterDecorationToken decorationToken,
                                          String indentation,
                                          String underscore,
                                          boolean lastLine) {
        if (firstFailedLineIdx == -1 && !decorationToken.isEmpty()) {
            printer.print(decorationToken.getColor(), decorationToken.getWrapWith(), underscore, decorationToken.getWrapWith());
        } else {
            printer.print(Color.RESET, indentation, underscore);
        }

        if (!lastLine) {
            printer.flushCurrentLine();
        }
    }

    private String createLongestLineUnderscore() {
        int maxLength = 0;
        for (String line : getLines()) {
            maxLength = Math.max(maxLength, line.length());
        }

        StringBuilder result = new StringBuilder();
        for (int idx = 0; idx < maxLength; idx++) {
            result.append('_');
        }

        return result.toString();
    }

    private String quoteString(Object text) {
        return "\"" + text + "\"";
    }
}
