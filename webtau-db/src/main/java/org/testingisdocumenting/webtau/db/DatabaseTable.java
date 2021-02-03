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
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.db.gen.SqlQueriesGenerator;
import org.testingisdocumenting.webtau.reporter.MessageToken;

import java.sql.SQLException;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.createAndExecuteStep;

class DatabaseTable {
    private final LabeledDataSource dataSource;
    private final String name;

    public DatabaseTable(LabeledDataSource dataSource, String name) {
        this.dataSource = dataSource;
        this.name = name;
    }

    public void insert(TableData tableData) {
        createAndExecuteStep(
                tokenizedMessage(action("inserting"), numberValue(tableData.numberOfRows()), action("row(s)"),
                        INTO, createMessageId()),
                () -> tokenizedMessage(action("inserted"), numberValue(tableData.numberOfRows()), action("row(s)"),
                        INTO, createMessageId()),
                () -> insertStep(tableData));
    }

    public TableData query() {
        return createQuery().queryTableData();
    }

    public DbQuery createCountQuery() {
        return QueryRunnerUtils.createQuery(dataSource, SqlQueriesGenerator.count(name));
    }

    public DbQuery createQuery() {
        return QueryRunnerUtils.createQuery(dataSource, SqlQueriesGenerator.fullTable(name));
    }

    private void insertStep(TableData tableData) {
        if (tableData.isEmpty()) {
            return;
        }

        QueryRunner run = new QueryRunner(dataSource.getDataSource());
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

    private MessageToken createMessageId() {
        return id(dataSource.getLabel() + "." + name);
    }

    public void leftShift(TableData tableData) {
        insert(tableData);
    }
}
