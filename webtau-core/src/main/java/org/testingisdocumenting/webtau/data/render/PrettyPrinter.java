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
import org.testingisdocumenting.webtau.console.ansi.AnsiAsStylesValuesListConsoleOutput;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.*;

/**
 * prints values using pretty ANSI colors, maintains indentation
 * delegates to either {@link PrettyPrintable} or {@link PrettyPrintableProvider}
 */
public class PrettyPrinter implements Iterable<PrettyPrinterLine> {
    private static final int DEFAULT_RECOMMENDED_MAX_WIDTH_FOR_SINGLE_LINE_OBJECTS = 100;

    public static final Color DELIMITER_COLOR = Color.YELLOW;
    public static final Color STRING_COLOR = Color.GREEN;
    public static final Color NUMBER_COLOR = Color.BLUE;
    public static final Color KEY_COLOR = Color.PURPLE;
    public static final Color UNKNOWN_COLOR = Color.CYAN;

    private static final int INDENTATION_STEP = 2;
    private static final List<PrettyPrintableProvider> prettyPrintProviders = ServiceLoaderUtils.load(PrettyPrintableProvider.class);

    private final Set<ValuePath> pathsToDecorate;
    private PrettyPrinterDecorationToken decorationToken;

    private final List<PrettyPrinterLine> lines;
    private PrettyPrinterLine currentLine;

    private int indentationSize;
    private String indentation;

    private ValueConverter valueConverter;
    private int recommendedMaxWidthForSingleLineObjects;

    public PrettyPrinter(int indentationSize) {
        this.lines = new ArrayList<>();
        this.currentLine = new PrettyPrinterLine();
        this.pathsToDecorate = new HashSet<>();

        this.setRecommendedMaxWidthForSingleLineObjects(DEFAULT_RECOMMENDED_MAX_WIDTH_FOR_SINGLE_LINE_OBJECTS);

        setIndentationSize(indentationSize);
    }

    public void setValueConverter(ValueConverter valueConverter) {
        this.valueConverter = valueConverter;
    }

    public ValueConverter getValueConverter() {
        return valueConverter;
    }

    public void setRecommendedMaxWidthForSingleLineObjects(int width) {
        this.recommendedMaxWidthForSingleLineObjects = width;
    }

    public int getRecommendedMaxWidthForSingleLineObjects() {
        return recommendedMaxWidthForSingleLineObjects;
    }

    public static boolean isPrettyPrintable(Object value) {
        return findPrettyPrintable(value).isPresent();
    }

    public static Optional<PrettyPrintable> findPrettyPrintable(Object value) {
        if (value instanceof PrettyPrintable) {
            return Optional.of((PrettyPrintable) value);
        }

        return prettyPrintProviders.stream()
                .map(provider -> provider.prettyPrintableFor(value))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    public List<List<Map<String, Object>>> generateStyledTextListOfListsOfMaps() {
        AnsiAsStylesValuesListConsoleOutput ansiStylesConsoleOutput = new AnsiAsStylesValuesListConsoleOutput();
        renderToConsole(ansiStylesConsoleOutput);

        return ansiStylesConsoleOutput.toListOfListsOfMaps();
    }

    public void setPathsDecoration(PrettyPrinterDecorationToken decorationToken, Set<ValuePath> paths) {
        this.pathsToDecorate.clear();
        this.pathsToDecorate.addAll(paths);
        this.decorationToken = decorationToken;
    }

    public void setPathsDecoration(PrettyPrinterDecorationToken decorationToken, List<ValuePath> paths) {
        setPathsDecoration(decorationToken, new HashSet<>(paths));
    }

    public Set<ValuePath> getPathsToDecorate() {
        return pathsToDecorate;
    }

    public PrettyPrinterDecorationToken getDecorationToken() {
        return decorationToken;
    }

    public void clear() {
        lines.clear();
        currentLine.clear();
        pathsToDecorate.clear();
        indentationSize = 0;
    }

    public void renderToConsole(ConsoleOutput consoleOutput) {
        flushCurrentLine();
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

    @Override
    public Iterator<PrettyPrinterLine> iterator() {
        return lines.listIterator();
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
        appendToCurrentLine(line.getStyleAndValues());
    }

    public void print(Object... styleOrValues) {
        appendToCurrentLine(styleOrValues);
    }

    public void printDelimiter(String d) {
        print(DELIMITER_COLOR, d);
    }

    public void printLine(Object... styleOrValues) {
        appendToCurrentLine(styleOrValues);
        flushCurrentLine();
    }

    public void flushCurrentLine() {
        if (currentLine.isEmpty()) {
            return;
        }

        lines.add(currentLine);
        currentLine = new PrettyPrinterLine();
    }

    public void printObject(Object o) {
        printObject(ValuePath.UNDEFINED, o);
    }

    public void printObject(ValuePath valuePath, Object o) {
        System.out.println("@@ printObject");
        Object effectiveObject = valueConverter != null ?
                valueConverter.convertValue(valuePath, o) :
                o;

        boolean needToDecorate = pathsToDecorate.contains(valuePath);

        if (effectiveObject instanceof Number) {
            printPrimitive(NUMBER_COLOR, effectiveObject, needToDecorate);
        } else {
            PrettyPrintable prettyPrintable = findPrettyPrintable(effectiveObject)
                    .orElseGet(() -> new FallbackPrettyPrintable(effectiveObject));

            boolean handlesDecoration = prettyPrintable.handlesDecoration();
            if (!handlesDecoration && needToDecorate) {
                print(decorationToken.getColor(), decorationToken.getWrapWith());
            }

            if (handlesDecoration && needToDecorate) {
                prettyPrintable.prettyPrint(this, valuePath, decorationToken);
            } else {
                prettyPrintable.prettyPrint(this, valuePath);
            }

            if (!handlesDecoration && needToDecorate) {
                print(decorationToken.getColor(), decorationToken.getWrapWith());
            }
        }
    }

    public void printObjectIndented(Object value, int width) {
        printObjectIndented(ValuePath.UNDEFINED, value, width);
    }

    public void printObjectIndented(ValuePath path, Object value, int width) {
        int previousIndentation = indentationSize;
        setIndentationSize(width);
        printObject(path, value);
        setIndentationSize(previousIndentation);
    }

    public void printObjectAutoIndentedByCurrentLine(Object value) {
        printObjectAutoIndentedByCurrentLine(ValuePath.UNDEFINED, value);
    }

    public void printObjectAutoIndentedByCurrentLine(ValuePath path, Object value) {
        printObjectIndented(path, value, currentLine.getWidth());
    }

    private void appendToCurrentLine(Object... styleOrValue) {
        if (currentLine.isEmpty()) {
            currentLine.append(indentation);
        }

        currentLine.append(styleOrValue);
    }

    private void printPrimitive(Color color, Object o, boolean needToDecorate) {
        if (needToDecorate) {
            print(decorationToken.getColor(), decorationToken.getWrapWith(), o, decorationToken.getWrapWith());
        } else {
            print(color, o);
        }
    }
}
