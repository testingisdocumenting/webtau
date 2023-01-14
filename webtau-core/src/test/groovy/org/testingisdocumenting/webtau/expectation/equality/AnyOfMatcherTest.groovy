/*
 * Copyright 2022 webtau maintainers
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

import org.junit.Assert
import org.junit.Test
import org.testingisdocumenting.webtau.data.ValuePath

import static org.testingisdocumenting.webtau.Matchers.*

class AnyOfMatcherTest {
    private final ValuePath actualPath = new ValuePath("value")

    @Test
    void "positive match"() {
        def actual = 10
        def matcher = new AnyOfMatcher([3, 10, greaterThan(8), 10])

        assert matcher.matches(actualPath, actual)

        Assert.assertEquals("matches any of [3, 10, <greater than 8>, 10]\n" +
                "value:   actual: 10 <java.lang.Integer>\n" +
                "       expected: 10 <java.lang.Integer>", matcher.matchedMessage(actualPath, actual))
    }

    @Test
    void "positive mismatch"() {
        def actual = 10
        def matcher = new AnyOfMatcher([3, lessThan(8), 1])

        assert !matcher.matches(actualPath, actual)
        Assert.assertEquals("mismatches:\n" +
                "\n" +
                "value:   actual: 10 <java.lang.Integer>\n" +
                "       expected: 3 <java.lang.Integer>\n" +
                "value: to be less than 8:\n" +
                "       mismatches:\n" +
                "       \n" +
                "       value:   actual: 10 <java.lang.Integer>\n" +
                "              expected: less than 8 <java.lang.Integer>\n" +
                "value:   actual: 10 <java.lang.Integer>\n" +
                "       expected: 1 <java.lang.Integer>", matcher.mismatchedMessage(actualPath, actual))
    }

    @Test
    void "negative match"() {
        def actual = 10
        def matcher = new AnyOfMatcher([1, 3, greaterThan(12)])

        assert matcher.negativeMatches(actualPath, actual)

        Assert.assertEquals("doesn't match any of [1, 3, <greater than 12>]\n" +
                "value:   actual: 10 <java.lang.Integer>\n" +
                "       expected: not 1 <java.lang.Integer>\n" +
                "value:   actual: 10 <java.lang.Integer>\n" +
                "       expected: not 3 <java.lang.Integer>\n" +
                "value: less than or equal to 12\n" +
                "       value:   actual: 10 <java.lang.Integer>\n" +
                "              expected: less than or equal to 12 <java.lang.Integer>",
                matcher.negativeMatchedMessage(actualPath, actual))
    }

    @Test
    void "negative mismatch"() {
        def actual = 10
        def matcher = new AnyOfMatcher([1, 3, greaterThan(8)])

        assert !matcher.negativeMatches(actualPath, actual)

        Assert.assertEquals("mismatches:\n" +
                "\n" +
                "value: to be less than or equal to 8:\n" +
                "       mismatches:\n" +
                "       \n" +
                "       value:   actual: 10 <java.lang.Integer>\n" +
                "              expected: less than or equal to 8 <java.lang.Integer>",
                matcher.negativeMismatchedMessage(actualPath, actual))
    }

    @Test
    void "full matcher with actual step"() {
        actual(8).shouldBe(anyOf(2, greaterThan(4)))
        actual(10).shouldNotBe(anyOf(3, 11, greaterThan(20)))
        actual("hello").shouldNotBe(anyOf("world", contain("super")))
    }
}
