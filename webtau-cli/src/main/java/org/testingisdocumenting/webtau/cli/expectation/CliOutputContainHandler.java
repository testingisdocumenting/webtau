/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.cli.expectation;

import org.testingisdocumenting.webtau.cli.CliOutput;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.contain.ContainAnalyzer;
import org.testingisdocumenting.webtau.expectation.contain.ContainHandler;
import org.testingisdocumenting.webtau.expectation.contain.handlers.IndexedValue;
import org.testingisdocumenting.webtau.expectation.contain.handlers.IterableContainAnalyzer;

import java.util.List;
import java.util.regex.Pattern;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class CliOutputContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof CliOutput;
    }

    @Override
    public void analyzeContain(ContainAnalyzer containAnalyzer, ValuePath actualPath, Object actual, Object expected) {
        CliOutput cliOutput = ((CliOutput) actual);
        IterableContainAnalyzer analyzer = new IterableContainAnalyzer(actualPath, cliOutput.copyLines(),
                adjustedExpected(expected), false);
        List<IndexedValue> indexedValues = analyzer.findContainingIndexedValues();

        if (indexedValues.isEmpty()) {
            containAnalyzer.reportMismatchedValue(expected);
        }

        indexedValues.forEach(iv -> cliOutput.registerMatchedLine(iv.idx()));
    }

    @Override
    public void analyzeNotContain(ContainAnalyzer containAnalyzer, ValuePath actualPath, Object actual, Object expected) {
        CliOutput cliOutput = ((CliOutput) actual);

        IterableContainAnalyzer analyzer = new IterableContainAnalyzer(actualPath, cliOutput.copyLines(),
                adjustedExpected(expected), true);
        List<IndexedValue> indexedValues = analyzer.findContainingIndexedValues();

        indexedValues.forEach(indexedValue ->
                containAnalyzer.reportMatch(this, actualPath.index(indexedValue.idx()),
                        () -> tokenizedMessage().matcher("equals").value(indexedValue.value())
                ));
    }

    /*
    for output, we want to be able to mark matched lines, and so want to treat output as a list of lines.
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
