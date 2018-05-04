/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

import com.twosigma.webtau.expectation.equality.EqualComparator
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler
import org.junit.Before
import org.junit.Test

import static com.twosigma.webtau.Ddjt.createActualPath
import static org.junit.Assert.assertEquals

class MapsEqualHandlerTest {
    private EqualComparator equalComparator

    @Before
    void init() {
        equalComparator = EqualComparator.comparator()
    }

    @Test
    void "should only handle maps on both sides"() {
        EqualComparatorHandler handler = new MapsEqualHandler()
        assert ! handler.handle(10, "test")
        assert ! handler.handle([k: 1], [])
        assert ! handler.handle([], [k: 1])

        assert handler.handle([k1: 1], [k2: 2])
    }

    @Test
    void "should report missing keys on both sides"() {
        equalComparator.compare(createActualPath("map"),
                [k6: 'v1', k2: [k21: 'v21', k23: "v23"], k3: 'v3'],
                [k1: 'v1', k2: [k22: 'v21', k24: "v24"], k3: 'v3-'])

        def report = equalComparator.generateMismatchReport()
        assertEquals("mismatches:\n" +
                "\n" +
                "map.k3:   actual: v3 <java.lang.String>\n" +
                "        expected: v3- <java.lang.String>\n" +
                "\n" +
                "missing, but expected values:\n" +
                "\n" +
                "map.k1: v1\n" +
                "map.k2.k22: v21\n" +
                "map.k2.k24: v24\n" +
                "\n" +
                "unexpected values:\n" +
                "\n" +
                "map.k2.k21: v21\n" +
                "map.k2.k23: v23\n" +
                "map.k6: v1", report)
    }
}
