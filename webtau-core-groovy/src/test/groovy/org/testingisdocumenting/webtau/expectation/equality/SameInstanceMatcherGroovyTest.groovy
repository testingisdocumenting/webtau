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

package org.testingisdocumenting.webtau.expectation.equality

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.sameInstance

class SameInstanceMatcherGroovyTest {
    @Test
    void matches() {
        def value = [1, 2, 3]
        def anotherValue = value
        // should-be
        value.shouldBe sameInstance(anotherValue)
        // should-be
    }

    @Test
    void negativeMatches() {
        def value = [1, 2, 3]
        def anotherValue = [1, 2, 3]
        // should-not-be
        value.shouldNotBe sameInstance(anotherValue)
        // should-not-be
    }
}
