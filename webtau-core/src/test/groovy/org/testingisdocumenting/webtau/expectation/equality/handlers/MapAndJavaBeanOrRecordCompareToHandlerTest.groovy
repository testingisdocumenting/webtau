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

import org.testingisdocumenting.webtau.WishLitItem
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator
import org.junit.Before
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.WebTauCore.properties
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class MapAndJavaBeanOrRecordCompareToHandlerTest {
    private CompareToComparator comparator

    @Before
    void init() {
        comparator = CompareToComparator.comparator()
    }

    @Test
    void "should only handle map as expected and bean as actual"() {
        def handler = new MapAndJavaBeanOrRecordCompareToHandler()
        assert !handler.handleEquality(10, 'test')
        assert !handler.handleEquality([k: 1], [k: 1])

        assert handler.handleEquality(new SmallBean(), [k2: 2])
    }

    @Test
    void "should only check explicitly specified java bean properties"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting bean to equal {"price": 120, "name": "n2"}:\n' +
                '    bean.price:  actual: 100 <java.math.BigDecimal> (before conversion: 100 <java.lang.Long>)\n' +
                '               expected: 120 <java.math.BigDecimal> (before conversion: 120 <java.lang.Integer>)\n' +
                '    bean.name:  actual: "n1" <java.lang.String>\n' +
                '              expected: "n2" <java.lang.String>\n' +
                '                          ^ (Xms)\n' +
                '  \n' +
                '  {"description": "d1", "name": **"n1"**, "price": **100**}') {
            actual(new SmallBean(), 'bean').should(equal([price: 120, name: 'n2']))
        }
    }

    @Test
    void "should only check explicitly specified record properties"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting record to equal {"description": "d1", "favorite": true}:\n' +
                '    record.favorite:  actual: false <java.lang.Boolean>\n' +
                '                    expected: true <java.lang.Boolean> (Xms)\n' +
                '  \n' +
                '  {"id": "id1", "description": "d1", "favorite": **false**, "related": []}') {
            actual(new WishLitItem("id1", "d1", false, []), 'record').should(equal([description: "d1", favorite: true]))
        }
    }

    @Test
    void "should handle object properties by taking top level props"() {
        runExpectExceptionAndValidateOutput(AssertionError, contain('expected: "n2"')) {
            actual(properties(new SmallBean()), 'properties').should(equal([price: 120, name: 'n2']))
        }
    }
}
