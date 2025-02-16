/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.contain.handlers

import org.testingisdocumenting.webtau.expectation.contain.ContainAnalyzer
import org.junit.Before
import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class IterableAndSingleValueContainHandlerTest {
    private ContainAnalyzer analyzer

    @Before
    void init() {
        analyzer = ContainAnalyzer.containAnalyzer()
    }

    @Test
    void "handles iterable as actual and anything as expected"() {
        def handler = new IterableAndSingleValueContainHandler()
        assert handler.handle([1, 2], 1)
        assert handler.handle([1, 2], 'hello')
        assert !handler.handle('hello', 'hello')
        assert !handler.handle(1, 'hello')
    }

    @Test
    void "no mismatches when collection contains a value"() {
        assert analyzer.contains(createActualPath('list'), ['hello', 'world', 'of', 'testing'], 'world')
    }

    @Test
    void "no mismatches when collection doesn't contain a value and should not"() {
        assert analyzer.notContains(createActualPath('list'), ['hello', 'world', 'of', 'testing'], 'off')
    }

    @Test
    void "highlights and prints element that matched when expecting to not contain"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to not contain 2:\n' +
                '    [value][1]:  actual: 2 <java.lang.Integer>\n' +
                '               expected: not 2 <java.lang.Integer> (Xms)\n' +
                '  \n' +
                '  [1, **2**, 3]') {
            actual([1, 2, 3]).shouldNot(contain(2))
        }
    }

    @Test
    void "displays complex type when no match found"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to contain {"firstName": "FN31", "lastName": "LN3"}:\n' +
                '    [value][2].firstName:  actual: "FN3" <java.lang.String>\n' +
                '                         expected: "FN31" <java.lang.String>\n' +
                '                                       ^ (Xms)\n' +
                '  \n' +
                '  [\n' +
                '    {"firstName": "FN1", "lastName": "LN1"},\n' +
                '    {"firstName": "FN2", "lastName": "LN2"},\n' +
                '    {"firstName": **"FN3"**, "lastName": "LN3"}\n' +
                '  ]') {
            actual([
                    [firstName: 'FN1', lastName: 'LN1'],
                    [firstName: 'FN2', lastName: 'LN2'],
                    [firstName: 'FN3', lastName: 'LN3'],
            ]).should(contain([firstName: 'FN31', lastName: 'LN3']))
        }
    }

    @Test
    void "displays missing and mismatched of the suspect"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to contain {"firstName": "FN2", "lastName": "LN3", "game": "on"}:\n' +
                '    possible mismatches:\n' +
                '    \n' +
                '    [value][1].lastName:  actual: "LN2" <java.lang.String>\n' +
                '                        expected: "LN3" <java.lang.String>\n' +
                '                                     ^\n' +
                '    [value][2].firstName:  actual: "FN3" <java.lang.String>\n' +
                '                         expected: "FN2" <java.lang.String>\n' +
                '                                      ^\n' +
                '    \n' +
                '    possible missing values:\n' +
                '    \n' +
                '    [value][1].game: "on"\n' +
                '    [value][2].game: "on" (Xms)\n' +
                '  \n' +
                '  [\n' +
                '    {"firstName": "FN1", "lastName": "LN1", "extra": "T", "game": <missing>},\n' +
                '    {"firstName": "FN2", "lastName": **"LN2"**, "extra": "B", "game": **<missing>**},\n' +
                '    {"firstName": **"FN3"**, "lastName": "LN3", "wrong": "C", "game": **<missing>**}\n' +
                '  ]') {
            actual([
                    [firstName: 'FN1', lastName: 'LN1', extra: "T"],
                    [firstName: 'FN2', lastName: 'LN2', extra: "B"],
                    [firstName: 'FN3', lastName: 'LN3', wrong: "C"],
            ]).should(contain([firstName: 'FN2', lastName: 'LN3', game: "on"]))
        }
    }

    @Test
    void "complex values not contain passes when only a subset of fields matches"() {
        actual([
                [firstName: 'FN1', lastName: 'LN1'],
                [firstName: 'FN2', lastName: 'LN2'],
                [firstName: 'FN3', lastName: 'LN3'],
        ]).shouldNot(contain([firstName: 'FN31', lastName: 'LN3']))
    }

    @Test
    void "highlights and prints complex element that matched when expecting to not contain"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to not contain {"firstName": "FN3", "lastName": "LN3"}:\n' +
                '    [value][2].firstName:  actual:     "FN3" <java.lang.String>\n' +
                '                         expected: not "FN3" <java.lang.String>\n' +
                '    [value][2].lastName:  actual:     "LN3" <java.lang.String>\n' +
                '                        expected: not "LN3" <java.lang.String> (Xms)\n' +
                '  \n' +
                '  [\n' +
                '    {"firstName": "FN1", "lastName": "LN1"},\n' +
                '    {"firstName": "FN2", "lastName": "LN2"},\n' +
                '    {"firstName": **"FN3"**, "lastName": **"LN3"**}\n' +
                '  ]') {
            actual([
                    [firstName: 'FN1', lastName: 'LN1'],
                    [firstName: 'FN2', lastName: 'LN2'],
                    [firstName: 'FN3', lastName: 'LN3'],
            ]).shouldNot(contain([firstName: 'FN3', lastName: 'LN3']))
        }
    }

    @Test
    void "contain matcher throws when doesn't match"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to contain "wod": no match found (Xms)\n' +
                '  \n' +
                '  ["hello", "world", "of", "testing"]') {
            actual(['hello', 'world', 'of', 'testing']).should(contain('wod'))
        }
    }
}
