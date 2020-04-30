/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.data.table.header.Header;
import org.testingisdocumenting.webtau.db.gen.SqlQueriesGenerator;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class DatabaseTable {
    private final DataSource dataSource;
    private final String name;

    public DatabaseTable(DataSource dataSource, String name) {
        this.dataSource = dataSource;
        this.name = name;
    }

    public void insert(TableData tableData) {
        if (tableData.isEmpty()) {
            return;
        }

        QueryRunner run = new QueryRunner(dataSource);
        try {
            Object[][] values = new Object[tableData.numberOfRows()][];
            for (int idx = 0; idx < tableData.numberOfRows(); idx++) {
                values[idx] = tableData.row(idx).valuesStream().toArray();
            }

            run.batch(SqlQueriesGenerator.insert(name, tableData.row(0)), values);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TableData load() {
        QueryRunner run = new QueryRunner(dataSource);
        MapListHandler handler = new MapListHandler();

        try {
            List<Map<String, Object>> result = run.query(SqlQueriesGenerator.query(name), handler);
            if (result.isEmpty()) {
                return new TableData(Collections.emptyList());
            }

            List<String> columns = result.get(0).keySet().stream()
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());

            Header header = new Header(columns.stream());
            TableData tableData = new TableData(header);
            result.forEach(row -> tableData.addRow(row.values().stream()));

            return tableData;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void leftShift(TableData tableData) {
        insert(tableData);
    }
}
