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

package org.testingisdocumenting.webtau.expectation.code

import org.junit.Test

import java.lang.reflect.UndeclaredThrowableException

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*

class ThrowExceptionMatcherGroovyTest {
    @Test
    void "should validate exception message"() {
        runExpectExceptionAndValidateOutput(AssertionError, contain('> expecting code to throw exception "error message1"\n' +
                'X failed expecting code to throw exception "error message1":\n' +
                '    exception.message:  actual: "error message" <java.lang.String>\n' +
                '                      expected: "error message1" <java.lang.String>\n' +
                '                                              ^')) {
            code {
                throw new RuntimeException('error message')
            } should throwException('error message1')
        }
    }

    @Test
    void "should validate exception message using regexp"() {
        runExpectExceptionAndValidateOutput(AssertionError, contain('exception.message:    actual string: error message\n' +
                '                       expected pattern: ~/error \\d+/')) {
            code {
                throw new RuntimeException('error message')
            } should throwException(~/error \d+/)
        }
    }

    @Test
    void "should validate exception class"() {
        runExpectExceptionAndValidateOutput(AssertionError, contain('X failed expecting code to throw exception java.lang.UnsupportedOperationException:\n' +
                '    exception.class:  actual: java.lang.IllegalArgumentException <java.lang.Class>\n' +
                '                    expected: java.lang.UnsupportedOperationException <java.lang.Class>')) {
            code {
                throw new IllegalArgumentException('error message')
            } should throwException(UnsupportedOperationException)
        }
    }

    @Test
    void "should validate exception class and expected message pattern"() {
        runExpectExceptionAndValidateOutput(AssertionError, contain('X failed expecting code to throw exception {"message": ~/error \\d/, "class": java.lang.UnsupportedOperationException}:\n' +
                '    exception.message:    actual string: error message\n' +
                '                       expected pattern: ~/error \\d/\n' +
                '    exception.class:  actual: java.lang.IllegalArgumentException <java.lang.Class>\n' +
                '                    expected: java.lang.UnsupportedOperationException <java.lang.Class>')) {
            code {
                throw new IllegalArgumentException('error message')
            } should throwException(UnsupportedOperationException, ~/error \d/)
        }
    }

    @Test
    void "should validate exception class and expected message"() {
        runExpectExceptionAndValidateOutput(AssertionError, contain('X failed expecting code to throw exception {"message": "error message1", "class": java.lang.UnsupportedOperationException}:\n' +
                '    exception.message:  actual: "error message" <java.lang.String>\n' +
                '                      expected: "error message1" <java.lang.String>\n' +
                '                                              ^\n' +
                '    exception.class:  actual: java.lang.IllegalArgumentException <java.lang.Class>\n' +
                '                    expected: java.lang.UnsupportedOperationException <java.lang.Class>')) {
            code {
                throw new IllegalArgumentException('error message')
            } should throwException(UnsupportedOperationException, 'error message1')
        }
    }

    @Test
    void "should add exception stack trace when mismatched"() {
        runExpectExceptionAndValidateOutput(AssertionError.class, contain("stack trace:\n" +
                "    java.lang.RuntimeException: java.lang.IllegalArgumentException: negative not allowed")) {
            code {
                businessLogicStart()
            } should throwException(NullPointerException, 'error message1')
        }
    }

    @Test
    void "should clearly report expected exception message when none is thrown"() {
        runExpectExceptionAndValidateOutput(AssertionError, '> expecting code to throw exception "negative numbers are not supported"\n' +
                'X failed expecting code to throw exception "negative numbers are not supported": no exception was thrown (Xms)') {
            code {
            } should throwException("negative numbers are not supported")
        }
    }

    @Test
    void examples() {
        code {
            businessLogic(-10)
        } should throwException(IllegalArgumentException, 'negative not allowed')

        code {
            businessLogic(-10)
        } should throwException(IllegalArgumentException, ~/negative.*not allowed/)

        code {
            businessLogic(-10)
        } should throwException(IllegalArgumentException)
    }

    @Test
    void "handle undeclared throwable exceptions"() {
        code { checkedExceptionThrowingProxy('catch me if you can') }
                .should(throwException(IOException, 'catch me if you can'))
    }

    private static void businessLogicStart() {
        try {
            businessLogic(-10)
        } catch (Throwable e) {
            throw new RuntimeException(e)
        }
    }

    private static void businessLogic(num) {
        if (num < 0) {
            throw new IllegalArgumentException('negative not allowed')
        }
    }

    private static checkedExceptionThrowingProxy(def message) {
        throw new UndeclaredThrowableException(new IOException(message))
    }
}
