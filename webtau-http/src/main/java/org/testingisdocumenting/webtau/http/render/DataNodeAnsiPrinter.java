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

package org.testingisdocumenting.webtau.http.render;

import org.testingisdocumenting.webtau.console.ConsoleOutput;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.console.ansi.FontStyle;
import org.testingisdocumenting.webtau.data.traceable.TraceableValue;
import org.testingisdocumenting.webtau.http.datanode.DataNode;
import org.apache.commons.lang3.StringUtils;
import org.testingisdocumenting.webtau.utils.TypeUtils;

import java.util.*;

public class DataNodeAnsiPrinter {
    private static final Color DELIMITER_COLOR = Color.YELLOW;
    private static final Color STRING_COLOR = Color.GREEN;
    private static final Color NUMBER_COLOR = Color.CYAN;
    private static final Color KEY_COLOR = Color.PURPLE;

    private static final Object[] PASS_STYLE = new Object[]{FontStyle.BOLD, Color.GREEN};
    private static final Object[] FAIL_STYLE = new Object[]{FontStyle.BOLD, Color.RED};
    private static final Object[] NO_STYLE = new Object[]{};
    private final ConsoleOutput console;

    private List<Line> lines;
    private Line currentLine;
    private int indentation;

    public DataNodeAnsiPrinter(ConsoleOutput console) {
        this.console = console;
    }

    public void print(DataNode dataNode) {
        print(dataNode, -1);
    }

    public void print(DataNode dataNode, int maxNumberOfLInes) {
        lines = new ArrayList<>();
        currentLine = new Line();
        lines.add(currentLine);

        printNode(dataNode, false);

        console.outLinesWithLimit(lines, maxNumberOfLInes,
                (line) -> line.getStyleAndValues().toArray());
    }

    private void printNode(DataNode dataNode, boolean skipIndent) {
        if (dataNode == null) {
            return;
        }

        if (dataNode.isList()) {
            printList(dataNode, skipIndent);
        } else if (dataNode.isSingleValue()) {
            if (!skipIndent) {
                printIndentation();
            }

            printSingle(dataNode);
        } else {
            printObject(dataNode, skipIndent);
        }
    }

    private void printObject(DataNode dataNode, boolean skipIndent) {
        if (dataNode.numberOfChildren() == 0) {
            printEmptyObject(skipIndent);
        } else {
            printNotEmptyObject(dataNode, skipIndent);
        }
    }

    private void printEmptyObject(boolean skipIndent) {
        if (!skipIndent) {
            printIndentation();
        }

        printDelimiter("{");
        printDelimiter("}");
    }

    private void printNotEmptyObject(DataNode dataNode, boolean skipIndent) {
        openScope("{", skipIndent);

        Collection<DataNode> children = dataNode.children();
        int idx = 0;
        for (DataNode v : children) {
            String k = v.id().getName();

            boolean isLast = idx == children.size() - 1;

            printIndentation();
            printKey(k);
            printNode(v, true);

            if (!isLast) {
                printDelimiter(",");
                println();
            }

            idx++;
        }

        closeScope("}");
    }

    private void printList(DataNode dataNode, boolean skipIndent) {
        if (dataNode.elements().isEmpty()) {
            printEmptyList(skipIndent);
        } else {
            printNonEmptyList(dataNode, skipIndent);
        }
    }

    private void printEmptyList(boolean skipIndent) {
        if (!skipIndent) {
            printIndentation();
        }

        printDelimiter("[");
        printDelimiter("]");
    }

    private void printNonEmptyList(DataNode dataNode, boolean skipIndent) {
        openScope("[", skipIndent);

        int size = dataNode.elements().size();
        int idx = 0;
        for (DataNode n : dataNode.elements()) {
            printNode(n, false);

            boolean isLast = idx == size - 1;
            if (!isLast) {
                printDelimiter(",");
                println();
            }

            idx++;
        }

        closeScope("]");
    }

    private void printSingle(DataNode dataNode) {
        TraceableValue traceableValue = dataNode.getTraceableValue();

        Object value = traceableValue.getValue();
        print(TypeUtils.isString(value) ? STRING_COLOR : NUMBER_COLOR);

        print(valueStyle(traceableValue));
        print(convertToString(traceableValue));
    }

    private String convertToString(TraceableValue traceableValue) {
        switch (traceableValue.getCheckLevel()) {
            case FuzzyFailed:
            case ExplicitFailed:
                return "**" + convertToString(traceableValue.getValue()) + "**";
            case ExplicitPassed:
                return "__" + convertToString(traceableValue.getValue()) + "__";
            case FuzzyPassed:
                return "~~" + convertToString(traceableValue.getValue()) + "~~";
            default:
                return convertToString(traceableValue.getValue());
        }
    }

    private String convertToString(Object value) {
        if (value == null) {
            return "null";
        }

        return TypeUtils.isString(value) ?
                "\"" + value + "\"" :
                value.toString();
    }

    private Object[] valueStyle(TraceableValue traceableValue) {
        switch (traceableValue.getCheckLevel()) {
            case FuzzyFailed:
            case ExplicitFailed:
                return FAIL_STYLE;
            case FuzzyPassed:
            case ExplicitPassed:
                return PASS_STYLE;
            default:
                return NO_STYLE;
        }
    }

    private void printKey(String k) {
        print(KEY_COLOR, "\"" + k + "\"", ": ");
    }

    private void printDelimiter(String d) {
        print(DELIMITER_COLOR, d);
    }

    private void openScope(String scopeChar, boolean skipIndent) {
        if (!skipIndent) {
            printIndentation();
        }

        printDelimiter(scopeChar);
        println();
        indentRight();
    }

    private void closeScope(String scopeChar) {
        println();
        indentLeft();
        printIndentation();
        printDelimiter(scopeChar);
    }

    private void printIndentation() {
        String indentation = indentation();
        if (!indentation.isEmpty()) {
            print(indentation);
        }
    }

    private void indentRight() {
        indentation++;
    }

    private void indentLeft() {
        indentation--;
    }

    private void print(Object... styleAndValues) {
        currentLine.append(styleAndValues);
    }

    private void println(Object... styleAndValues) {
        print(styleAndValues);
        currentLine = new Line();
        lines.add(currentLine);
    }

    private String indentation() {
        return indent(indentation);
    }

    private static String indent(final int nestLevel) {
        if (nestLevel == 0) {
            return "";
        }

        return StringUtils.leftPad(" ", nestLevel * 2);
    }

    private static class Line {
        private final List<Object> styleAndValues = new ArrayList<>();

        public void append(Object... styleAndValues) {
            this.styleAndValues.addAll(Arrays.asList(styleAndValues));
        }

        public List<Object> getStyleAndValues() {
            return styleAndValues;
        }
    }
}
