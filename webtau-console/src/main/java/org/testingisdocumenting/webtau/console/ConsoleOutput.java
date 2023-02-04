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

import org.testingisdocumenting.webtau.console.ansi.Color;

import java.util.List;
import java.util.function.Function;

public interface ConsoleOutput {
    void out(Object... styleOrValues);
    void err(Object... styleOrValues);

    /**
     * output multiple lines, where line is defined by converting function
     * @param lines list of lines to print
     * @param styleOrValueExtractor function to convert a line to a style and values
     * @param <E> type of line
     */
    default <E> void outLines(List<E> lines, Function<E, Object[]> styleOrValueExtractor) {
        lines.forEach(l -> out(styleOrValueExtractor.apply(l)));
    }

    /**
     * output multiple lines by limiting the number of lines printed,
     * where line is defined by converting function
     * @param lines list of lines to print
     * @param limit max number of lines to print
     * @param styleOrValueExtractor function to convert a line to a style and values
     * @param <E> type of line
     */
    default <E> void outLinesWithLimit(List<E> lines,
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
}
