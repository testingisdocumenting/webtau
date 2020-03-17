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

package org.testingisdocumenting.webtau.reporter.stacktrace;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StackTraceUtils {
    private static List<String> libPrefixes = Arrays.asList(
            "sun.",
            "java.",
            "com.sun.",
            "org.testingisdocumenting.webtau",
            "org.codehaus.groovy",
            "org.junit",
            "com.intellij",
            "groovy.",
            "groovysh_evaluate");

    private StackTraceUtils() {
    }

    public static String renderStackTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);

        return stringWriter.toString();
    }

    public static String renderStackTraceWithoutLibCalls(Throwable t) {
        return filterStackTrace(t,  (line) -> !isStandardCall(line) &&
                !isMoreMessage(line) &&
                !isUnknownSource(line));
    }

    public static String fullCauseMessage(Throwable t) {
        return filterStackTrace(t, (line) -> !isAtLine(line) && !isMoreMessage(line));
    }

    public static List<StackTraceCodeEntry> extractLocalCodeEntries(Throwable t) {
        List<StackTraceCodeEntry> result = new ArrayList<>();

        while (t != null) {
            result.addAll(convertThrowableToCodeEntries(t));
            t = t.getCause();
        }

        Collections.reverse(result);
        List<StackTraceCodeEntry> merged = mergeByFileName(result.stream());
        return new ArrayList<>(new LinkedHashSet<>(merged));
    }

    private static List<StackTraceCodeEntry> convertThrowableToCodeEntries(Throwable t) {
        Stream<StackTraceElement> localCalls = Arrays.stream(t.getStackTrace()).filter(stackTraceElement ->
                !isStandardCall(stackTraceElement) &&
                stackTraceElement.getFileName() != null &&
                stackTraceElement.getLineNumber() > 0);


        Stream<StackTraceCodeEntry> localCodeEntries = localCalls.map(stackTraceElement -> {
            int lastDotIdx = stackTraceElement.getFileName().lastIndexOf('.');
            String ext = lastDotIdx == -1 ? "" : stackTraceElement.getFileName().substring(lastDotIdx + 1);

            String className = stackTraceElement.getClassName();
            int dollarIdx = className.indexOf('$');

            String fileFriendlyClassName = dollarIdx == -1 ? className : className.substring(0, dollarIdx);

            return new StackTraceCodeEntry(fileFriendlyClassName.replace('.', '/') + '.' + ext,
                    Collections.singleton(stackTraceElement.getLineNumber()));
        });

        return localCodeEntries.collect(Collectors.toList());
    }

    private static List<StackTraceCodeEntry> mergeByFileName(Stream<StackTraceCodeEntry> codeEntries) {
        List<StackTraceCodeEntry> result = new ArrayList<>();

        codeEntries.forEach(codeEntry -> {
            StackTraceCodeEntry last = result.isEmpty() ? null : result.get(result.size() - 1);

            if (last != null && last.getFilePath().equals(codeEntry.getFilePath())){
                last.addLineNumbers(codeEntry.getLineNumbers());
            } else{
                result.add(codeEntry);
            }
        });

        return result;
    }

    private static String filterStackTrace(Throwable t, Predicate<String> filter) {
        String stackTrace = renderStackTrace(t);
        List<String> lines = Arrays.asList(stackTrace.split("\n"));

        return lines.stream()
                .filter(filter)
                .collect(Collectors.joining("\n")).trim();
    }

    private static boolean isStandardCall(String stackTraceLine) {
        return libPrefixes.stream().anyMatch(prefix -> isAtLine(stackTraceLine) && stackTraceLine.contains(prefix));
    }

    private static boolean isStandardCall(StackTraceElement stackTraceElement) {
        return libPrefixes.stream().anyMatch(prefix -> stackTraceElement.getClassName().startsWith(prefix));
    }

    private static boolean isAtLine(String stackTraceLine) {
        return stackTraceLine.trim().startsWith("at");
    }

    private static boolean isUnknownSource(String stackTraceLine) {
        return stackTraceLine.contains("(Unknown Source)");
    }

    private static boolean isMoreMessage(String stackTraceLine) {
        return stackTraceLine.trim().startsWith("...");
    }
}
