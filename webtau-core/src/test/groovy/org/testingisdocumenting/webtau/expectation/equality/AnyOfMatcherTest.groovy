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

import org.junit.Test
import org.example.domain.Account

import static org.testingisdocumenting.webtau.WebTauCore.*
import org.testingisdocumenting.webtau.data.DummyLiveValue

import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*

class AnyOfMatcherTest {
    @Test
    void "positive match"() {
        runAndValidateOutput('. [value] matches any of [3, 10, <greater than 8>, 10] (Xms)') {
            actual(10).shouldBe(anyOf(3, 10, greaterThan(8), 10))
        }
    }

    @Test
    void "positive mismatch"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to match any of [3, <less than 8>, 1]:\n' +
                '      actual: 12 <java.lang.Integer>\n' +
                '    expected: 3 <java.lang.Integer>\n' +
                '      actual: 12 <java.lang.Integer>\n' +
                '    expected: less than 8 <java.lang.Integer>\n' +
                '      actual: 12 <java.lang.Integer>\n' +
                '    expected: 1 <java.lang.Integer> (Xms)') {
            actual(12).shouldBe(anyOf(3, lessThan(8), 1))
        }
    }

    @Test
    void "negative match"() {
        runAndValidateOutput('. [value] doesn\'t match any of [3, 11, <less than 8>] (Xms)') {
            actual(10).shouldNotBe(anyOf(3, 11, lessThan(8)))
        }
    }

    @Test
    void "negative mismatch"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to not match any of [3, 11, <less than 12>]:\n' +
                '      actual: 10 <java.lang.Integer>\n' +
                '    expected: greater than or equal to 12 <java.lang.Integer> (Xms)') {
            actual(10).shouldNotBe(anyOf(3, 11, lessThan(12)))
        }
    }

    @Test
    void "wait on live value"() {
        def liveValue = new DummyLiveValue([1, 10, 100, 1000, 2000])
        actual(liveValue).waitToBe(anyOf(100, 1000))
    }

    @Test
    void "renders converted java bean in case of mismatch"() {
        def bean = new Account("id1", "name1", 100)

        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to match any of [{"id": "id1", "name": "name2"}, {"id": "id1", "name": "name2"}]:\n' +
                '    [value].name:  actual: "name1" <java.lang.String>\n' +
                '                 expected: "name2" <java.lang.String>\n' +
                '                                ^\n' +
                '    [value].name:  actual: "name1" <java.lang.String>\n' +
                '                 expected: "name2" <java.lang.String>\n' +
                '                                ^ (Xms)\n' +
                '  \n' +
                '  {"id": "id1", "money": org.example.domain.Money@<ref>, "name": "name1"}') {
            actual(bean).shouldBe(anyOf(
                    map("id", "id1", "name", "name2"),
                    map("id", "id1", "name", "name2")))
        }
    }
}