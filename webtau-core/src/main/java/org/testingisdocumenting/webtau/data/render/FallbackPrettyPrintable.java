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

import org.testingisdocumenting.webtau.data.ValuePath;

import java.util.Objects;

class FallbackPrettyPrintable implements PrettyPrintable {
    private final Object object;

    public FallbackPrettyPrintable(Object object) {
        this.object = object;
    }

    @Override
    public boolean handlesDecoration() {
        return true;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer, ValuePath rootPath, PrettyPrinterDecorationToken decorationToken) {
        if (decorationToken.isEmpty()) {
            printer.print(PrettyPrinter.UNKNOWN_COLOR, Objects.toString(object));
        } else {
            printer.print(decorationToken.getColor(), decorationToken.getWrapWith(), Objects.toString(object), decorationToken.getWrapWith());
        }
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        prettyPrint(printer, ValuePath.UNDEFINED, PrettyPrinterDecorationToken.EMPTY);
    }
}
