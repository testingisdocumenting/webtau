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

package org.testingisdocumenting.webtau.expectation.stepoutput;

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.render.PrettyPrinterDecorationToken;
import org.testingisdocumenting.webtau.data.render.PrettyPrinterLine;
import org.testingisdocumenting.webtau.reporter.WebTauStepOutput;

import java.util.*;

public class ValueMatcherStepOutput implements WebTauStepOutput {
    private final ValuePath root;
    private final Object actual;
    private final Set<ValuePath> valuePathsToHighlight;
    private final ValueConverter valueConverter;

    // we cache pretty print as sometimes actual values pretty print involves things like browser or other interactions
    // we print failed tests and their steps one more time at the end of tests runs
    // by that time browsers and other heavy resources are already closed
    // so instead of reprinting we will use cached printed result
    private final List<PrettyPrinterLine> cachedPrettyPrintOutput;

    public ValueMatcherStepOutput(ValuePath root, Object actual, ValueConverter valueConverter, Set<ValuePath> valuePathsToHighlight) {
        this.root = root;
        this.actual = actual;
        this.valueConverter = valueConverter;
        this.valuePathsToHighlight = valuePathsToHighlight;
        this.cachedPrettyPrintOutput = new ArrayList<>();
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        if (!cachedPrettyPrintOutput.isEmpty()) {
            rePrintCached(printer);
        } else {
            printStepOutput(printer);
            cacheOutput(printer);
        }
    }

    private void rePrintCached(PrettyPrinter printer) {
        cachedPrettyPrintOutput.forEach(printer::addLine);
    }

    private void printStepOutput(PrettyPrinter printer) {
        printer.printLine();
        printer.setPathsDecoration(new PrettyPrinterDecorationToken("**", Color.RED), valuePathsToHighlight);
        printer.setValueConverter(valueConverter);

        printer.printObject(root, actual);
        printer.flushCurrentLine();
    }

    private void cacheOutput(PrettyPrinter printer) {
        printer.getLinesStream().forEach(cachedPrettyPrintOutput::add);
    }

    @Override
    public Map<String, ?> toMap() {
        PrettyPrinter printer = new PrettyPrinter(0);
        prettyPrint(printer);

        return Collections.singletonMap("styledText", printer.generateStyledTextListOfListsOfMaps());
    }
}
