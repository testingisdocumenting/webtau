/*
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

package com.twosigma.webtau.expectation.contain.handlers

import com.twosigma.webtau.expectation.contain.ContainAnalyzer
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.contain
import static com.twosigma.webtau.Ddjt.createActualPath
import static com.twosigma.webtau.Ddjt.throwException

class IterableContainHandlerTest {
    private ContainAnalyzer analyzer

    @Before
    void init() {
        analyzer = ContainAnalyzer.containAnalyzer()
    }

    @Test
    void "handles iterable as actual and anything as expected"() {
        def handler = new IterableContainHandler()
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
    void "mismatches when collection contain a value but should not"() {
        assert !analyzer.notContains(createActualPath('list'), ['hello', 'world', 'of', 'testing'], 'of')
        Assert.assertEquals('list[2]: equals "of"', analyzer.generateMismatchReport())
    }

    @Test
    void "mismatch report contains information about each match attempt"() {
        assert !analyzer.contains(createActualPath('list'), ['hello', 'world', 'of', 'testing'], 'off')

        Assert.assertEquals('list: mismatches:\n' +
                '      \n' +
                '      list[0]:   actual: "hello" <java.lang.String>\n' +
                '               expected: "off" <java.lang.String>\n' +
                '                          ^\n' +
                '      list[1]:   actual: "world" <java.lang.String>\n' +
                '               expected: "off" <java.lang.String>\n' +
                '                          ^\n' +
                '      list[2]:   actual: "of" <java.lang.String>\n' +
                '               expected: "off" <java.lang.String>\n' +
                '                            ^\n' +
                '      list[3]:   actual: "testing" <java.lang.String>\n' +
                '               expected: "off" <java.lang.String>\n' +
                '                          ^', analyzer.generateMismatchReport())

    }

    @Test
    void "works with complex types"() {
        assert !analyzer.contains(createActualPath('list'), [
            [firstName: 'FN1', lastName: 'LN1'],
            [firstName: 'FN2', lastName: 'LN2'],
            [firstName: 'FN3', lastName: 'LN3'],
        ], [firstName: 'FN31', lastName: 'LN3'])

        Assert.assertEquals('list: mismatches:\n' +
                '      \n' +
                '      list[0].firstName:   actual: "FN1" <java.lang.String>\n' +
                '                         expected: "FN31" <java.lang.String>\n' +
                '                                      ^\n' +
                '      list[0].lastName:   actual: "LN1" <java.lang.String>\n' +
                '                        expected: "LN3" <java.lang.String>\n' +
                '                                     ^\n' +
                '      list[1].firstName:   actual: "FN2" <java.lang.String>\n' +
                '                         expected: "FN31" <java.lang.String>\n' +
                '                                      ^\n' +
                '      list[1].lastName:   actual: "LN2" <java.lang.String>\n' +
                '                        expected: "LN3" <java.lang.String>\n' +
                '                                     ^\n' +
                '      list[2].firstName:   actual: "FN3" <java.lang.String>\n' +
                '                         expected: "FN31" <java.lang.String>\n' +
                '                                       ^', analyzer.generateMismatchReport())
    }

    @Test
    void "contain matcher throws when doesn't match"() {
        code {
            actual(['hello', 'world', 'of', 'testing']).should(contain('wod'))
        } should throwException(~/\[value] expect to contain "wod"/)
    }

    @Test
    void "contain matcher throws when contains but should not"() {
        code {
            actual(['hello', 'world', 'of', 'testing']).shouldNot(contain('of'))
        } should throwException('\n[value] expect to not contain "of"\n' +
            '[value][2]: equals "of"')
    }
}
