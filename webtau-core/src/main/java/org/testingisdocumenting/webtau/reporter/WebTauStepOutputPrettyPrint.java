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

package org.testingisdocumenting.webtau.reporter;

import org.testingisdocumenting.webtau.data.render.PrettyPrinter;

import java.util.Collections;
import java.util.Map;

public class WebTauStepOutputPrettyPrint implements WebTauStepOutput {
    private final Object value;

    public WebTauStepOutputPrettyPrint(Object value) {
        this.value = value;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        printer.printObject(value);
        printer.flushCurrentLine();
    }

    @Override
    public Map<String, ?> toMap() {
        PrettyPrinter printer = new PrettyPrinter(0);
        prettyPrint(printer);

        return Collections.singletonMap("styledText", printer.generateStyledTextListOfListsOfMaps());
    }
}
