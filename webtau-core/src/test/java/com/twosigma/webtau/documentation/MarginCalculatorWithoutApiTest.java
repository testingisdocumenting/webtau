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

package com.twosigma.webtau.documentation;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MarginCalculatorWithoutApiTest {
    private MarginCalculator marginCalculator = new MarginCalculator();

    @Test
    public void marginShouldBeZeroIfNoLotsSet() {
        Transaction t1 = new Transaction();
        t1.setSymbol("SYM.B");
        t1.setLot(0);
        t1.setPrice(8);

        Transaction t2 = new Transaction();
        t1.setSymbol("SYM.C");
        t1.setLot(0);
        t1.setPrice(19);

        assertEquals(0, marginCalculator.calculate(Arrays.asList(t1, t2)),
                0.0000001);
    }
}
