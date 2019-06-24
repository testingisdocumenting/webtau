/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.cli.expectation;

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.contain.ContainAnalyzer;
import com.twosigma.webtau.expectation.contain.ContainHandler;
import com.twosigma.webtau.expectation.contain.handlers.IndexedValue;
import com.twosigma.webtau.expectation.contain.handlers.IterableContainAnalyzer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class CliOutputContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof CliOutput;
    }

    @Override
    public void analyzeContain(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        CliOutput cliOutput = ((CliOutput) actual);
        IterableContainAnalyzer analyzer = new IterableContainAnalyzer(actualPath, cliOutput.getLines(),
                adjustedExpected(expected));
        List<IndexedValue> indexedValues = analyzer.containingIndexedValues();

        if (indexedValues.isEmpty()) {
            containAnalyzer.reportMismatch(this, actualPath, analyzer.getComparator()
                    .generateEqualMismatchReport());
        }

        indexedValues.forEach(iv -> cliOutput.registerMatchedLine(iv.getIdx()));
    }

    @Override
    public void analyzeNotContain(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        CliOutput cliOutput = ((CliOutput) actual);

        IterableContainAnalyzer analyzer = new IterableContainAnalyzer(actualPath, cliOutput.getLines(),
                adjustedExpected(expected));
        List<IndexedValue> indexedValues = analyzer.containingIndexedValues();

        indexedValues.forEach(indexedValue -> {
                    containAnalyzer.reportMismatch(this, actualPath.index(indexedValue.getIdx()),
                            "equals " + DataRenderers.render(indexedValue.getValue()));
                }
        );
    }

    /*
    for output we want to be able to mark matched lines, and so want to treat output as a list of lines.
    at the same time we want a substring match within a line.
    so we will automatically convert expected text to a quoted regexp and pass it down to contain analyzer.
     */
    public Object adjustedExpected(Object expected) {
        if (expected instanceof String) {
            return Pattern.compile(Pattern.quote(expected.toString()));
        }

        return expected;
    }
}
