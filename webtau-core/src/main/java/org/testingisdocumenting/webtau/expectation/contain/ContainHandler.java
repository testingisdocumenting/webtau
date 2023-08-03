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

package org.testingisdocumenting.webtau.expectation.contain;

import org.testingisdocumenting.webtau.data.ValuePath;

public interface ContainHandler {
    boolean handle(Object actual, Object expected);

    void analyzeContain(ContainAnalyzer containAnalyzer, ValuePath actualPath, Object actual, Object expected);
    void analyzeNotContain(ContainAnalyzer containAnalyzer, ValuePath actualPath, Object actual, Object expected);

    /**
     * value optionally can be converted to another value to be passed down comparison chain.
     * exposed as outside method for more precise reporting of actual values in case of a failure.
     *
     * @param actual original actual
     * @param expected expected value
     * @return optionally converted actual
     */
    default Object convertedActual(Object actual, Object expected) {
        return actual;
    }

    /**
     * value optionally can be converted to another value to be passed down comparison chain.
     * exposed as outside method for more precise reporting of expected values for reporting
     *
     * @param actual original actual
     * @param expected original expected
     * @return optionally converted expected
     */
    default Object convertedExpected(Object actual, Object expected) {
        return expected;
    }
}
