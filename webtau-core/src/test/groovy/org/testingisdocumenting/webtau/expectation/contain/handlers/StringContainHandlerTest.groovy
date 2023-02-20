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

import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.testingisdocumenting.webtau.expectation.contain.ContainAnalyzer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class StringContainHandlerTest {
    private ContainAnalyzer analyzer

    @Before
    void init() {
        analyzer = ContainAnalyzer.containAnalyzer()
    }

    @Test
    void "handles char sequences"() {
        def handler = new StringContainHandler()
        def var = 100

        assert handler.handle('text', 'ext')
        assert handler.handle('text', "ext $var")
        assert !handler.handle('text', 10)
        assert !handler.handle(10, 20)
    }

    @Test
    void "no mismatches when actual string contains expected"() {
        assert analyzer.contains(createActualPath('text'), 'hello world', 'world')
    }

    @Test
    void "converts gstring to string before comparison"() {
        def var = 100
        assert analyzer.contains(createActualPath('text'), "hello $var world", 'world')
        assert analyzer.contains(createActualPath('text'), 'hello world ' + var, "world $var")
    }

    @Test
    void "contains passes"() {
        actual("hello").should(contain("lo"))
    }

    @Test
    void "contains fails"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to contain "hi": no match found (Xms)\n' +
                '  \n' +
                '  **"hello"**') {
            actual("hello").should(contain("hi"))
        }
    }

    @Test
    void "not contains passes"() {
        actual("hello").shouldNot(contain("hi"))
    }

    @Test
    void "not contains fails"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to not contain "lo": contains at idx 3 (Xms)\n' +
                '  \n' +
                '  **"hello"**') {
            actual("hello").shouldNot(contain("lo"))
        }
    }
}
