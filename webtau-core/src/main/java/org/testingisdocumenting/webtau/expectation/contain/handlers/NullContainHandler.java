/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.contain.handlers;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.contain.ContainAnalyzer;
import org.testingisdocumenting.webtau.expectation.contain.ContainHandler;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class NullContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual == null || expected == null;
    }

    @Override
    public void analyzeContain(ContainAnalyzer containAnalyzer, ValuePath actualPath, Object actual, Object expected) {
        containAnalyzer.reportMismatch(this, actualPath, () -> tokenizedMessage().valueFirstLinesOnly(actual).error("doesn't contain").valueFirstLinesOnly(expected));
    }

    @Override
    public void analyzeNotContain(ContainAnalyzer containAnalyzer, ValuePath actualPath, Object actual, Object expected) {
        containAnalyzer.reportMismatch(this, actualPath, () -> tokenizedMessage().valueFirstLinesOnly(actual).error("doesn't contain").valueFirstLinesOnly(expected));
    }
}
