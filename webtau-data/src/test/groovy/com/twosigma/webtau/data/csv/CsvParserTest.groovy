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

package com.twosigma.webtau.data.csv

import org.junit.Test

class CsvParserTest {
    @Test
    void "csv with a header inside content"() {
        def csvData = CsvParser.parse("""Account, "Description"
#12BGD3, "custom, table"
#12BGD3, chair
#91AGB1, lunch
""")

        csvData.should == [
                [Account: "#12BGD3", Description: "custom, table"],
                [Account: "#12BGD3", Description: "chair"],
                [Account: "#91AGB1", Description: "lunch"]]
    }

    @Test
    void "passed header from outside"() {
        def csvData = CsvParser.parse(['Account', 'Description'], """#12BGD3, "custom, table"
#12BGD3, chair
""")

        csvData.should == [
                [Account: "#12BGD3", Description: "custom, table"],
                [Account: "#12BGD3", Description: "chair"]]
    }

    @Test
    void "converts text to numbers when using auto conversion method"() {
        def csvData = CsvParser.parseWithAutoConversion(['Account', 'Price', 'Description'], """#12BGD3, 100, "custom, table"
#12BGD3, 150.5, chair
#12BGD3, 150 %, chair
#12BGD3, "150,000", chair
""")

        csvData.should == [
                [Account: "#12BGD3", Price: 100, Description: "custom, table"],
                [Account: "#12BGD3", Price: 150.5, Description: "chair"],
                [Account: "#12BGD3", Price: '150 %', Description: "chair"],
                [Account: "#12BGD3", Price: 150000, Description: "chair"]]
    }
}
