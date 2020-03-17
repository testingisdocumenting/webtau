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
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.List;
import java.util.Set;
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
}
