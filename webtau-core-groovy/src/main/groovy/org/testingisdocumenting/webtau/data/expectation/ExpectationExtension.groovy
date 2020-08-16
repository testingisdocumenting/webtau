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

package org.testingisdocumenting.webtau.data.expectation

import org.testingisdocumenting.webtau.expectation.ActualValue
import org.testingisdocumenting.webtau.expectation.ValueMatcher

import static org.testingisdocumenting.webtau.groovy.ast.ShouldAstTransformation.SHOULD_BE_REPLACED_MESSAGE

class ExpectationExtension {
    static void should(actual, ValueMatcher valueMatcher) {
        new ActualValue(actual).should(valueMatcher)
    }

    static void shouldBe(actual, ValueMatcher valueMatcher) {
        should(actual, valueMatcher)
    }

    static void shouldNot(actual, ValueMatcher valueMatcher) {
        new ActualValue(actual).shouldNot(valueMatcher)
    }

    static void shouldNotBe(actual, ValueMatcher valueMatcher) {
        shouldNot(actual, valueMatcher)
    }

    static Object getShould(actual) {
        throw new IllegalStateException(SHOULD_BE_REPLACED_MESSAGE)
    }

    static Object getWaitTo(actual) {
        throw new IllegalStateException(SHOULD_BE_REPLACED_MESSAGE)
    }

    static Object getShouldBe(actual) {
        getShould(actual)
    }

    static Object getWaitToBe(actual) {
        getWaitTo(actual)
    }

    static Object getShouldNot(actual) {
        throw new IllegalStateException(SHOULD_BE_REPLACED_MESSAGE)
    }

    static Object getWaitToNot(actual) {
        throw new IllegalStateException(SHOULD_BE_REPLACED_MESSAGE)
    }

    static Object getShouldNotBe(actual) {
        getShouldNot(actual)
    }

    static Object getWaitToNotBe(actual) {
        getWaitToNot(actual)
    }
}
