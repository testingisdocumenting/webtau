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

package org.testingisdocumenting.webtau.expectation.contain.handlers

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class MapContainHandlerTest {
    @Test
    void "should pass when matched"() {
        def actualMap = [hello: 1, world: 2]
        def expectedMap = [hello: 1, world: 2]

        actual(actualMap).should(contain(expectedMap))
    }

    @Test
    void "should ignore extra actual properties"() {
        def actualMap = [hello: 1, world: 2]
        def expectedMap = [hello: 1]

        actual(actualMap).should(contain(expectedMap))
    }

    @Test
    void "should display value mismatch"() {
        def actualMap = [hello: 1, world: 2]
        def expectedMap = [hello: 1, world: 3]

        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to contain {"hello": 1, "world": 3}:\n' +
                '    [value].world:  actual: 2 <java.lang.Integer>\n' +
                '                  expected: 3 <java.lang.Integer> (Xms)\n' +
                '  \n' +
                '  {"hello": 1, "world": **2**}') {
            actual(actualMap).should(contain(expectedMap))
        }
    }

    @Test
    void "should detect missing key"() {
        def actualMap = [hello: 1, world: 2, nested: [test: "value"], anotherExtra: [gold: "fish"]]
        def expectedMap = [hello: 1, world: 2, extra: 3, nested: [test: "value", another: "a-value"], anotherExtra: [nestedInExtra: 100]]

        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to contain {\n' +
                '                                        "hello": 1,\n' +
                '                                        "world": 2,\n' +
                '                                        "extra": 3,\n' +
                '                                        "nested": {"test": "value", "another": "a-value"},\n' +
                '                                        "anotherExtra": {"nestedInExtra": 100}\n' +
                '                                      }:\n' +
                '    missing values:\n' +
                '    \n' +
                '    [value].extra: 3\n' +
                '    [value].nested.another: "a-value"\n' +
                '    [value].anotherExtra.nestedInExtra: 100 (Xms)\n' +
                '  \n' +
                '  {\n' +
                '    "hello": 1,\n' +
                '    "world": 2,\n' +
                '    "nested": {"test": "value", "another": **<missing>**},\n' +
                '    "anotherExtra": {"gold": "fish", "nestedInExtra": **<missing>**},\n' +
                '    "extra": **<missing>**\n' +
                '  }') {
            actual(actualMap).should(contain(expectedMap))
        }
    }

    @Test
    void "should display both missing and mismatched"() {
        def actualMap = [hello: 1, world: 2]
        def expectedMap = [hello: 2, another: 3]

        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to contain {"hello": 2, "another": 3}:\n' +
                '    mismatches:\n' +
                '    \n' +
                '    [value].hello:  actual: 1 <java.lang.Integer>\n' +
                '                  expected: 2 <java.lang.Integer>\n' +
                '    \n' +
                '    missing values:\n' +
                '    \n' +
                '    [value].another: 3 (Xms)\n' +
                '  \n' +
                '  {"hello": **1**, "world": 2, "another": **<missing>**}') {
            actual(actualMap).should(contain(expectedMap))
        }
    }

    @Test
    void "should pass when not contain"() {
        def actualMap = [hello: 1, world: 2]
        def expectedMap = [three: 3]

        actual(actualMap).shouldNot(contain(expectedMap))
    }

    @Test
    void "should pass when key is present but value is not matched"() {
        def actualMap = [hello: 1, world: 2]
        def expectedMap = [world: 3]

        actual(actualMap).shouldNot(contain(expectedMap))
    }

    @Test
    void "should fail when key is present and value is matched"() {
        def actualMap = [hello: 1, world: 2]
        def expectedMap = [world: 2]

        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to not contain {"world": 2}:\n' +
                '    [value].world:  actual: 2 <java.lang.Integer>\n' +
                '                  expected: not 2 <java.lang.Integer> (Xms)\n' +
                '  \n' +
                '  {"hello": 1, "world": **2**}') {
            actual(actualMap).shouldNot(contain(expectedMap))
        }
    }
}
