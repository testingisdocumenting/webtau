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
import org.testingisdocumenting.webtau.data.MultilineString;

public class StringPrettyPrintable implements PrettyPrintable {
    private final MultilineString text;

    public StringPrettyPrintable(MultilineString text) {
        this.text = text;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        text.prettyPrint(printer);
    }

    @Override
    public void prettyPrint(PrettyPrinter printer, ValuePath rootPath) {
        text.prettyPrint(printer, rootPath);
    }

    @Override
    public boolean handlesDecoration() {
        return text.handlesDecoration();
    }

    @Override
    public void prettyPrint(PrettyPrinter printer, ValuePath rootPath, PrettyPrinterDecorationToken decorationToken) {
        text.prettyPrint(printer, rootPath, decorationToken);
    }

    @Override
    public boolean printAsBlock() {
        return text.printAsBlock();
    }
}
