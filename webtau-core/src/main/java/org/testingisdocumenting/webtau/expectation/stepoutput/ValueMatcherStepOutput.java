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
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.render.PrettyPrinterDecorationToken;
import org.testingisdocumenting.webtau.reporter.WebTauStepOutput;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ValueMatcherStepOutput implements WebTauStepOutput {
    private final ValuePath root;
    private final Object actual;
    private final List<ValuePath> valuePathsToHighlight;

    public ValueMatcherStepOutput(ValuePath root, Object actual, List<ValuePath> valuePathsToHighlight) {
        this.root = root;
        this.actual = actual;
        this.valuePathsToHighlight = valuePathsToHighlight;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        printer.setPathsDecoration(new PrettyPrinterDecorationToken("**", Color.RED), valuePathsToHighlight);
        printer.printObject(root, actual);
        printer.flush();
    }

    @Override
    public Map<String, ?> toMap() {
        return Collections.emptyMap();
    }
}
