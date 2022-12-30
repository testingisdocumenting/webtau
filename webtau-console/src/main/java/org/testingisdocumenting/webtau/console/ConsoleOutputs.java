/*
 * Copyright 2021 webtau maintainers
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
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ConsoleOutputs {
    public static final ConsoleOutput defaultOutput = new AnsiConsoleOutput();

    private static final ConsoleOutput combined = new CombinedConsoleOutput();

    private static final List<ConsoleOutput> outputs = ServiceLoaderUtils.load(ConsoleOutput.class);
    private static final ThreadLocal<List<ConsoleOutput>> localOutputs = ThreadLocal.withInitial(ArrayList::new);

    public static void out(Object... styleOrValues) {
        getOutputsStream().forEach(o -> o.out(styleOrValues));
    }

    public static void err(Object... styleOrValues) {
        getOutputsStream().forEach(o -> o.err(styleOrValues));
    }

    public static ConsoleOutput asCombinedConsoleOutput() {
        return combined;
    }

    /**
     * output multiple lines, where line is defined by converting function
     * @param lines list of lines to print
     * @param styleOrValueExtractor function to convert a line to a style and values
     * @param <E> type of line
     */
    public static <E> void outLines(List<E> lines, Function<E, Object[]> styleOrValueExtractor) {
        getOutputsStream().forEach(o -> o.outLines(lines, styleOrValueExtractor));
    }

    /**
     * output multiple lines by limiting the number of lines printed,
     * where line is defined by converting function
     * @param lines list of lines to print
     * @param limit max number of lines to print
     * @param styleOrValueExtractor function to convert a line to a style and values
     * @param <E> type of line
     */
    public static <E> void outLinesWithLimit(List<E> lines,
                                             int limit,
                                             Function<E, Object[]> styleOrValueExtractor) {
        getOutputsStream().forEach(o -> o.outLinesWithLimit(lines, limit, styleOrValueExtractor));
    }

    public static void add(ConsoleOutput consoleOutput) {
        outputs.add(consoleOutput);
    }

    public static void remove(ConsoleOutput consoleOutput) {
        outputs.remove(consoleOutput);
    }

    public static <R> R withAdditionalOutput(ConsoleOutput consoleOutput, Supplier<R> code) {
        try {
            addLocal(consoleOutput);
            return code.get();
        } finally {
            removeLocal(consoleOutput);
        }
    }

    private static void addLocal(ConsoleOutput output) {
        localOutputs.get().add(output);
    }

    private static void removeLocal(ConsoleOutput output) {
        localOutputs.get().remove(output);
    }

    private static Stream<ConsoleOutput> getOutputsStream() {
        if (outputs.isEmpty()) {
            return Stream.of(defaultOutput);
        }

        return Stream.concat(outputs.stream(), localOutputs.get().stream());
    }

    private static class CombinedConsoleOutput implements ConsoleOutput {
        @Override
        public void out(Object... styleOrValues) {
            ConsoleOutputs.out((styleOrValues));
        }

        @Override
        public void err(Object... styleOrValues) {
            ConsoleOutputs.err((styleOrValues));
        }
    }
}
