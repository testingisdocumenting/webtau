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
    static DbQuery createQuery(LabeledDataSource dataSource, String query) {
        return createQuery(dataSource, query, Collections.emptyMap());
    }

    static DbQuery createQuery(LabeledDataSource dataSource, String query, Map<String, Object> params) {
        QueryRunner run = new QueryRunner(dataSource.getDataSource());
        MapListHandler handler = new MapListHandler();

        DbNamedParamsQuery namedParamsQuery = new DbNamedParamsQuery(query, params);

        return new DbQuery(dataSource.getLabel(),
                () -> runQuery(run, handler, namedParamsQuery),
                query, namedParamsQuery.effectiveParams());
    }

    static int runUpdate(DataSource dataSource, String query) {
        return runUpdate(dataSource, query, Collections.emptyMap());
    }

    static int runUpdate(DataSource dataSource, String query, Map<String, Object> params) {
        QueryRunner run = new QueryRunner(dataSource);

        try {
            if (params.isEmpty()) {
                return run.update(query);
            } else {
                DbNamedParamsQuery namedParamsQuery = new DbNamedParamsQuery(query, params);
                return run.update(namedParamsQuery.getQuestionMarksQuery(), namedParamsQuery.getQuestionMarksValues());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Map<String, Object>> runQuery(QueryRunner runner,
                                                      MapListHandler handler,
                                                      DbNamedParamsQuery namedParamsQuery) {
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
