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


import org.junit.Test
import org.testingisdocumenting.webtau.data.ValuePath

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*

class ByteArrayCompareToHandlerTest {
    private static final ValuePath actualPath = createActualPath("value")

    @Test
    void "handles binary arrays"() {
        def handler = new ByteArrayCompareToHandler()

        def a = [2, 3, 4] as byte[]

        assert handler.handleEquality(a, a)
        assert !handler.handleEquality(a, 10)
        assert !handler.handleEquality(10, a)
    }

    @Test
    void "prints first bytes of array when fails"() {
        def a = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 29] as byte[]
        def b = [1, 2, 3, 4, 5, 6, 7, 9, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 29] as byte[]

        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to equal binary content of size 20:\n' +
                '    binary content first difference idx: 7\n' +
                '      actual: ...08090A0B0C0D0E0F101112131D\n' +
                '    expected: ...09090A0B0C0D0E0F101112131D (Xms)\n' +
                '  \n' +
                '  **binary content of size 20**') {
            actual(a).should(equal(b))
        }
    }

    @Test
    void "prints sizes mismatch and first index mismatch"() {
        def a = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22] as byte[]
        def b = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 18, 19, 20, 21, 22, 23] as byte[]

        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to equal binary content of size 23:\n' +
                '    binary content has different size:\n' +
                '      actual: 22\n' +
                '    expected: 23\n' +
                '    binary content first difference idx: 16\n' +
                '      actual: ...111213141516\n' +
                '    expected: ...12121314151617 (Xms)\n' +
                '  \n' +
                '  **binary content of size 22**') {
            actual(a).should(equal(b))
        }
    }
}
