package com.twosigma.webtau.documentation;

import com.twosigma.webtau.data.table.TableData;
import org.junit.Test;

import java.util.List;

import static com.twosigma.webtau.Ddjt.*;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class MarginCalculatorWithTableDataTest {
    private MarginCalculator marginCalculator = new MarginCalculator();

    @Test
    public void marginShouldBeZeroIfNoLotsSet() {
        TableData transactionsData = header("symbol", "lot", "price").values(
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
