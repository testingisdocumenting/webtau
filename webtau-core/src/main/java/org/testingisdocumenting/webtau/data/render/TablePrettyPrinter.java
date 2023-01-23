/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.data.render;

import org.apache.commons.lang3.StringUtils;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.table.Record;
import org.testingisdocumenting.webtau.data.table.TableData;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class TablePrettyPrinter {
    private static final Color COLUMN_NAME_COLOR = Color.PURPLE;
    private static final String COLUMNS_DELIMITER = " │ ";

    private final TableData tableData;

    private final List<Integer> columnWidthByIdx;
    private final List<Integer> rowHeightByIdx;
    private TableData prettyPrintersTable;

    public TablePrettyPrinter(TableData tableData) {
        this.tableData = tableData;
        this.columnWidthByIdx = tableData.getHeader().getNamesStream()
                .map(String::length)
                .collect(Collectors.toList());
        this.rowHeightByIdx = tableData.rowsStream()
                .map(record -> 1)
                .collect(Collectors.toList());
    }

    public void prettyPrint(PrettyPrinter printer) {
        prettyPrintersTable = createPrettyPrintersTable(printer, tableData);
        calcColumnWidthsAndHeights();

        renderHeader(printer);
        renderBody(printer);
    }

    private void renderHeader(PrettyPrinter printer) {
        Iterator<String> namesIt = tableData.getHeader().getNamesStream().iterator();
        int columnIdx = 0;
        while (namesIt.hasNext()) {
            String name = namesIt.next();
            int columnWidth = columnWidthByIdx.get(columnIdx);
            boolean isLastColumn = columnIdx == tableData.getHeader().size() - 1;


            printer.print(COLUMN_NAME_COLOR, StringUtils.rightPad(name,columnWidth, ' '));
            if (!isLastColumn) {
                printer.print(PrettyPrinter.DELIMITER_COLOR, COLUMNS_DELIMITER);
            }

            columnIdx++;
        }

        printer.flushCurrentLine();
    }

    private void renderBody(PrettyPrinter printer) {
        for (int rowIdx = 0; rowIdx < prettyPrintersTable.numberOfRows(); rowIdx++) {
            Record row = prettyPrintersTable.row(rowIdx);
            renderRow(printer, row, rowIdx);
        }

    }

    private void renderRow(PrettyPrinter printer, Record row, int rowIdx) {
        Integer rowHeight = rowHeightByIdx.get(rowIdx);
        for (int rowLineIdx = 0; rowLineIdx < rowHeight; rowLineIdx++) {
            int columnIdx = 0;
            for (Object cellValue : row.getValues()) {
                PrettyPrinter cellPrettyPrinter = (PrettyPrinter) cellValue;
                boolean isLastColumn = columnIdx == tableData.getHeader().size() - 1;

                Integer columnWidth = columnWidthByIdx.get(columnIdx);

                if (rowLineIdx < cellPrettyPrinter.getNumberOfLines()) {
                    PrettyPrinterLine line = cellPrettyPrinter.getLine(rowLineIdx);
                    printer.print(line.getStyleAndValues().toArray());
                    Integer contentWidth = line.getWidth();
                    printer.print(StringUtils.rightPad("", columnWidth - contentWidth, " "));
                } else {
                    printer.print(StringUtils.rightPad("", columnWidth, " "));
                }

                if (!isLastColumn) {
                    printer.print(PrettyPrinter.DELIMITER_COLOR, COLUMNS_DELIMITER);
                }

                columnIdx++;
            }
            printer.flushCurrentLine();
        }
    }

    private void calcColumnWidthsAndHeights() {
        int rowIdx = 0;
        Iterator<Record> rowIt = prettyPrintersTable.rowsStream().iterator();
        while (rowIt.hasNext()) {
            Record record = rowIt.next();

            int columnIdx = 0;
            for (Object cell : record.getValues()) {
                PrettyPrinter prettyPrinter = (PrettyPrinter) cell;
                columnWidthByIdx.set(columnIdx, Math.max(columnWidthByIdx.get(columnIdx), prettyPrinter.calcMaxWidth()));
                rowHeightByIdx.set(rowIdx, Math.max(rowHeightByIdx.get(rowIdx), prettyPrinter.getNumberOfLines()));

                columnIdx++;
            }

            rowIdx++;
        }
    }

    private TableData createPrettyPrintersTable(PrettyPrinter prettyPrinter, TableData tableData) {
        return tableData.map(((rowIdx, colIdx, columnName, v) -> {
            PrettyPrinter cellPrettyPrinter = new PrettyPrinter(prettyPrinter.getConsoleOutput(), 0);
            cellPrettyPrinter.printObject(v);
            cellPrettyPrinter.flushCurrentLine();

            return cellPrettyPrinter;
        }));
    }
}

/*

col1      │ col2                 │ col 3
"hello"   │ ┌ {                ┐ │ "world"
          │     "key": "value",  │
          │     "amount": 100    │
          │ └ }                ┘ │
100       │ 200                  │ "test"


┌ {                ┐
    "key": "value",
    "amount": 100
└ }                ┘

┌  ┐

└  ┘

 */
