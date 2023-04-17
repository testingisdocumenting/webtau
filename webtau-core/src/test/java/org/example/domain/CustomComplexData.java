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

package org.example.domain;

import java.util.*;

public class CustomComplexData implements Iterable<List<Object>> {
    private final List<String> columnNames;
    private final List<List<Object>> values;

    public CustomComplexData(String... columns) {
        this.columnNames = Arrays.asList(columns);
        this.values = new ArrayList<>();
    }

    public List<String> getColumnNames() {
        return Collections.unmodifiableList(columnNames);
    }

    public void addRow(Object... values) {
        this.values.add(Arrays.asList(values));
    }

    @Override
    public Iterator<List<Object>> iterator() {
        return values.iterator();
    }

    @Override
    public String toString() {
        return "CustomComplexData{" +
                "columnNames=" + columnNames +
                ", values=" + values +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomComplexData that = (CustomComplexData) o;
        return columnNames.equals(that.columnNames) && values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnNames, values);
    }
}
