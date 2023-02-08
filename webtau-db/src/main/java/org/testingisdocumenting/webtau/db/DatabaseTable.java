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
import org.apache.commons.lang3.StringUtils;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.db.gen.SqlQueriesGenerator;
import org.testingisdocumenting.webtau.reporter.MessageToken;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.createAndExecuteStep;

public class DatabaseTable {
    private final Database database;
    private final LabeledDataSourceProvider dataSourceProvider;
    private final String name;

    public DatabaseTable(Database database, LabeledDataSourceProvider dataSourceProvider, String name) {
        this.database = database;
        this.dataSourceProvider = dataSourceProvider;
        this.name = name;
    }

    public void insert(TableData tableData) {
        createAndExecuteStep(
                insertingMessage(tableData.numberOfRows()),
                () -> insertedMessage(tableData.numberOfRows()),
                () -> insertTableStep(tableData));
    }

    public void insert(List<Map<String, Object>> rows) {
        createAndExecuteStep(
                insertingMessage(rows.size()),
                () -> insertedMessage(rows.size()),
                () -> insertTableStep(rows));
    }

    public void insert(Map<String, Object> row) {
        createAndExecuteStep(
                insertingMessage(1),
                () -> insertedMessage(1),
                () -> insertRowStep(row));
    }

    public void clear() {
        database.update("delete from " + name);
    }

    public DbQuery queryCount() {
        return QueryRunnerUtils.createQuery(dataSourceProvider, SqlQueriesGenerator.count(name));
    }

    public DbQuery query() {
        return QueryRunnerUtils.createQuery(dataSourceProvider, SqlQueriesGenerator.fullTable(name));
    }

    private TokenizedMessage insertingMessage(int numberOfRows) {
        return insertMessageWithLabel("inserting", numberOfRows);
    }

    private TokenizedMessage insertedMessage(int numberOfRows) {
        return insertMessageWithLabel("inserted", numberOfRows);
    }

    private TokenizedMessage insertMessageWithLabel(String actionLabel, int numberOfRows) {
        return tokenizedMessage(action(actionLabel), numberValue(numberOfRows),
                numberOfRows > 1 ? action("rows") : action("row"),
                INTO, createMessageId());
    }

    private void insertTableStep(TableData tableData) {
        insertMultipleRowsStep(tableData::isEmpty,
                tableData::numberOfRows,
                () -> extractHeaderStream(tableData.getHeader().getNamesStream()),
                (idx) -> tableData.row(idx).valuesStream());
    }

    private Stream<String> extractHeaderStream(Stream<String> original) {
        return original.map(this::convertToUnderscoresIfRequired);
    }

    private String convertToUnderscoresIfRequired(String name) {
        if (name.contains("_")) {
            return name;
        }

        String[] parts = StringUtils.splitByCharacterTypeCamelCase(name);
        return Arrays.stream(parts)
                .map(String::toUpperCase)
                .collect(Collectors.joining("_"));
    }

    private void insertTableStep(List<Map<String, Object>> rows) {
        insertMultipleRowsStep(rows::isEmpty,
                rows::size,
                () -> rows.get(0).keySet().stream(),
                (idx) -> rows.get(idx).values().stream());
    }

    private void insertMultipleRowsStep(Supplier<Boolean> isEmpty,
                                        Supplier<Integer> size,
                                        Supplier<Stream<String>> header,
                                        Function<Integer, Stream<Object>> valuesByRowIdx) {
        if (isEmpty.get()) {
            return;
        }

        QueryRunner run = new QueryRunner(dataSourceProvider.provide().getDataSource());
        try {
            int numberOfRows = size.get();
            Object[][] values = new Object[numberOfRows][];
            for (int idx = 0; idx < numberOfRows; idx++) {
                values[idx] = valuesByRowIdx.apply(idx).toArray();
            }

            run.batch(SqlQueriesGenerator.insert(name, header.get(), valuesByRowIdx.apply(0)), values);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertRowStep(Map<String, Object> row) {
        QueryRunner run = new QueryRunner(dataSourceProvider.provide().getDataSource());
        try {
            run.update(SqlQueriesGenerator.insert(name, row.keySet().stream(), row.values().stream()),
                    row.values().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageToken createMessageId() {
        return id(dataSourceProvider.provide().getLabel() + "." + name);
    }

    public void leftShift(TableData tableData) {
        insert(tableData);
    }

    public void leftShift(Map<String, Object> row) {
        insert(row);
    }

    public void leftShift(List<Map<String, Object>> rows) {
        insert(rows);
    }
}
