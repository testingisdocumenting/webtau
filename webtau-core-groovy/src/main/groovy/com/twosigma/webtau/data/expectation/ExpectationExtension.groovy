/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.data.expectation

import com.twosigma.webtau.expectation.ActualValue
import com.twosigma.webtau.expectation.ValueMatcher

import static com.twosigma.webtau.groovy.ast.ShouldAstTransformation.SHOULD_BE_REPLACED_MESSAGE

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

    static ShouldWaitStub getShould(actual) {
        throw new IllegalStateException(SHOULD_BE_REPLACED_MESSAGE)
    }

    static ShouldWaitStub getWaitTo(actual) {
        throw new IllegalStateException(SHOULD_BE_REPLACED_MESSAGE)
    }

    static ShouldWaitStub getShouldBe(actual) {
        getShould(actual)
    }

    static ShouldWaitStub getWaitToBe(actual) {
        getWaitTo(actual)
    }

    static ShouldWaitStub getShouldNot(actual) {
        throw new IllegalStateException(SHOULD_BE_REPLACED_MESSAGE)
    }

    static ShouldWaitStub getWaitToNot(actual) {
        throw new IllegalStateException(SHOULD_BE_REPLACED_MESSAGE)
    }

    static ShouldWaitStub getShouldNotBe(actual) {
        getShouldNot(actual)
    }

    static ShouldWaitStub getWaitToNotBe(actual) {
        getWaitToNot(actual)
    }
}
