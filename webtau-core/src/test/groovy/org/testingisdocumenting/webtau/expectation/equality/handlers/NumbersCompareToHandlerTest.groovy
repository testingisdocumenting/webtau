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

package org.testingisdocumenting.webtau.expectation.equality.handlers

import org.junit.Test
import org.testingisdocumenting.webtau.data.ValuePath
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*

class NumbersCompareToHandlerTest {
    private static final ValuePath actualPath = createActualPath("value")

    @Test
    void "failed equality"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to equal 9:\n' +
                '      actual: 100.0 <java.math.BigDecimal> (before conversion: 100.0 <java.lang.Double>)\n' +
                '    expected: 9 <java.math.BigDecimal> (before conversion: 9 <java.lang.Long>) (Xms)') {
            actual(100d).should(equal(9L))
        }
    }

    @Test
    void "failed greater than"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to be greater than 9:\n' +
                '      actual: 8.0 <java.math.BigDecimal> (before conversion: 8.0 <java.lang.Double>)\n' +
                '    expected: greater than 9 <java.math.BigDecimal> (before conversion: 9 <java.lang.Long>) (Xms)') {
            actual(8d).shouldBe(greaterThan(9L))
        }
    }

    @Test
    void "failed less than"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to be less than 7:\n' +
                '      actual: 8.0 <java.math.BigDecimal> (before conversion: 8.0 <java.lang.Double>)\n' +
                '    expected: less than 7 <java.math.BigDecimal> (before conversion: 7 <java.lang.Long>) (Xms)') {
            actual(8d).shouldBe(lessThan(7L))
        }
    }

    @Test
    void "failed less than equal"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to be less than or equal to 7:\n' +
                '      actual: 8.0 <java.math.BigDecimal> (before conversion: 8.0 <java.lang.Double>)\n' +
                '    expected: less than or equal to 7 <java.math.BigDecimal> (before conversion: 7 <java.lang.Long>) (Xms)') {
            actual(8d).shouldBe(lessThanOrEqual(7L))
        }
    }

    @Test
    void "matches"() {
        actual(8d).shouldBe(lessThan(9L))
        actual(8d).shouldBe(greaterThan(7L))
        actual(8d).shouldBe(lessThanOrEqual(8L))
        actual(8d).shouldBe(greaterThanOrEqual(8L))
    }

    @Test
    void "compares numbers of different types by converting to largest type"() {
        def comparator = CompareToComparator.comparator()
        assert comparator.compareIsLess(actualPath, 8d, 9l)
        assert comparator.compareIsLessOrEqual(actualPath, 8l, 9i)
        assert comparator.compareIsLessOrEqual(actualPath, 8d, 8)
        assert comparator.compareIsGreater(actualPath, 9l, 8i)
        assert comparator.compareIsGreaterOrEqual(actualPath, 9l, 9i)

        assert !comparator.compareIsGreaterOrEqual(actualPath, 9l, 10i)

        assert comparator.compareIsEqual(actualPath, 8l, 8i)
        assert comparator.compareIsEqual(actualPath, 8.0, 8)
        assert comparator.compareIsEqual(actualPath, 8d, 8)

        assert !comparator.compareIsEqual(actualPath, 8d, 9d)
    }
}
