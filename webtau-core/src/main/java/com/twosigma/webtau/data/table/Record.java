/*
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

package com.twosigma.webtau.data.table;

import com.twosigma.webtau.data.MultiValue;
import com.twosigma.webtau.data.table.autogen.TableDataCellValueGenerator;
import com.twosigma.webtau.data.table.header.CompositeKey;
import com.twosigma.webtau.data.table.header.Header;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Record {
    private final Header header;
    private final List<Object> values;
    private final CompositeKey key;

    private final boolean hasMultiValues;
    private final boolean hasValueGenerators;

    public Record(Header header, Stream<Object> values) {
        this.header = header;
        RecordFromStream recordFromStream = new RecordFromStream(values);

        hasMultiValues = recordFromStream.hasMultiValues;
        hasValueGenerators = recordFromStream.hasValueGenerators;
        this.values = recordFromStream.values;

        this.key = header.hasKeyColumns() ?
                new CompositeKey(header.getKeyIdxStream().map(this::get)) : null;
    }

    public Header getHeader() {
        return header;
    }

    public CompositeKey getKey() {
        return key;
    }

    @SuppressWarnings("unchecked")
    public <E> E get(String name) {
        return (E) values.get(header.columnIdxByName(name));
    }

    @SuppressWarnings("unchecked")
    public <E> E get(int idx) {
        header.validateIdx(idx);
        return (E) values.get(idx);
    }

    public Stream<Object> valuesStream() {
        return values.stream();
    }

    public List<Object> getValues() {
        return values;
    }

    public boolean hasMultiValues() {
        return this.hasMultiValues;
    }

    public boolean hasValueGenerators() {
        return this.hasValueGenerators;
    }

    @SuppressWarnings("unchecked")
    public <T, R> Stream<R> mapValues(Function<T, R> mapper) {
        return values.stream().map(v -> mapper.apply((T) v));
    }

    public List<Record> unwrapMultiValues() {
        MultiValuesUnwrapper multiValuesUnwrapper = new MultiValuesUnwrapper();
        multiValuesUnwrapper.add(this);

        return multiValuesUnwrapper.result;
    }

    public Record evaluateValueGenerators(Record previous, int rowIdx) {
        if (!hasValueGenerators()) {
            return this;
        }

        List<Object> newValues = new ArrayList<>(this.values.size());
        int colIdx = 0;
        for (Object value : this.values) {
            if (value instanceof TableDataCellValueGenerator) {
                newValues.add(((TableDataCellValueGenerator<?>) value).generate(
                        this, previous, rowIdx, colIdx, header.columnNameByIdx(colIdx)));
            } else {
                newValues.add(value);
            }

            colIdx++;
        }

        return new Record(header, newValues.stream());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        header.getColumnIdxStream().forEach(i -> result.put(header.columnNameByIdx(i), values.get(i)));

        return result;
    }

    @Override
    public String toString() {
        return toMap().toString();
    }

    private static class MultiValuesUnwrapper {
        private List<Record> result;

        MultiValuesUnwrapper() {
            this.result = new ArrayList<>();
        }

        void add(Record record) {
            for (int idx = record.values.size() - 1; idx >=0; idx--) {
                Object value = record.values.get(idx);

                if (!(value instanceof MultiValue)) {
                    continue;
                }

                ArrayList<Object> copy = new ArrayList<>(record.values);
                final int columnIdx = idx;
                ((MultiValue) value).getValues().forEach(mv -> {
                    copy.set(columnIdx, mv);
                    add(new Record(record.header, copy.stream()));
                });

                return;
            }

            result.add(record);
        }
    }

    private static class RecordFromStream {
        private boolean hasMultiValues;
        private boolean hasValueGenerators;
        private List<Object> values;

        public RecordFromStream(Stream<Object> valuesStream) {
            values = new ArrayList<>();

            valuesStream.forEach(v -> {
                if (v instanceof MultiValue) {
                    hasMultiValues = true;
                }

                if (v instanceof TableDataCellValueGenerator) {
                    hasValueGenerators = true;
                }

                values.add(v);
            });
        }
    }
}
