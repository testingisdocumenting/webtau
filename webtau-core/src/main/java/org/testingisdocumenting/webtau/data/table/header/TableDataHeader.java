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

package org.testingisdocumenting.webtau.data.table.header;

import org.testingisdocumenting.webtau.data.table.Record;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TableDataHeader {
    private final Map<String, Integer> indexByName = new HashMap<>();
    private final List<String> namesByIndex = new ArrayList<>();

    private final List<Integer> keyIdx = new ArrayList<>();
    private final List<String> keyNames;

    public TableDataHeader(Stream<String> names) {
        this.keyNames = new ArrayList<>();
        names.forEach(this::add);
    }

    public TableDataHeader(Stream<String> names, Stream<String> keyNames) {
        this.keyNames = new ArrayList<>();
        Set<String> keyNamesAsSet = keyNames.collect(Collectors.toSet());
        names.forEach(name -> add(name, keyNamesAsSet.contains(name)));
    }

    public Record createRecord(Stream<Object> values) {
        return new Record(this, values);
    }

    public CompositeKey createKey(Record record) {
        return new CompositeKey(getKeyNamesStream().map(record::get));
    }

    public boolean has(String name) {
        return indexByName.containsKey(name);
    }

    public Stream<String> getNamesStream() {
        return namesByIndex.stream();
    }

    public boolean hasKeyColumns() {
        return ! keyNames.isEmpty();
    }

    public Stream<String> getKeyNamesStream() {
        return keyNames.stream();
    }

    public Stream<Integer> getKeyIdxStream() {
        return keyIdx.stream();
    }

    public IntStream getColumnIdxStream() {
        return IntStream.range(0, namesByIndex.size());
    }

    public boolean hasColumn(String columnName) {
        return namesByIndex.contains(columnName);
    }

    private void add(String nameWithMeta) {
        boolean startsWithAsterisk = nameWithMeta.startsWith("*");

        String name = startsWithAsterisk ? nameWithMeta.substring(1) : nameWithMeta;
        add(name, startsWithAsterisk);
    }

    private void add(String name, boolean isKeyColumn) {
        int newIdx = namesByIndex.size();

        if (isKeyColumn) {
            keyNames.add(name);
            keyIdx.add(newIdx);
        }

        Integer previousIndex = indexByName.put(name, newIdx);
        if (previousIndex != null) {
            throw new IllegalStateException("getHeader name '" + name + "' was already present. current getHeader: " + name);
        }

        namesByIndex.add(name);
    }

    public String columnNameByIdx(int idx) {
        validateIdx(idx);
        return namesByIndex.get(idx);
    }

    public int size() {
        return namesByIndex.size();
    }

    @Override
    public String toString() {
        return namesByIndex.toString();
    }

    /**
     * column index by column name
     * @param columnName column name to get index for
     * @return column index or -1 if not found
     */
    public int findColumnIdxByName(String columnName) {
        Integer idx = indexByName.get(columnName);
        return idx == null ? -1: idx;
    }

    /**
     * column index by column name
     * @param columnName column name to get index for
     * @return column index
     * @throws IllegalArgumentException if column is not defined
     */
    public int columnIdxByName(String columnName) {
        Integer idx = indexByName.get(columnName);
        if (idx == null) {
            throw new IllegalArgumentException("column '" + columnName + "' is not present");
        }

        return idx;
    }

    public void validateIdx(int idx) {
        if (idx < 0 || idx >= namesByIndex.size()) {
            throw new IllegalArgumentException("column idx " + idx + " is out of boundaries. header size is " +
                namesByIndex.size() + ", header is " + namesByIndex);
        }
    }
}
