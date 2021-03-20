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
    void "fails when not all values are present"() {
        code {
            actual(['a', 'b', 'd']).should(containAll('b', 'A'))
        } should throwException('\n[value] expects to contain all [b, A]\n' +
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
    void "passes when all values are present"() {
        actual(['a', 'b', 'd']).should(containAll('b', 'a'))
    }

    @Test
    void "negative matcher fails only when all the values are present "() {
        code {
            actual(['a', 'b', 'd']).shouldNot(containAll('b', 'a'))
        } should throwException('\n[value] expects to not contain all [b, a]\n' +
                '[value][1]: equals "b"\n' +
                '[value][0]: equals "a"')
    }

    @Test
    void "negative matcher passes when only some of values are present"() {
        actual(['a', 'b', 'd']).shouldNot(containAll('b', 'a', 'x'))
    }

    @Test
    void "negative matcher passes when all the values are missing"() {
        actual(['a', 'b', 'd']).shouldNot(containAll('x', 'y'))
    }

    @Test
    void "containing all alias"() {
        actual([['a'], ['b'], ['c', 'd', 'e']]).should(contain(containingAll('d', 'e')))
    }
}
