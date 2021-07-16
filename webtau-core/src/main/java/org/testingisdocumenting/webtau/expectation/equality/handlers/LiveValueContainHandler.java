/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.testingisdocumenting.webtau.data.live.LiveValue;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.contain.ContainAnalyzer;
import org.testingisdocumenting.webtau.expectation.contain.ContainHandler;

public class LiveValueContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof LiveValue;
    }

    @Override
    public void analyzeContain(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        LiveValue<?> actualLiveValue = (LiveValue<?>) actual;
        containAnalyzer.contains(actualPath, actualLiveValue.get(), expected);
    }

    @Override
    public void analyzeNotContain(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        LiveValue<?> actualLiveValue = (LiveValue<?>) actual;
        containAnalyzer.notContains(actualPath, actualLiveValue.get(), expected);
    }
}
