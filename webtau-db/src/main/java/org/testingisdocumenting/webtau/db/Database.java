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

import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.util.Collections;
import java.util.Map;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.createStep;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class Database {
    private final LabeledDataSourceProvider dataSourceProvider;

    Database(LabeledDataSourceProvider dataSourceProvider) {
        this.dataSourceProvider = dataSourceProvider;
    }

    public DatabaseTable table(String name) {
        return new DatabaseTable(dataSourceProvider, name);
    }

    public DbQuery query(String query) {
        return QueryRunnerUtils.createQuery(dataSourceProvider, query);
    }

    public DbQuery query(String query, Map<String, Object> params) {
        return QueryRunnerUtils.createQuery(dataSourceProvider, query, params);
    }

    public <E> DbQuery query(String query, E singleParam) {
        return QueryRunnerUtils.createQuery(dataSourceProvider, query, DbNamedParamsQuery.singleNoNameParam(singleParam));
    }

    public TableData queryTableData(String query) {
        return queryTableData(query, Collections.emptyMap());
    }

    public TableData queryTableData(String query, Map<String, Object> params) {
        return query(query, params).tableData();
    }

    public <E> E querySingleValue(String query) {
        return querySingleValue(query, Collections.emptyMap());
    }

    public <E> E querySingleValue(String query, Map<String, Object> params) {
        return query(query, params).singleValue();
    }

    public void update(String query) {
        update(query, Collections.emptyMap());
    }

    public <E> void update(String query, E singleParam) {
        update(query, DbNamedParamsQuery.singleNoNameParam(singleParam));
    }

    public void update(String query, Map<String, Object> params) {
        DbNamedParamsQuery namedParamsQuery = new DbNamedParamsQuery(query, params);

        WebTauStep step = createStep(null,
                updateMessage("running DB update", query, namedParamsQuery.effectiveParams(), null),
                (rows) -> updateMessage("ran DB update", query, Collections.emptyMap(), (Integer) rows),
                () -> QueryRunnerUtils.runUpdate(dataSourceProvider.provide().getDataSource(), query, namedParamsQuery));

        step.execute(StepReportOptions.REPORT_ALL);
    }

    private TokenizedMessage updateMessage(String actionLabel,
                                           String query,
                                           Map<String, Object> params,
                                           Integer numberOfRows) {
        return appendParamsAndAffectedIfRequired(
                tokenizedMessage(action(actionLabel), stringValue(query), ON,
                        id(dataSourceProvider.provide().getLabel())),
                params,
                numberOfRows);
    }

    private TokenizedMessage appendParamsAndAffectedIfRequired(TokenizedMessage message,
                                                               Map<String, Object> params,
                                                               Integer numberOfRows) {
        if (!params.isEmpty()) {
            message.add(WITH, stringValue(params));
        }

        if (numberOfRows != null) {
            message.add(action("affected"), numberValue(numberOfRows), classifier("rows"));
        }

        return message;
    }
}
