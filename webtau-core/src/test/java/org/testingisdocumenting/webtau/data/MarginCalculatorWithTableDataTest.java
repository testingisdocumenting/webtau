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

package org.testingisdocumenting.webtau.data;

import org.testingisdocumenting.webtau.data.table.TableData;
import org.junit.Test;

import java.util.List;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class MarginCalculatorWithTableDataTest {
    private MarginCalculator marginCalculator = new MarginCalculator();

    @Test
    public void marginShouldBeZeroIfNoLotsSet() {
        TableData transactionsData = table("symbol", "lot", "price").values(
                                             "SYM.B",  0.0,    8.0,
                                             "SYM.C",  0.0,    19.0);

        double margin = marginCalculator.calculate(createTransactions(transactionsData));
        assertEquals(0, margin, 0.0000001);
    }

    private static List<Transaction> createTransactions(TableData tableData) {
        return tableData.rowsStream().map(r -> {
            Transaction t = new Transaction();
            t.setSymbol(r.get("symbol"));
            t.setLot(r.get("lot"));
            t.setPrice(r.get("price"));

            return t;
        }).collect(toList());
    }
}
