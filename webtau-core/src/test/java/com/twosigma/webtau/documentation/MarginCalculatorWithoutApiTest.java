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
