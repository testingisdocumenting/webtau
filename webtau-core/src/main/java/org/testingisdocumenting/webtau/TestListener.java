/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau;

import org.testingisdocumenting.webtau.reporter.WebTauTest;

/**
 * Lowest common denominator for Groovy Standalone Tests, JUnit4, JUnit5
 */
public interface TestListener {
    /**
     * before any test is ran
     */
    default void beforeFirstTest() {}

    /**
     * before test code is invoked
     * @param test test
     */
    default void beforeTestRun(WebTauTest test) {}

    /**
     * after test code is invoked
     * @param test test
     */
    default void afterTestRun(WebTauTest test) {}

    /**
     * after all the tests are invoked
     */
    default void afterAllTests() {}

    /**
     * after test is ran but before its first statement.
     * executed code in this listener is considered to be part of a test.
     * @param test test
     */
    default void beforeFirstTestStatement(WebTauTest test) {}

    /**
     * right before test considered to be complete. May not be executed if the test didn't reach the last statement.
     * executed code in this listener is considered to be part of a test.
     * @param test test
     */
    default void afterLastTestStatement(WebTauTest test) {}
}
