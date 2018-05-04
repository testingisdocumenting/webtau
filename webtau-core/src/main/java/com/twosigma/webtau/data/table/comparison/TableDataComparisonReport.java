package com.twosigma.webtau.data.table.comparison;

import com.twosigma.webtau.data.table.render.TableRenderer;

import static java.util.stream.Collectors.joining;

public class TableDataComparisonReport {
    private final TableDataComparisonResult result;

    public TableDataComparisonReport(TableDataComparisonResult result) {
        this.result = result;
    }

    public String generate() {
        return missingColumnsReport() +
            "\nactual:\n\n" +
            TableRenderer.render(result.getActual().map(this::mapActualBreaks)) +
            "\nexpected:\n\n" +
            TableRenderer.render(result.getExpected().map(this::mapExpectedBreaks)) +
            missingRowsReport() +
            extraRowsReport();
    }

    private String extraRowsReport() {
        if (result.getExtraRows().isEmpty())
            return "";

        return "\nextra rows:\n" + TableRenderer.render(result.getExtraRows());
    }

    private String missingRowsReport() {
        if (result.getMissingRows().isEmpty())
            return "";

        return "\nmissing rows:\n" + TableRenderer.render(result.getMissingRows());
    }

    private String missingColumnsReport() {
        return result.getMissingColumns().isEmpty() ? "" : "missing columns: " + result.getMissingColumns().stream().collect(joining(", "));
    }

    private Object mapActualBreaks(int rowIdx,  int colIdx,  String columnName,  Object value) {
        return annotateCellBreak(value, result.getActualMismatch(rowIdx, columnName));
    }

    private Object mapExpectedBreaks(int rowIdx,  int colIdx,  String columnName,  Object value) {
        return annotateCellBreak(value, result.getExpectedMismatch(rowIdx, columnName));
    }

    private Object annotateCellBreak( Object value,  String mismatch) {
        return mismatch == null ? value :
            "***\n" + mismatch + "\n***\n\n" + value;
    }
}
