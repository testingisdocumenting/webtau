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

import static com.twosigma.webtau.WebTauCore.*
import static com.twosigma.webtau.expectation.equality.CompareToComparator.AssertionMode.*
import static org.junit.Assert.assertEquals

class SetCompareToHandlerTest {
    private static final ActualPath actualPath = createActualPath("value")

    @Test
    void "handles sets"() {
        def handler = new SetCompareToHandler()
        assert handler.handleEquality([] as Set, [] as Set)
        assert !handler.handleEquality([], [] as Set)
    }

    @Test
    void "order should not matter"() {
        actual([1, 2] as Set).should(equal([2, 1] as Set))
    }

    @Test
    void "should report missing and extra elements in equal mode"() {
        CompareToComparator comparator = CompareToComparator.comparator(EQUAL)
        comparator.compareUsingEqualOnly(actualPath,
                ["hello", "world"] as Set,
                [~/worl./, ~/.ello1/] as Set)

        assertEquals('missing, but expected values:\n' +
                '\n' +
                'value: pattern /.ello1/\n' +
                '\n' +
                'unexpected values:\n' +
                '\n' +
                'value: "hello"', comparator.generateEqualMismatchReport())

        println comparator.generateEqualMatchReport()
    }

    @Test
    void "should report matched elements in not equal mode"() {
        CompareToComparator comparator = CompareToComparator.comparator(NOT_EQUAL)
        comparator.compareUsingEqualOnly(actualPath,
                ["hello", "world"] as Set,
                [~/worl./, ~/.ello1/] as Set)

        assertEquals('mismatches:\n' +
                '\n' +
                'value[1]:   actual: "world" <java.lang.String>\n' +
                '          expected: not pattern /worl./ <java.util.regex.Pattern>',
                comparator.generateNotEqualMismatchReport())
    }

    @Test
    void "should not report missing elements in not equal mode"() {
        CompareToComparator comparator = CompareToComparator.comparator(NOT_EQUAL)
        comparator.compareUsingEqualOnly(actualPath,
                ["hello"] as Set,
                [~/.ello1/] as Set)

        assertEquals('', comparator.generateNotEqualMismatchReport())
    }
}
