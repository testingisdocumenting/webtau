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

package com.twosigma.webtau.expectation.equality.handlers

import com.twosigma.webtau.expectation.ActualPath
import com.twosigma.webtau.expectation.equality.CompareToComparator
import org.junit.Test

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.beGreaterThanOrEqual
import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.createActualPath
import static com.twosigma.webtau.Ddjt.equal
import static com.twosigma.webtau.Ddjt.throwException
import static org.junit.Assert.assertEquals

class NumbersCompareToHandlerTest {
    private static final ActualPath actualPath = createActualPath("value")

    @Test
    void "handles numbers of different types only for equality"() {
        assertHandlesTypes('handleEquality')
    }

    @Test
    void "handles numbers of different types only for greater less"() {
        assertHandlesTypes('handleGreaterLessEqual')
    }

    @Test
    void "report contains value before conversion when fails for equality"() {
        def comparator = CompareToComparator.comparator()
        assert !comparator.compareIsEqual(actualPath, 10d, 9l)

        assertEquals(
            "mismatches:\n\n" +
            "value:   actual: 10.0 <java.math.BigDecimal>(before conversion: 10.0 <java.lang.Double>)\n" +
            "       expected: 9 <java.math.BigDecimal>(before conversion: 9 <java.lang.Long>)",
            comparator.generateEqualMismatchReport())
    }

    @Test
    void "report contains value before conversion when fails for greater-less"() {
        def comparator = CompareToComparator.comparator()
        assert !comparator.compareIsLess(actualPath, 10d, 9l)

        assertEquals(
            "mismatches:\n\n" +
            "value:   actual: 10.0 <java.math.BigDecimal>(before conversion: 10.0 <java.lang.Double>)\n" +
            "       expected: less than 9 <java.math.BigDecimal>(before conversion: 9 <java.lang.Long>)",
            comparator.generateLessThanMismatchReport())
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

    @Test
    void "handler is linked"() {
        actual(10.0).should(equal(10))
        actual(10.0).should(beGreaterThanOrEqual(8l))

        code {
            actual(10.0).should(equal(9))
        } should throwException(AssertionError, ~/expected: 9/)

        code {
            actual(10.0).should(beGreaterThanOrEqual(11))
        } should throwException(AssertionError, ~/expected: greater than or equal to 11/)
    }

    private static void assertHandlesTypes(handleMethodName) {
        def handler = new NumbersCompareToHandler()
        assert handler."$handleMethodName"(2l, 1i)
        assert handler."$handleMethodName"(2l, 1d)
        assert handler."$handleMethodName"(2i, 1d)

        assert !handler."$handleMethodName"(2i, 2i)
        assert !handler."$handleMethodName"(2d, 2d)
    }
}
