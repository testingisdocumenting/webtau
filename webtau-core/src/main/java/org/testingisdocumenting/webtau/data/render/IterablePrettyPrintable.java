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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class IterablePrettyPrintable implements PrettyPrintable {
    private final List<?> list;

    public IterablePrettyPrintable(Iterable<?> iterable) {
        list = StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        prettyPrint(printer, "");
    }

    @Override
    public void prettyPrint(PrettyPrinter printer, String rootPath) {
        if (list.isEmpty()) {
            printEmptyList(printer);
        } else {
            printNonEmptyList(printer, rootPath);
        }
        printer.printLine();
    }

    private void printNonEmptyList(PrettyPrinter printer, String path) {
        printer.printDelimiter("[");
        printer.printLine();
        printer.increaseIndentation();

        int idx = 0;
        for (Object element : list) {
            boolean isLast = idx == list.size() - 1;

            printer.printObject(element);

            if (!isLast) {
                printer.printDelimiter(",");
                printer.printLine();
            }

            idx++;
        }

        printer.printLine();
        printer.decreaseIndentation();
        printer.printDelimiter("]");
    }

    private static void printEmptyList(PrettyPrinter printer) {
        printer.printDelimiter("[");
        printer.printDelimiter("]");
    }
}
