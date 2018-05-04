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
import org.junit.Before
import org.junit.Test

import static com.twosigma.webtau.Ddjt.*
import static org.junit.Assert.assertEquals


class MapAndBeanEqualHandlerTest {
    private EqualComparator equalComparator

    @Before
    void init() {
        equalComparator = EqualComparator.comparator()
    }

    @Test
    void "should only handle map as expected and bean as actual"() {
        def handler = new MapAndBeanEqualHandler()
        assert ! handler.handle(10, "test")
        assert ! handler.handle([k: 1], [k: 1])

        assert handler.handle(new SmallBean(), [k2: 2])
    }

    @Test
    void "should only check explicitly specified properties"() {
        def result = equalComparator.compare(createActualPath("bean"),
                new SmallBean(), [price: 120, name: "n2"])

        def report = equalComparator.generateMismatchReport()
        assertEquals("mismatches:\n" +
                "\n" +
                "bean.price:   actual: 100 <java.math.BigDecimal>(before conversion: 100 <java.lang.Long>)\n" +
                "            expected: 120 <java.math.BigDecimal>(before conversion: 120 <java.lang.Integer>)\n" +
                "bean.name:   actual: n1 <java.lang.String>\n" +
                "           expected: n2 <java.lang.String>", report)

    }

    class SmallBean {
        long getPrice() {
            return 100;
        }

        void setPrice(long price) {
        }

        String getName() {
            return "n1";
        }

        void setName(String name) {
        }

        String getDescription() {
            return "d1";
        }

        void setDescription(String description) {
        }
    }

}
