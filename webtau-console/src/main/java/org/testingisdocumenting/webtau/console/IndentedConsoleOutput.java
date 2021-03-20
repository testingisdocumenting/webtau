/*
 * Copyright 2021 webtau maintainers
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

import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.Arrays;

import java.util.stream.Stream;

public class IndentedConsoleOutput implements ConsoleOutput {
    private final ConsoleOutput original;
    private final String indentation;

    public IndentedConsoleOutput(ConsoleOutput original, Integer indentationSize) {
        this.original = original;
        this.indentation = StringUtils.createIndentation(indentationSize);
    }

    @Override
    public void out(Object... styleOrValues) {
        original.out(Stream.concat(Stream.of(indentation), Arrays.stream(styleOrValues)).toArray());
    }

    @Override
    public void err(Object... styleOrValues) {
        original.err(Stream.concat(Stream.of(indentation), Arrays.stream(styleOrValues)).toArray());
    }
}
