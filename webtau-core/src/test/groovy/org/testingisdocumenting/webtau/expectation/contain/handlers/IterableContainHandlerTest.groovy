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

package org.testingisdocumenting.webtau.expectation.contain.handlers

import org.testingisdocumenting.webtau.expectation.contain.ContainAnalyzer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

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
    void "works with complex types"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to contain {"firstName": "FN31", "lastName": "LN3"}: no match found (Xms)\n' +
                '  \n' +
                '  [\n' +
                '    {"firstName": "FN1", "lastName": "LN1"},\n' +
                '    {"firstName": "FN2", "lastName": "LN2"},\n' +
                '    {"firstName": "FN3", "lastName": "LN3"}\n' +
                '  ]') {
            actual([
                    [firstName: 'FN1', lastName: 'LN1'],
                    [firstName: 'FN2', lastName: 'LN2'],
                    [firstName: 'FN3', lastName: 'LN3'],
            ]).should(contain([firstName: 'FN31', lastName: 'LN3']))
        }
    }

    @Test
    void "contain matcher throws when doesn't match"() {
        code {
            actual(['hello', 'world', 'of', 'testing']).should(contain('wod'))
        } should throwException("no match found")
    }

    @Test
    void "contain matcher throws when contains but should not"() {
        code {
            actual(['hello', 'world', 'of', 'testing']).shouldNot(contain('of'))
        } should throwException("match is found")
    }
}
