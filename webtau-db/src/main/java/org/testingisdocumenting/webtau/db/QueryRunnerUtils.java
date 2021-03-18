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

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class QueryRunnerUtils {
    static DbQuery createQuery(LabeledDataSourceProvider dataSourceProvider, String query) {
        return createQuery(dataSourceProvider, query, Collections.emptyMap());
    }

    static DbQuery createQuery(LabeledDataSourceProvider dataSourceProvider, String query, Map<String, Object> params) {
        DbNamedParamsQuery namedParamsQuery = new DbNamedParamsQuery(query, params);

        return new DbQuery(() -> dataSourceProvider.provide().getLabel(),
                () -> runQuery(dataSourceProvider, namedParamsQuery),
                query, namedParamsQuery.effectiveParams());
    }

    static int runUpdate(DataSource dataSource, String query) {
        return runUpdate(dataSource, query, Collections.emptyMap());
    }

    static int runUpdate(DataSource dataSource, String query, Map<String, Object> params) {
        DbNamedParamsQuery namedParamsQuery = new DbNamedParamsQuery(query, params);
        return runUpdate(dataSource, query, namedParamsQuery);
    }

    static int runUpdate(DataSource dataSource, String query, DbNamedParamsQuery namedParamsQuery) {
        QueryRunner run = new QueryRunner(dataSource);

        try {
            if (namedParamsQuery.isEmpty()) {
                return run.update(query);
            } else {
                return run.update(namedParamsQuery.getQuestionMarksQuery(), namedParamsQuery.getQuestionMarksValues());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Map<String, Object>> runQuery(LabeledDataSourceProvider dataSourceProvider,
                                                      DbNamedParamsQuery namedParamsQuery) {
        QueryRunner runner = new QueryRunner(dataSourceProvider.provide().getDataSource());
        MapListHandler handler = new MapListHandler();

        try {
            if (namedParamsQuery.isEmpty()) {
                return runner.query(namedParamsQuery.getQuestionMarksQuery(), handler);
            }

            return runner.query(namedParamsQuery.getQuestionMarksQuery(),
                    handler,
                    namedParamsQuery.getQuestionMarksValues());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
