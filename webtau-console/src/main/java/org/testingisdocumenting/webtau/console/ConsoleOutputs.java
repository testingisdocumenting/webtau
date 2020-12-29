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

package org.testingisdocumenting.webtau.console;

import org.testingisdocumenting.webtau.console.ansi.AnsiConsoleOutput;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class ConsoleOutputs {
    private static final ConsoleOutput defaultOutput = new AnsiConsoleOutput();

    private static final List<ConsoleOutput> outputs = ServiceLoaderUtils.load(ConsoleOutput.class);

    public static void out(Object... styleOrValues) {
        getOutputsStream().forEach(o -> o.out(styleOrValues));
    }

    public static void err(Object... styleOrValues) {
        getOutputsStream().forEach(o -> o.err(styleOrValues));
    }

    /**
     * prints lines limiting output to a given value. Prints values from the start and from the end, omitting middle.
     *
     * @param lines lines to print and limit
     * @param limit max number of lines to print, -1 to print all
     * @param styleOrValueExtractor function to extract valueOrStyle from given list
     */
    public static <E> void outLinesWithLimit(List<E> lines,
                                             int limit,
                                             Function<E, Object[]> styleOrValueExtractor) {
        if (limit == -1 || lines.size() <= limit) {
            outLines(lines, styleOrValueExtractor);
            return;
        }

        int firstHalfNumberOfLines = limit / 2;
        int secondHalfNumberOfLines = firstHalfNumberOfLines + limit % 2;

        outLines(lines.subList(0, firstHalfNumberOfLines), styleOrValueExtractor);
        ConsoleOutputs.out(Color.YELLOW, "...");
        outLines(lines.subList(lines.size() - secondHalfNumberOfLines, lines.size()), styleOrValueExtractor);
    }

    public static void add(ConsoleOutput consoleOutput) {
        outputs.add(consoleOutput);
    }

    public static void remove(ConsoleOutput consoleOutput) {
        outputs.remove(consoleOutput);
    }

    private static Stream<ConsoleOutput> getOutputsStream() {
        if (outputs.isEmpty()) {
            return Stream.of(defaultOutput);
        }

        return outputs.stream();
    }

    private static <E> void outLines(List<E> lines, Function<E, Object[]> styleOrValueExtractor) {
        lines.forEach(l -> ConsoleOutputs.out(styleOrValueExtractor.apply(l)));
    }
}
