/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class StringUtils {
    private StringUtils() {
    }

    public static int maxLineLength(String text) {
        return Arrays.stream(text.replace("\r", "").split("\n")).
                map(String::length).
                max(Integer::compareTo).
                orElse(0);
    }

    public static String[] splitLinesPreserveNewLineSeparator(String text) {
        return text.split("((?=\\n)|(?<=\\n))");
    }

    public static String stripIndentation(String text) {
        List<String> lines = trimEmptyLines(Arrays.asList(text.replace("\r", "").split("\n")));
        Integer indentation = lines.stream().
                filter(StringUtils::notEmptyLine).
                map(StringUtils::lineIndentation).min(Integer::compareTo).orElse(0);

        return lines.stream().map(l -> removeIndentation(l, indentation)).collect(Collectors.joining("\n"));
    }

    public static boolean hasNewLineSeparator(String text) {
        return text.indexOf('\n') != -1;
    }

    public static String firstNLines(String text, int n) {
        return Arrays.stream(text.split("\n"))
                .limit(n)
                .collect(joining("\n"));
    }

    public static int numberOfLines(String text) {
        if (text.isEmpty()) {
            return 1;
        }

        int numberOfEol = 0;
        int idx = 0;
        while ((idx = text.indexOf('\n', idx)) != -1) {
            numberOfEol++;
            idx++;
        }

        return numberOfEol + 1;
    }

    public static String extractInsideCurlyBraces(String code) {
        int startIdx = code.indexOf('{');
        if (startIdx == -1) {
            return "";
        }

        int endIdx = code.lastIndexOf('}');

        return code.substring(startIdx + 1, endIdx);
    }

    public static String removeContentInsideBracketsInclusive(String code) {
        int openIdx = code.indexOf('<');
        return openIdx == -1 ? code : code.substring(0, openIdx);
    }

    public static String createIndentation(int numberOfSpaces) {
        return numberOfSpaces == 0 ? "" : String.format("%" + numberOfSpaces + "s", "");
    }

    public static String concatWithIndentation(String prefix, String multilineText) {
        String indentation = StringUtils.createIndentation(prefix.length());

        String[] lines = multilineText.split("\n");
        return (prefix + lines[0]) +
                (lines.length > 1 ?
                        "\n" + joinWithIndentAllButFirstLine(indentation, lines) : "");
    }

    public static String indentAllLinesButFirst(String indentation, String multilineText) {
        String[] lines = multilineText.split("\n");
        return (lines[0]) +
                (lines.length > 1 ?
                        "\n" + joinWithIndentAllButFirstLine(indentation, lines) : "");
    }

    public static boolean nullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean notNullOrEmpty(String s) {
        return !nullOrEmpty(s);
    }

    public static boolean isNumeric(NumberFormat numberFormat, String text) {
        text = text.trim();

        if (text.isEmpty()) {
            return false;
        }

        ParsePosition pos = new ParsePosition(0);
        numberFormat.parse(text, pos);

        return text.length() == pos.getIndex();
    }

    public static Number convertToNumber(String text) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            return numberFormat.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Number convertToNumber(NumberFormat numberFormat, String text) {
        try {
            return numberFormat.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String ensureStartsWith(String str, String start) {
        if (str.startsWith(start)) {
            return str;
        }

        return start + str;
    }

    public static String stripTrailing(String str, char trailingChar) {
        if (str.endsWith("" + trailingChar)) {
            return str.substring(0, str.length() - 1);
        }

        return str;
    }

    public static String toStringOrNull(Object o) {
        return o == null ? null : o.toString();
    }

    private static String joinWithIndentAllButFirstLine(String indentation, String[] lines) {
        return Arrays.stream(lines).skip(1).map(l -> indentation + l).collect(joining("\n"));
    }

    private static Integer lineIndentation(String line) {
        int i = 0;
        while (i < line.length() && line.charAt(i) == ' ') {
            i++;
        }

        return i;
    }

    private static boolean notEmptyLine(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static String removeIndentation(String line, Integer indentation) {
        if (line.trim().isEmpty()) {
            return line;
        }

        return line.substring(indentation);
    }

    private static List<String> trimEmptyLines(List<String> lines) {
        int b = firstNonEmptyLineIdx(lines);
        int e = firstFromEndNonEmptyLineIdx(lines);

        return lines.subList(Math.max(b, 0), e > 0 ? e + 1 : lines.size());
    }

    private static int firstNonEmptyLineIdx(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            if (notEmptyLine(lines.get(i))) {
                return i;
            }
        }

        return -1;
    }

    private static int firstFromEndNonEmptyLineIdx(List<String> lines) {
        for (int i = lines.size() - 1; i >= 0; i--) {
            if (notEmptyLine(lines.get(i))) {
                return i;
            }
        }

        return -1;
    }

    private static NumberFormat getNumberFormat() {
        return NumberFormat.getInstance();
    }
}
