/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.data.table.render;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import org.testingisdocumenting.webtau.data.table.Record;
import org.testingisdocumenting.webtau.data.render.DataRenderers;
import org.testingisdocumenting.webtau.data.table.TableData;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class TableRenderer {
    private final TableData data;
    private final TableCellDataRenderer tableCellDataRenderer;
    private final TableRenderStyle style;
    private final StringBuilder rendered;

    private TableData renderedCells;
    private Map<String, Integer> widthPerColumnName;
    private List<Integer> heightPerRowIdx;

    public static String render(TableData tableData) {
        return render(tableData, DataRenderers::render, new DefaultTableRenderStyle());
    }

    public static String render(TableData tableData, TableCellDataRenderer tableCellDataRenderer) {
        return render(tableData, tableCellDataRenderer, new DefaultTableRenderStyle());
    }

    public static String render(TableData tableData, TableRenderStyle renderStyle) {
        return render(tableData, DefaultTableCellDataRenderer.INSTANCE, renderStyle);
    }

    // TODO nested tables adjustments
    public static String render(TableData tableData, TableCellDataRenderer tableCellDataRenderer, TableRenderStyle renderStyle) {
        return new TableRenderer(tableData, tableCellDataRenderer, renderStyle).render();
    }

    private TableRenderer(TableData data, TableCellDataRenderer tableCellDataRenderer, TableRenderStyle style) {
        this.data = data;
        this.tableCellDataRenderer = tableCellDataRenderer;
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
        renderedCells.getHeader().getNamesStream().forEach((name) ->
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
        renderLine(style.headerMidLeft(), style.headerMidMid(), style.headerMidRight(), " ",
                null, (name) -> name);
    }

    private void renderHeaderLow() {
        String headerBotFill = style.headerBotFill();
        if (headerBotFill != null) {
            renderLine(style.headerBotLeft(), style.headerBotMid(), style.headerBotRight(), headerBotFill,
                    null, (name) -> "");
        }
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
                    (name) -> ((CellToRender) row.get(name)).getOriginalValue(),
                    (name) -> ((CellToRender) row.get(name)).getLine(finalLineIdx));
        }

        String bodyBotFill = style.bodyBotFill();
        if (bodyBotFill != null) {
            renderLine(style.bodyBotLeft(), style.bodyBotMid(), style.bodyBotRight(), bodyBotFill,
                    null, (name) -> "");
        }
    }

    private void renderLine(String left, String mid, String right, String fill,
                            Function<String, Object> originalValueForColumn,
                            Function<String, String> renderedValueForColumn) {
        if (widthPerColumnName.isEmpty()) {
            return;
        }

        rendered.append(left);

        rendered.append(
                widthPerColumnName.entrySet()
                        .stream()
                        .map(e ->
                        {
                            String rendered = renderedValueForColumn.apply(e.getKey());

                            Object original = originalValueForColumn != null ?
                                    originalValueForColumn.apply(e.getKey()):
                                    rendered;

                            String aligned = tableCellDataRenderer.align(original, rendered, e.getValue(), fill);

                            return originalValueForColumn != null ?
                                    tableCellDataRenderer.wrapBeforeRender(
                                            originalValueForColumn.apply(e.getKey()), aligned):
                                    aligned;
                        })
                        .collect(joining(mid)));

        rendered.append(right);
        rendered.append("\n");
    }

    private class CellToRender {
        private final Object originalValue;
        private List<String> lines;
        private int width;
        private int height;

        CellToRender(String columnName, Object originalValue) {
            this.originalValue = originalValue;
            splitLines(originalValue);
            calcMaxWidth(columnName, originalValue);
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

        private Object getOriginalValue() {
            return originalValue;
        }

        private void splitLines(Object originalValue) {
            String rendered = tableCellDataRenderer.renderCell(originalValue);
            lines = Arrays.asList(rendered.replace("\r", "").split("\n"));
        }

        private void calcMaxWidth(String columnName, Object originalValue) {
            width = lines
                    .stream()
                    .map(v -> tableCellDataRenderer.useDefaultWidth() ?
                            v.length() :
                            tableCellDataRenderer.valueWidth(originalValue))
                    .reduce(columnName.length(), (l, r) -> l > r ? l : r);
        }

        private void calcHeight() {
            height = lines.size();
        }
    }
}
