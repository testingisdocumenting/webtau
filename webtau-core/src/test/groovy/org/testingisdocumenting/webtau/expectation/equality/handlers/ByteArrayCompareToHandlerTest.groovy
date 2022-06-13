/*
 * Copyright 2022 webtau maintainers
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

import org.testingisdocumenting.webtau.expectation.ActualPath
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator
import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.createActualPath
import static org.junit.Assert.assertEquals

class ByteArrayCompareToHandlerTest {
    private static final ActualPath actualPath = createActualPath("value")

    @Test
    void "handles binary arrays"() {
        def handler = new ByteArrayCompareToHandler()

        def a = [2, 3, 4] as byte[]

        assert handler.handleEquality(a, a)
        assert !handler.handleEquality(a, 10)
        assert !handler.handleEquality(10, a)
    }

    @Test
    void "prints first bytes of array as hex when matched and ellipsis to show there is more"() {
        def a = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 29] as byte[]

        def comparator = CompareToComparator.comparator()
        assert comparator.compareIsEqual(actualPath, a, a)

        assertEquals(
            'value: binary content of size 20\n' +
            '         actual: 0102030405060708090A0B0C0D0E0F10...\n' +
            '       expected: 0102030405060708090A0B0C0D0E0F10...', comparator.generateEqualMatchReport())
    }

    @Test
    void "prints first bytes of array as hex without ellipsis if it fits"() {
        def a = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16] as byte[]

        def comparator = CompareToComparator.comparator()
        assert comparator.compareIsEqual(actualPath, a, a)

        assertEquals(
            'value: binary content of size 16\n' +
            '         actual: 0102030405060708090A0B0C0D0E0F10\n' +
            '       expected: 0102030405060708090A0B0C0D0E0F10', comparator.generateEqualMatchReport())
    }

    @Test
    void "prints sizes mismatch and first index mismatch"() {
        def a = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22] as byte[]
        def b = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 18, 19, 20, 21, 22, 23] as byte[]

        def comparator = CompareToComparator.comparator()
        assert !comparator.compareIsEqual(actualPath, a, b)

        assertEquals('mismatches:\n' +
            '\n' +
            'value: binary content has different size:\n' +
            '         actual: 22\n' +
            '       expected: 23\n' +
            'value: binary content first difference idx: 16\n' +
            '         actual: ...111213141516\n' +
            '       expected: ...12121314151617', comparator.generateEqualMismatchReport())
    }
}
