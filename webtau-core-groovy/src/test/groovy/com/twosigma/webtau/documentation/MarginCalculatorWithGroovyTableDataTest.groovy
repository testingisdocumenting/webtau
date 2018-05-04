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

package com.twosigma.webtau.documentation

import com.twosigma.webtau.data.table.TableData
import org.junit.Test

class MarginCalculatorWithGroovyTableDataTest {
    private MarginCalculator marginCalculator = new MarginCalculator()

    @Test
    void "margin should be zero if no lots set"() {
        def transactionsData = ["symbol" | "lot" | "price"] {
                                 ___________________________
                                 "SYM.B" |     0 |     8
                                 "SYM.C" |     0 |    19 }

        def margin = marginCalculator.calculate(createTransactions(transactionsData))
        margin.should == 0
    }

    private static List<Transaction> createTransactions(TableData tableData) {
        tableData.collect {
            new Transaction(symbol: it.symbol, lot: it.lot, price: it.price)
        }
    }
}
