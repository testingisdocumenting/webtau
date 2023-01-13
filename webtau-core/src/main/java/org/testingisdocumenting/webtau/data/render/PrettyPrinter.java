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
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    private final Line currentLine;

    private final ConsoleOutput consoleOutput;
    private int indentationSize;
    private String indentation;

    public PrettyPrinter(ConsoleOutput consoleOutput, int indentationSize) {
        this.consoleOutput = consoleOutput;
        this.currentLine = new Line();

        setIndentationSize(indentationSize);

    }

    // TODO remove once DataNode is rewritten using printer
    public ConsoleOutput createIndentedConsoleOutput() {
        return new IndentedConsoleOutput(consoleOutput, indentationSize);
    }

    public void newLine() {
        consoleOutput.out();
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

    public void print(Object... styleOrValues) {
        currentLine.append(styleOrValues);
    }

    public void printDelimiter(String d) {
        print(DELIMITER_COLOR, d);
    }

    public void printLine(Object... styleOrValues) {
        currentLine.append(styleOrValues);
        consoleOutput.out(Stream.concat(Stream.of(indentation), currentLine.styleAndValues.stream()).toArray());
        currentLine.clear();
    }

    public void printObject(Object o) {
        if (o instanceof PrettyPrintable) {
            ((PrettyPrintable) o).prettyPrint(this);
            return;
        }

        if (o instanceof Number) {
            print(NUMBER_COLOR, o);
            return;
        }

        if (o instanceof String) {
            print(STRING_COLOR, quoteString(o));
            return;
        }

        PrettyPrintable prettyPrintable = prettyPrintProviders.stream()
                .map(provider -> provider.prettyPrintableFor(o))
                .filter(Optional::isPresent)
                .findFirst()
                .orElseGet(() -> Optional.of(new FallbackPrettyPrintable(o)))
                .get();

        prettyPrintable.prettyPrint(this);
    }

    private String quoteString(Object text) {
        return "\"" + text + "\"";
    }

    private static class Line {
        private final List<Object> styleAndValues = new ArrayList<>();

        public void append(Object... styleAndValues) {
            this.styleAndValues.addAll(Arrays.asList(styleAndValues));
        }

        public void clear() {
            styleAndValues.clear();
        }
    }
}
