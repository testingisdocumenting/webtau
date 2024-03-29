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

package org.testingisdocumenting.webtau.data.render;

import org.testingisdocumenting.webtau.data.ValuePath;

/**
 * Print value to the console using ANSI colors
 * Used in e.g. REPL, {@link org.testingisdocumenting.webtau.reporter.ConsoleStepReporter}
 */
public interface PrettyPrintable {
    enum RenderMethod {
        REGULAR,
        FORCE_MULTILINE,
    }

    void prettyPrint(PrettyPrinter printer);

    default boolean handlesDecoration() {
        return false;
    }

    default boolean printAsBlock() {
        return false;
    }

    default void prettyPrint(PrettyPrinter printer, ValuePath rootPath) {
        prettyPrint(printer);
    }

    default void prettyPrint(PrettyPrinter printer, ValuePath rootPath, PrettyPrinterDecorationToken decorationToken) {
        prettyPrint(printer, rootPath);
    }

    default void prettyPrint(PrettyPrinter printer, ValuePath rootPath, RenderMethod renderMethod, PrettyPrinterDecorationToken decorationToken) {
        prettyPrint(printer);
    }
}
