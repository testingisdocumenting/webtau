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

import org.testingisdocumenting.webtau.data.render.IterablePrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.reporter.WebTauStepOutput;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ValueMatcherIterableStepOutput implements WebTauStepOutput {
    private final Iterable<?> actual;
    private final List<ValuePath> valuePaths;

    public ValueMatcherIterableStepOutput(Iterable<?> actual, List<ValuePath> valuePaths) {
        this.actual = actual;
        this.valuePaths = valuePaths;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        new IterablePrettyPrintable(actual).prettyPrint(printer);
    }

    @Override
    public Map<String, ?> toMap() {
        return Collections.emptyMap();
    }
}
