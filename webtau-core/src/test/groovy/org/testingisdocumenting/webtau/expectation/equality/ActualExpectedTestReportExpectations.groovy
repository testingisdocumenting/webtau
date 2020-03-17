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

package org.testingisdocumenting.webtau.expectation.equality

class ActualExpectedTestReportExpectations {
    static String simpleActualExpectedWithIntegers(int actual, int expected) {
        return simpleActualExpectedWithIntegers(actual, null, expected)
    }

    static String simpleActualExpectedWithIntegers(int actual, String expectedPrefix, int expected) {
        def prefix = expectedPrefix != null && !expectedPrefix.isEmpty() && !expectedPrefix.endsWith(" ") ? expectedPrefix + " " : ""
        return "value:   actual: $actual <java.lang.Integer>\n" +
               "       expected: $prefix$expected <java.lang.Integer>"
    }
}
