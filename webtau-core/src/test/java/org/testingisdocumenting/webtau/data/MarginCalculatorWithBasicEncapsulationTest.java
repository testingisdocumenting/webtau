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

package org.testingisdocumenting.webtau.data;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.testingisdocumenting.webtau.WebTauCore.*;

public class MarginCalculatorWithBasicEncapsulationTest {
    private final MarginCalculator marginCalculator = new MarginCalculator();

    @Test
    public void marginShouldBeZeroIfNoLotsSet() {
        Transaction t1 = createTransaction("SYM.B", 0, 8);
        Transaction t2 = createTransaction("SYM.C", 0, 19);

        assertEquals(0, marginCalculator.calculate(listOf(t1, t2)),
                0.0000001);
    }

    private static Transaction createTransaction(String symbol, double lot, double price) {
        Transaction t = new Transaction();
        t.setSymbol(symbol);
        t.setLot(lot);
        t.setPrice(price);

        return t;
    }
}
