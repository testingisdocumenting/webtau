/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.equality.handlers

import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator
import org.junit.Test
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.junit.Assert.assertEquals
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class StringCompareToHandlerTest {
    @Test
    void "handles instances of CharSequence and Characters as actual or expected"() {
        def handler = new StringCompareToHandler()
        def var = 100

        assert handler.handleEquality('test', 'test')
        assert handler.handleEquality('test', "test $var")
        assert handler.handleEquality("test $var", 'test')
        assert handler.handleEquality('t', Character.valueOf('t'.toCharArray()[0]))
        assert handler.handleEquality(Character.valueOf('t'.toCharArray()[0]), 't')

        assert ! handler.handleEquality('test', 100)
        assert ! handler.handleEquality('test', ~/regexp/)
    }

    @Test
    void "compares string and character"() {
        actual('t').should(equal(Character.valueOf('t'.toCharArray()[0])))
    }

    @Test
    void "compares string and gstring"() {
        def var = 100
        actual('test ' + var).should(equal("test $var"))
        actual("test $var").should(equal('test ' + var))
    }

    @Test
    void "reports types before conversion to string in case of failure"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting text to equal "b":\n' +
                '      actual: "t" <java.lang.String> (before conversion: t <java.lang.Character>)\n' +
                '    expected: "b" <java.lang.String>\n' +
                '               ^ (Xms)') {
            actual(Character.valueOf('t'.toCharArray()[0]), "text").should(equal('b'))
        }
    }

    @Test
    void "shows caret indicator for first mismatch of a one line strings"() {
        runExpectExceptionAndValidateOutput(AssertionError,
                'X failed expecting text to equal "herlo world":\n' +
                '      actual: "hello world" <java.lang.String>\n' +
                '    expected: "herlo world" <java.lang.String>\n' +
                '                 ^ (Xms)') {
            actual('hello world', 'text').should(equal('herlo world'))
        }
    }

    @Test
    void "does not shows caret when strings match but not supposed to"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting text to not equal "hello world":\n' +
                '      actual:     "hello world" <java.lang.String>\n' +
                '    expected: not "hello world" <java.lang.String> (Xms)') {
            actual('hello world', 'text').shouldNot(equal('hello world'))
        }
    }

    @Test
    void "renders multiline strings in blocks"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting text to equal _________________\n' +
                '                                 hello world world\n' +
                '                                 hi again\n' +
                '                                 _________________:\n' +
                '    first mismatch at line idx 1:\n' +
                '      actual: "hello again"\n' +
                '    expected: "hi again"\n' +
                '                ^ (Xms)\n' +
                '  \n' +
                '   _________________\n' +
                '   hello world world\n' +
                '  *hello again*\n' +
                '   _________________') {
            actual("hello world world\nhello again", "text").should(equal('hello world world\nhi again'))
        }
    }

    @Test
    void "properly handles new line symbol at the end of a single line"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting text to equal ___________\n' +
                '                                 single lone\n' +
                '                                 \n' +
                '                                 ___________:\n' +
                '    first mismatch at line idx 0:\n' +
                '      actual: "single line"\n' +
                '    expected: "single lone"\n' +
                '                       ^ (Xms)\n' +
                '  \n' +
                '   ___________\n' +
                '  *single line*\n' +
                '   \n' +
                '   ___________') {
            actual('single line\n', 'text').should(equal('single lone\n'))
        }
    }

    @Test
    void "properly handles multiple new line symbols at the end of a single line"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting text to equal ___________\n' +
                '                                 single lone\n' +
                '                                 \n' +
                '                                 \n' +
                '                                 \n' +
                '                                 ...:\n' +
                '    first mismatch at line idx 0:\n' +
                '      actual: "single line"\n' +
                '    expected: "single lone"\n' +
                '                       ^ (Xms)\n' +
                '  \n' +
                '   ___________\n' +
                '  *single line*\n' +
                '   \n' +
                '   \n' +
                '   \n' +
                '   ___________') {
            actual('single line\n\n\n', 'text').should(equal('single lone\n\n\n'))
        }
    }

    @Test
    void "reports different number of lines"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting text to equal _________________\n' +
                '                                 hello world world\n' +
                '                                 hi again\n' +
                '                                 _________________:\n' +
                '    different number of lines, expected: 3 actual: 2\n' +
                '    first mismatch at line idx 0:\n' +
                '      actual: ""\n' +
                '    expected: "hello world world"\n' +
                '               ^ (Xms)\n' +
                '  \n' +
                '   _________________\n' +
                '  **\n' +
                '   hello world world\n' +
                '   hello again\n' +
                '   _________________') {
            actual('\nhello world world\nhello again', 'text').should(equal('hello world world\nhi again'))
        }
    }

    @Test
    void "reports different line ending"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting text to equal _________________\n' +
                '                                 hello world world\n' +
                '                                 hello again\n' +
                '                                 _________________:\n' +
                '    different line endings, expected doesn\'t contain \\r, but actual does (Xms)\n' +
                '  \n' +
                '  _________________\n' +
                '  hello world world\n' +
                '  hello again\n' +
                '  _________________') {
            actual('hello world world\n\rhello again', 'text').should(equal('hello world world\nhello again'))
        }
    }
}
