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

package org.testingisdocumenting.webtau.data.table;

import org.testingisdocumenting.webtau.data.render.TableDataRenderer;
import org.testingisdocumenting.webtau.data.table.header.CompositeKey;
import org.testingisdocumenting.webtau.data.table.header.TableDataHeader;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Represents a set of rows with named columns to be used as part of test input preparation and/or test output validation
 */
public class TableData implements Iterable<Record> {
    private final List<Record> rows;
    private final Map<CompositeKey, Record> rowsByKey;
    private final Map<CompositeKey, Integer> rowIdxByKey;
    private final TableDataHeader header;

    public TableData(List<?> columnNamesAndOptionalValues) {
        this(new TableDataHeader(extractColumnNames(columnNamesAndOptionalValues.stream()).stream()));
        populateValues(columnNamesAndOptionalValues.stream());
    }

    public TableData(Stream<?> columnNamesAndOptionalValues) {
        this(columnNamesAndOptionalValues.collect(toList()));
    }

    public TableData(TableDataHeader header) {
        this.header = header;
        this.rows = new ArrayList<>();
        this.rowsByKey = new HashMap<>();
        this.rowIdxByKey = new HashMap<>();
    }

    public TableDataHeader getHeader() {
        return header;
    }

    public boolean isEmpty() {
        return rows.isEmpty();
    }

    public Set<CompositeKey> keySet() {
        return rowsByKey.keySet();
    }

    public Integer findRowIdxByKey(CompositeKey key) {
        return rowIdxByKey.get(key);
    }

    /**
     * create new table data with the data of a current one but with new key columns.
     * can be used to validate new key columns uniqueness
     * @param keyColumns new key columns
     * @return new table data with updated key columns
     */
    public TableData withNewKeyColumns(String... keyColumns) {
        TableDataHeader newHeader = new TableDataHeader(header.getNamesStream(), Arrays.stream(keyColumns));
        TableData withNewHeader = new TableData(newHeader);

        for (Record originalRow : rows) {
            withNewHeader.addRow(newHeader.createRecord(originalRow.valuesStream()));
        }

        return withNewHeader;
    }

    /**
     * @param values row values combined in one vararg
     * @return populate table data instance
     */
    public TableData values(Object... values) {
        int numberOfRows = header.size() == 0 ? 0 : values.length / header.size();
        int numberOfExtraValues = header.size() == 0 ? 0 : values.length % header.size();

        if (numberOfExtraValues != 0) {
            int startIdxOfExtraValues = numberOfRows * header.size();
            throw new IllegalArgumentException("unfinished row idx " + numberOfRows + ", header:  " + header + "\nvalues so far: " +
                    Arrays.stream(values).skip(startIdxOfExtraValues).map(Object::toString).
                            collect(joining(", ")));
        }

        int total = numberOfRows * header.size();
        for (int i = 0; i < total; i += header.size()) {
            addRow(Arrays.stream(values).skip(i).limit(header.size()));
        }

        return this;
    }

    public Record row(int rowIdx) {
        validateRowIdx(rowIdx);
        return rows.get(rowIdx);
    }

    public Record find(CompositeKey key) {
        return rowsByKey.get(key);
    }

    public void addRow(List<Object> values) {
        addRow(values.stream());
    }

    public void addRow(Stream<Object> values) {
        Record record = new Record(header, values);

        if (record.hasMultiValues()) {
            record.unwrapMultiValues().forEach(this::addRow);
        } else {
            addRow(record);
        }
    }

    public void addRow(Record record) {
        if (header != record.getHeader()) {
            throw new RuntimeException("incompatible headers. current getHeader: " + header + ", new record one: " + record.getHeader());
        }

        int rowIdx = rows.size();
        CompositeKey key = getOrBuildKey(rowIdx, record);

        Record existing = rowsByKey.put(key, record);
        if (existing != null) {
            throw new IllegalArgumentException("duplicate entry found with key: " + key +
                    "\n" + existing +
                    "\n" + record);
        }

        Record previous = rows.isEmpty() ? null : rows.get(rows.size() - 1);
        Record withEvaluatedGenerators = record.evaluateValueGenerators(previous, rows.size());

        rowIdxByKey.put(key, rowIdx);
        rows.add(withEvaluatedGenerators);
    }

    public <T, R> TableData map(TableDataCellMapFunction<T, R> mapper) {
        TableData mapped = new TableData(header);

        int rowIdx = 0;
        for (Record originalRow : rows) {
            mapped.addRow(mapRow(rowIdx, originalRow, mapper));
            rowIdx++;
        }

        return mapped;
    }

    public TableData replace(Object before, Object after) {
        return map(((rowIdx, colIdx, columnName, v) -> v.equals(before) ? after : v));
    }

    public <T, R> Stream<R> mapColumn(String columnName, Function<T, R> mapper) {
        int idx = header.columnIdxByName(columnName);
        return rows.stream().map(r -> mapper.apply(r.get(idx)));
    }

    private <T, R> Stream<Object> mapRow(int rowIdx, Record originalRow, TableDataCellMapFunction<T, R> mapper) {
        return header.getColumnIdxStream()
                .mapToObj(idx -> mapper.apply(rowIdx, idx, header.columnNameByIdx(idx), originalRow.get(idx)));
    }

    public Stream<Record> rowsStream() {
        return rows.stream();
    }

    public List<Map<String, ?>> toListOfMaps() {
        return rows.stream().map(Record::toMap).collect(toList());
    }

    public String toJson() {
        return JsonUtils.serializePrettyPrint(toListOfMaps());
    }

    @Override
    public Iterator<Record> iterator() {
        return rows.iterator();
    }

    public int numberOfRows() {
        return rows.size();
    }

    private void validateRowIdx(int rowIdx) {
        if (rowIdx < 0 || rowIdx >= numberOfRows())
            throw new IllegalArgumentException("rowIdx is out of range: [0, " + (numberOfRows() - 1) + "]");
    }

    private CompositeKey getOrBuildKey(int rowIdx, Record row) {
        if (header.hasKeyColumns()) {
            return row.getKey();
        }

        return new CompositeKey(Stream.of(rowIdx));
    }

    private void populateValues(Stream<?> columnNameAndValues) {
        values(columnNameAndValues.skip(header.size() + 1).toArray());
    }

    @Override
    public String toString() {
        return TableDataRenderer.renderTable(this);
    }

    private static List<String> extractColumnNames(Stream<?> columnNameAndValues) {
        List<String> result = new ArrayList<>();

        Iterator<?> iterator = columnNameAndValues.iterator();
        while (iterator.hasNext()) {
            Object nameOrValue = iterator.next();
            if (nameOrValue instanceof TableDataUnderscore) {
                break;
            }

            result.add(nameOrValue.toString());
        }

        return result;
    }
}
