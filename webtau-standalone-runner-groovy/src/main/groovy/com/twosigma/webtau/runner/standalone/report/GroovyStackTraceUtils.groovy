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

package com.twosigma.webtau.runner.standalone.report

import com.twosigma.webtau.utils.TraceUtils

class GroovyStackTraceUtils {
    private static List<String> libPrefixes = [
            "sun.reflect",
            "sun.net.",
            "sun.security.",
            "java.net.",
            "com.sun.",
            "java.base/",
            "com.twosigma", // TODO limit to webtau
            "org.codehaus.groovy",
            "org.junit",
            "com.intellij",
            "java.lang",
            "groovy.lang"
    ]

    private GroovyStackTraceUtils() {
    }

    static String renderStackTraceWithoutLibCalls(Throwable t) {
        return filterStackTrace(t) { !isStandardCall(it) && !isMoreMessage(it) }
    }

    static String fullCauseMessage(Throwable t) {
        return filterStackTrace(t) { !isAtLine(it) && !isMoreMessage(it) }
    }

    static List<StackTraceCodeEntry> extractLocalCodeEntries(Throwable t) {
        def result = []

        while (t) {
            result.addAll(convertThrowableToCodeEntries(t))
            t = t.getCause()
        }

        return new LinkedHashSet<>(result.reverse()).toList()
    }

    private static List<StackTraceCodeEntry> convertThrowableToCodeEntries(Throwable t) {
        def localEntries = t.stackTrace.findAll {
            !isStandardCall(it) && it.fileName != null && it.lineNumber > 0}

        def codeEntries = localEntries.collect { stackTraceEntry ->
            def lastDotIdx = stackTraceEntry.fileName.lastIndexOf('.')
            def ext = lastDotIdx ==-1 ? '' : stackTraceEntry.fileName.substring(lastDotIdx + 1)

            def className = stackTraceEntry.getClassName()
            def dollarIdx = className.indexOf('$')
            def fileFriendlyClassName = dollarIdx == -1 ? className : className.substring(0, dollarIdx)

            return new StackTraceCodeEntry(fileFriendlyClassName.replace('.', '/') + '.' + ext,
                    [stackTraceEntry.lineNumber])
        }

        return mergeByFileName(codeEntries)
    }

    private static List<StackTraceCodeEntry> mergeByFileName(List<StackTraceCodeEntry> codeEntries) {
        List<StackTraceCodeEntry> result = []
        codeEntries.each {
            def last = result.isEmpty() ? null : result.last()
            if (last?.filePath == it.filePath) {
                last.lineNumbers.addAll(it.lineNumbers)
            } else {
                result.add(it)
            }
        }

        return result
    }

    private static String filterStackTrace(Throwable t, Closure filter) {
        def stackTrace = TraceUtils.stackTrace(t)
        def lines = stackTrace.split("\n")

        return lines[0] + "\n" +
                lines[1..lines.length - 1].findAll(filter).join("\n")
    }

    private static boolean isStandardCall(String stackTraceLine) {
        return libPrefixes.any { isAtLine(stackTraceLine) && stackTraceLine.contains(it) }
    }

    private static boolean isStandardCall(StackTraceElement stackTraceElement) {
        return libPrefixes.any { stackTraceElement.className.startsWith(it) }
    }

    private static boolean isAtLine(String stackTraceLine) {
        return stackTraceLine.trim().startsWith('at')
    }

    private static boolean isMoreMessage(String stackTraceLine) {
        return stackTraceLine.trim().startsWith('...')
    }
}
