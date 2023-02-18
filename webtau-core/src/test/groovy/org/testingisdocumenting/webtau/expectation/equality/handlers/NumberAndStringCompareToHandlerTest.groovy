/*
 * Copyright 2021 webtau maintainers
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

import org.junit.Test
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class NumberAndStringCompareToHandlerTest {
    @Test
    void "handles string as actual and number as expected"() {
        def handler = new NumberAndStringCompareToHandler()
        assert handler.handleEquality("100", 43)
        assert handler.handleEquality("${100 + 10}", 43)
        assert !handler.handleEquality(100, 43)

        assert handler.handleGreaterLessEqual("100", 43)
        assert !handler.handleGreaterLessEqual(100, 43)
    }

    @Test
    void "automatically convert string to a number for equality comparison"() {
        actual("100.54").should(equal(100.54))
        actual("${100}.54").should(equal(100.54))
    }

    @Test
    void "automatically convert string to a number for greater-less comparison"() {
        actual("100.54").shouldBe(greaterThan(30))
        actual("${100}.54").should(equal(100.54))
    }

    @Test
    void "failed string and number equal comparison"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to equal 100.64:\n' +
                '      actual: 100.54 <java.math.BigDecimal> (before conversion: 100.54 <java.lang.Double>)\n' +
                '    expected: 100.64 <java.math.BigDecimal> (before conversion: 100.64 <java.math.BigDecimal>) (Xms)') {
            actual("100.54").should(equal(100.64))
        }
    }

    @Test
    void "non parsable string should not equal a give number"() {
        code {
            actual("abc").should(equal(100))
        } should throwException(AssertionError)
    }

    @Test
    void "non parsable string should not be greater or less a give number"() {
        code {
            actual("abc").shouldBe(greaterThan(100))
        } should throwException(AssertionError)

        code {
            actual("abc").shouldBe(lessThan(100))
        } should throwException(AssertionError)

        code {
            actual("abc").shouldBe(greaterThanOrEqual(100))
        } should throwException(AssertionError)

        code {
            actual("abc").shouldBe(lessThanOrEqual(100))
        } should throwException(AssertionError)
    }
}
