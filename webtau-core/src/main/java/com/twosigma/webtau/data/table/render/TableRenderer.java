package com.twosigma.webtau.data.table.render;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.twosigma.webtau.data.table.Record;
import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.data.table.TableData;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class TableRenderer {
    private TableData data;
    private TableRenderStyle style;
    private StringBuilder rendered;

    private TableData renderedCells;
    private Map<String, Integer> widthPerColumnName;
    private List<Integer> heightPerRowIdx;

    public static String render(TableData tableData) {
        return render(tableData, new DefaultTableRenderStyle());
    }

    // TODO nested tables adjustments
    public static String render(TableData tableData, TableRenderStyle renderStyle) {
        return new TableRenderer(tableData, renderStyle).render();
    }

    private TableRenderer(TableData data, TableRenderStyle style) {
        this.data = data;
        this.style = style;
        this.rendered = new StringBuilder();
    }

    private String render() {
        preRenderValues();
        calculateSizes();

        renderHeader();
        renderBody();

        return rendered.toString();
    }

    private void preRenderValues() {
        renderedCells = data.map((rowIdx, colIx, columnName, value) -> new CellToRender(columnName, value));
    }

    private void calculateSizes() {
        calculateWidths();
        calculateHeights();
    }

    private void calculateWidths() {
        widthPerColumnName = new LinkedHashMap<>();
        renderedCells.getHeader().getNames().forEach((name) ->
            widthPerColumnName.put(name, renderedCells.mapColumn(name, CellToRender::getWidth).max(Integer::compareTo).orElse(0))
        );
    }

    private void calculateHeights() {
        heightPerRowIdx = renderedCells.rowsStream().
            map(r -> r.mapValues(CellToRender::getHeight).max(Integer::compareTo).orElse(0)).
            collect(toList());
    }

    private void renderHeader() {
        renderHeaderMid();
        renderHeaderLow();
    }

    private void renderHeaderMid() {
        renderLine(style.headerMidLeft(), style.headerMidMid(), style.headerMidRight(), " ", (name) -> name);
    }

    private void renderHeaderLow() {
        renderLine(style.headerBotLeft(), style.headerBotMid(), style.headerBotRight(), style.headerBotFill(), (name) -> "");
    }

    private void renderBody() {
        int rowIdx = 0;
        for (Record row : renderedCells) {
            renderRow(row, rowIdx);
            rowIdx++;
        }
    }

    private void renderRow(Record row, int rowIdx) {
        int rowHeight = heightPerRowIdx.get(rowIdx);

        // cell value can be spread across multiple lines
        for (int lineIdx = 0; lineIdx < rowHeight; lineIdx++) {
            int finalLineIdx = lineIdx;
            renderLine(style.bodyMidLeft(), style.bodyMidMid(), style.bodyMidRight(), " ",
                (name) -> ((CellToRender) row.get(name)).getLine(finalLineIdx));
        }

        renderLine(style.bodyBotLeft(), style.bodyBotMid(), style.bodyBotRight(), style.bodyBotFill(), (name) -> "");
    }

    private void renderLine(String left, String mid, String right, String fill, Function<String, String> valueForColumn) {
        if (widthPerColumnName.isEmpty())
            return;

        rendered.append(left);

        rendered.append(widthPerColumnName.entrySet().stream().
            map(e -> StringUtils.rightPad(valueForColumn.apply(e.getKey()), e.getValue(), fill)).
            collect(joining(mid)));

        rendered.append(right);
        rendered.append("\n");
    }

    private static class CellToRender {
        private List<String> lines;
        private int width;
        private int height;

        CellToRender(String columnName, Object originalValue) {
            splitLines(originalValue);
            calcMaxWidth(columnName);
            calcHeight();
        }

        private int getWidth() {
            return width;
        }

        private int getHeight() {
            return height;
        }

        private String getLine(int lineIdx) {
            return lineIdx < lines.size() ? lines.get(lineIdx) : "";
        }

        private void splitLines(Object originalValue) {
            String rendered = DataRenderers.render(originalValue);
            lines = Arrays.asList(rendered.replace("\r", "").split("\n"));
        }

        private void calcMaxWidth(String columnName) {
            width = lines.stream().map(String::length).reduce(columnName.length(), (l, r) -> l > r ? l : r);
        }

        private void calcHeight() {
            height = lines.size();
        }
    }
}


