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

import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.expectation.equality.CompareToComparator.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*

class MapsCompareToHandlerTest {
    private CompareToComparator comparator

    @Before
    void init() {
        comparator = comparator(AssertionMode.EQUAL)
    }

    @Test
    void "should only handle maps on both sides"() {
        def handler = new MapsCompareToHandler()
        assert !handler.handleEquality(10, "test")
        assert !handler.handleEquality([k: 1], [])
        assert !handler.handleEquality([], [k: 1])

        assert handler.handleEquality([k1: 1], [k2: 2])
    }

    @Test
    void "should report missing keys on both sides in case of mismatch"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting map to equal {"k1": "v1", "k2": {"k22": "v21", "k24": "v24"}, "k3": "v3-"}:\n' +
                '    mismatches:\n' +
                '    \n' +
                '    map.k3:  actual: "v3" <java.lang.String>\n' +
                '           expected: "v3-" <java.lang.String>\n' +
                '                        ^\n' +
                '    \n' +
                '    missing, but expected values:\n' +
                '    \n' +
                '    map.k1: "v1"\n' +
                '    map.k2.k22: "v21"\n' +
                '    map.k2.k24: "v24"\n' +
                '    \n' +
                '    unexpected values:\n' +
                '    \n' +
                '    map.k2.k21: "v21"\n' +
                '    map.k2.k23: "v23"\n' +
                '    map.k6: "v1" (Xms)\n' +
                '  \n' +
                '  {"k6": **"v1"**, "k2": {"k21": **"v21"**, "k23": **"v23"**, "k22": <missing>, "k24": <missing>}, "k3": **"v3"**, "k1": <missing>}') {
            actual([k6: 'v1', k2: [k21: 'v21', k23: 'v23'], k3: 'v3'], 'map').should(
                    equal([k1: 'v1', k2: [k22: 'v21', k24: 'v24'], k3: 'v3-']))
        }
    }

    @Test
    void "should render actual when expects to be not equal and passes"() {
        runAndValidateOutput('. map doesn\'t equal {"k1": "v1"} (Xms)') {
            actual([[k1: 'v1']], 'map').shouldNot(equal([k1: 'v1']))
        }
    }
}
