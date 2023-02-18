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

import org.testingisdocumenting.webtau.console.ansi.Color;

public class ClassPrettyPrintable implements PrettyPrintable {
    private final Class<?> value;
    private final String fullName;

    public ClassPrettyPrintable(Class<?> value) {
        fullName = value.getCanonicalName();
        this.value = value;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        if (value.isArray()) {
            printArrayClass(printer);
        } else {
            printRegularClass(printer);
        }
    }

    private void printArrayClass(PrettyPrinter printer) {
        printer.print(Color.RESET, "class ");
        printer.print(Color.BLUE, fullName);
    }

    private void printRegularClass(PrettyPrinter printer) {
        int lastDotIdx = fullName.lastIndexOf('.');
        String parentOrPackage = fullName.substring(0, lastDotIdx);

        printer.print(Color.RESET, parentOrPackage);
        if (!parentOrPackage.isEmpty()) {
            printer.print(".");
        }

        printer.print(Color.BLUE, fullName.substring(lastDotIdx + 1));
    }
}
