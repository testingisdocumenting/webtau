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

import com.twosigma.webtau.expectation.equality.CompareToComparator
import org.junit.Test

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.createActualPath
import static com.twosigma.webtau.Ddjt.equal
import static com.twosigma.webtau.Ddjt.table

class IterableAndTableDataCompareToHandlerTest {
    @Test
    void "should compare list of beans and table data"() {
        def beans = [new SimpleBean(price: 2, lot: 2, symbol: "SA"),
                     new SimpleBean(price: 2, lot: 2, symbol: "SB")]

        actual(beans).should(equal(table("symbol", "price", "lot").values(
                                              "SA",      2,     2,
                                              "SB",      2,     2)))
    }

    @Test
    void "should report table mismatch"() {
        def beans = [new SimpleBean(price: 2, lot: 1, symbol: "SA"),
                     new SimpleBean(price: 1, lot: 2, symbol: "SB")]

        def expected = table("symbol", "price", "lot").values(
                "SA", 2, 2,
                "SB", 2, 2)

        def comparator = CompareToComparator.comparator()
        assert !comparator.compareIsEqual(createActualPath("beans"), beans, expected)

        def report = comparator.generateEqualMismatchReport()
        assert report.contains("lot:   actual: 1.0")
    }

    static class SimpleBean {
        double price
        double lot
        String symbol
    }
}
