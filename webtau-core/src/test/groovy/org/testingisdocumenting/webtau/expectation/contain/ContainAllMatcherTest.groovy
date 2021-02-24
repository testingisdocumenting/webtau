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

package org.testingisdocumenting.webtau.expectation.contain

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.*

class ContainAllMatcherTest {
    @Test
    void "should throw exception when value doesn't contain all expected value"() {
        code {
            actual(['a', 'b', 'd']).should(containAll('b', 'A'))
        } should throwException('\n[value] expect to contain all [b, A]\n' +
                '[value]: mismatches:\n' +
                '         \n' +
                '         [value][0]:   actual: "a" <java.lang.String>\n' +
                '                     expected: "A" <java.lang.String>\n' +
                '                                ^\n' +
                '         [value][1]:   actual: "b" <java.lang.String>\n' +
                '                     expected: "A" <java.lang.String>\n' +
                '                                ^\n' +
                '         [value][2]:   actual: "d" <java.lang.String>\n' +
                '                     expected: "A" <java.lang.String>\n' +
                '                                ^')
    }

    @Test
    void "should pass when value contains all expected values"() {
        actual(['a', 'b', 'd']).should(containAll('b', 'a'))
    }

    @Test
    void "should throw exception when value contain expected value, but should not"() {
        code {
            actual(['a', 'b', 'd']).shouldNot(containAll('b', 'a'))
        } should throwException('\n[value] expect to not contain all [b, a]\n' +
                '[value][1]: equals "b"\n' +
                '[value][0]: equals "a"')
    }

    @Test
    void "should pass when value not contains all expected values"() {
        actual(['a', 'b', 'd']).shouldNot(containAll('x', 'y'))
    }
}
