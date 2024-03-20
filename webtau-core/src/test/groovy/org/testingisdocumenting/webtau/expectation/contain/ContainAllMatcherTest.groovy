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
import org.testingisdocumenting.webtau.Account
import org.testingisdocumenting.webtau.Address

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.WebTauCore.map
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class ContainAllMatcherTest {
    @Test
    void "fails when not all values are present"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to contain all ["b", "A"]:\n' +
                '    no matches found for: ["A"] (Xms)\n' +
                '  \n' +
                '  ["a", "b", "d"]') {
            actual(['a', 'b', 'd']).should(containAll('b', 'A'))
        }
    }

    @Test
    void "passes when all values are present"() {
        actual(['a', 'b', 'd']).should(containAll('b', 'a'))
    }

    @Test
    void "passes when all values are present as list"() {
        actual(['a', 'b', 'd']).should(containAll(['b', 'a']))
    }

    @Test
    void "negative matcher fails only when all the values are present "() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to not contain all ["b", "a"]:\n' +
                '    [value][1]:  actual:     "b" <java.lang.String>\n' +
                '               expected: not "b" <java.lang.String>\n' +
                '    [value][0]:  actual:     "a" <java.lang.String>\n' +
                '               expected: not "a" <java.lang.String> (Xms)\n' +
                '  \n' +
                '  [**"a"**, **"b"**, "d"]') {
            actual(['a', 'b', 'd']).shouldNot(containAll('b', 'a'))
        }
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

    @Test
    void "list of beans contain all maps prints converted beans"() {
        def address = new Address("city", "zipcode")
        def ac1 = new Account("id1", "name1", "d1", address)
        def ac2 = new Account("id2", "name2", "d2", address)
        def ac3 = new Account("id3", "name3", "d3", address)
        def beans = [ac1, ac2, ac3]

        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to contain all [{"id": "id1", "name": "name2"}, {"id": "id2", "name": "name2"}]:\n' +
                '    no matches found for: [{"id": "id1", "name": "name2"}] (Xms)\n' +
                '  \n' +
                '  [\n' +
                '    {\n' +
                '      "address": org.testingisdocumenting.webtau.Address@<ref>,\n' +
                '      "description": "d1",\n' +
                '      "id": "id1",\n' +
                '      "name": **"name1"**\n' +
                '    },\n' +
                '    {\n' +
                '      "address": org.testingisdocumenting.webtau.Address@<ref>,\n' +
                '      "description": "d2",\n' +
                '      "id": **"id2"**,\n' +
                '      "name": "name2"\n' +
                '    },\n' +
                '    {\n' +
                '      "address": org.testingisdocumenting.webtau.Address@<ref>,\n' +
                '      "description": "d3",\n' +
                '      "id": "id3",\n' +
                '      "name": "name3"\n' +
                '    }\n' +
                '  ]') {
            actual(beans).should(containAll(
                    map("id", "id1", "name", "name2"),
                    map("id", "id2", "name", "name2")))
        }
    }
}
