/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.utils

import org.junit.Assert
import org.junit.Test

class CsvUtilsTest {
    @Test
    void "parse csv with a header inside content"() {
        def csvData = CsvUtils.parse("""Account, "Description"
#12BGD3, "custom, table"
#12BGD3, chair
#91AGB1, lunch
""")

        assert csvData == [
                [Account: "#12BGD3", Description: "custom, table"],
                [Account: "#12BGD3", Description: "chair"],
                [Account: "#91AGB1", Description: "lunch"]]
    }

    @Test
    void "parse csv with passed header from outside"() {
        def csvData = CsvUtils.parse(['Account', 'Description'], """#12BGD3, "custom, table"
#12BGD3, chair
""")

        assert csvData == [
                [Account: "#12BGD3", Description: "custom, table"],
                [Account: "#12BGD3", Description: "chair"]]
    }

    @Test
    void "generates csv content from a list of columns and list of rows"() {
        def csv = CsvUtils.serialize(["colA", "colB", "colC"].stream(), [
                [1, "a", 3],
                [null, 4, "hello \"name\""],
        ].stream())

        Assert.assertEquals('colA,colB,colC\r\n' +
                '1,a,3\r\n' +
                ',4,"hello ""name"""\r\n', csv)
    }

    @Test
    void "generates csv content from a list of maps"() {
        def csv = CsvUtils.serialize([
                [colA: 1, colB: 'a', colC: 3],
                [colA: null, colB: 4, colC: 'hello "name"'],
        ])

        Assert.assertEquals('colA,colB,colC\r\n' +
                '1,a,3\r\n' +
                ',4,"hello ""name"""\r\n', csv)
    }

    @Test
    void "generates empty csv content from an empty list"() {
        def csv = CsvUtils.serialize([])
        Assert.assertEquals('', csv)
    }
}
