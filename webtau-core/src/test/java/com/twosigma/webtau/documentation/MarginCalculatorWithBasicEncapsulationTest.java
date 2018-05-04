package com.twosigma.webtau.documentation;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MarginCalculatorWithBasicEncapsulationTest {
    private MarginCalculator marginCalculator = new MarginCalculator();

    @Test
    public void marginShouldBeZeroIfNoLotsSet() {
        Transaction t1 = createTransaction("SYM.B", 0, 8);
        Transaction t2 = createTransaction("SYM.C", 0, 19);

        assertEquals(0, marginCalculator.calculate(Arrays.asList(t1, t2)),
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
