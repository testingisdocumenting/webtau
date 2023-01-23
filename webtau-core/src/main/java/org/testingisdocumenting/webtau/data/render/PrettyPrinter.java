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

package org.testingisdocumenting.webtau.data.render;

import org.testingisdocumenting.webtau.console.ConsoleOutput;
import org.testingisdocumenting.webtau.console.IndentedConsoleOutput;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.*;

/**
 * prints values using pretty ANSI colors, maintains indentation
 * delegates to either {@link PrettyPrintable} or {@link PrettyPrintableProvider}
 */
public class PrettyPrinter {
    public static final Color DELIMITER_COLOR = Color.YELLOW;
    public static final Color STRING_COLOR = Color.GREEN;
    public static final Color NUMBER_COLOR = Color.CYAN;
    public static final Color KEY_COLOR = Color.PURPLE;

    private static final int INDENTATION_STEP = 2;
    private static final List<PrettyPrintableProvider> prettyPrintProviders = ServiceLoaderUtils.load(PrettyPrintableProvider.class);

    private final Set<ValuePath> pathsToDecorate;
    private PrettyPrinterDecorationToken decorationToken;

    private final List<PrettyPrinterLine> lines;
    private PrettyPrinterLine currentLine;

    private final ConsoleOutput consoleOutput;
    private int indentationSize;
    private String indentation;

    public PrettyPrinter(ConsoleOutput consoleOutput, int indentationSize) {
        this.consoleOutput = consoleOutput;
        this.lines = new ArrayList<>();
        this.currentLine = new PrettyPrinterLine();
        this.pathsToDecorate = new HashSet<>();

        setIndentationSize(indentationSize);
    }

    public static boolean isPrettyPrintable(Object value) {
        if (value instanceof PrettyPrintable) {
            return true;
        }

        return prettyPrintProviders.stream()
                .map(provider -> provider.prettyPrintableFor(value))
                .anyMatch(Optional::isPresent);
    }

    public void setPathsDecoration(PrettyPrinterDecorationToken decorationToken, Set<ValuePath> paths) {
        this.pathsToDecorate.clear();
        this.pathsToDecorate.addAll(paths);
        this.decorationToken = decorationToken;
    }

    // TODO remove once DataNode is rewritten using printer
    public ConsoleOutput createIndentedConsoleOutput() {
        return new IndentedConsoleOutput(consoleOutput, indentationSize);
    }

    public void newLine() {
        consoleOutput.out();
    }

    public ConsoleOutput getConsoleOutput() {
        return consoleOutput;
    }

    public Set<ValuePath> getPathsToDecorate() {
        return pathsToDecorate;
    }

    public PrettyPrinterDecorationToken getDecorationToken() {
        return decorationToken;
    }

    public void renderToConsole() {
        for (PrettyPrinterLine line : lines) {
            consoleOutput.out(line.getStyleAndValues().toArray());
        }
    }

    public int calcMaxWidth() {
        return lines.stream()
                .map(PrettyPrinterLine::getWidth)
                .max(Integer::compareTo)
                .orElse(0);
    }

    public int getNumberOfLines() {
        return lines.size();
    }

    public PrettyPrinterLine getLine(int lineIdx) {
        return lines.get(lineIdx);
    }

    public void setIndentationSize(int indentationSize) {
        if (indentationSize < 0) {
            throw new IllegalStateException("can't set indentation to a negative value: " + indentationSize);
        }

        this.indentationSize = indentationSize;
        this.indentation = StringUtils.createIndentation(this.indentationSize);
    }

    public void increaseIndentation() {
        setIndentationSize(this.indentationSize + INDENTATION_STEP);
    }

    public void decreaseIndentation() {
        setIndentationSize(this.indentationSize - INDENTATION_STEP);
    }

    public void print(PrettyPrinterLine line) {
        currentLine.append(line.getStyleAndValues());
    }

    public void print(Object... styleOrValues) {
        currentLine.append(styleOrValues);
    }

    public void printDelimiter(String d) {
        print(DELIMITER_COLOR, d);
    }

    public void printLine(Object... styleOrValues) {
        currentLine.append(styleOrValues);
        flushCurrentLine();
    }

    public void flushCurrentLine() {
        currentLine.prepend(indentation);
        lines.add(currentLine);

        currentLine = new PrettyPrinterLine();
    }

    public void printObject(Object o) {
        printObject(ValuePath.UNDEFINED, o);
    }

    public void printObject(ValuePath valuePath, Object o) {
        if (o instanceof PrettyPrintable) {
            ((PrettyPrintable) o).prettyPrint(this);
            return;
        }

        boolean needToDecorate = pathsToDecorate.contains(valuePath);

        if (o instanceof Number) {
            printPrimitive(NUMBER_COLOR, o, needToDecorate);
        } else if (o instanceof String) {
            printPrimitive(STRING_COLOR, quoteString(o), needToDecorate);
        } else {
            PrettyPrintable prettyPrintable = prettyPrintProviders.stream()
                    .map(provider -> provider.prettyPrintableFor(o))
                    .filter(Optional::isPresent)
                    .findFirst()
                    .orElseGet(() -> Optional.of(new FallbackPrettyPrintable(o)))
                    .get();

            if (needToDecorate) {
                print(decorationToken.getColor(), decorationToken.getWrapWith());
            }

            prettyPrintable.prettyPrint(this, valuePath);

            if (needToDecorate) {
                print(decorationToken.getColor(), decorationToken.getWrapWith());
            }
        }
    }

    private void printPrimitive(Color color, Object o, boolean needToDecorate) {
        if (needToDecorate) {
            print(decorationToken.getColor(), decorationToken.getWrapWith(), o, decorationToken.getWrapWith());
        } else {
            print(color, o);
        }
    }

    private String quoteString(Object text) {
        return "\"" + text + "\"";
    }
}
