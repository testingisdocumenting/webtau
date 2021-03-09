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

import org.testingisdocumenting.webtau.data.render.DataRenderers;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.data.table.header.TableDataHeader;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.*;

/**
 * <code>DbQuery</code> defines a query to be evaluated at later stage.
 * It is compatible with <code>should</code> and <code>waitTo</code> matchers.
 * <p>
 * To define a query use <code>db.query("select * from table where id=:id", [id: 'my-id'])</code>
 */
public class DbQuery implements ActualValueExpectations, ActualPathAndDescriptionAware {
    private static final ActualPath ACTUAL_PATH = new ActualPath("query result");

    private final String dataSourceLabel;
    private final Supplier<List<Map<String, Object>>> dataFetcher;
    private final String query;
    private final Map<String, Object> params;

    DbQuery(String dataSourceLabel, Supplier<List<Map<String, Object>>> dataFetcher, String query, Map<String, Object> params) {
        this.dataSourceLabel = dataSourceLabel;
        this.dataFetcher = dataFetcher;
        this.query = query;
        this.params = params;
    }

    public int numberOfRows() {
        return tableData().numberOfRows();
    }

    public TableData tableData() {
        return fetchValueAsStep(this::queryTableDataNoStep);
    }

    public <E> E singleValue() {
        return fetchValueAsStep(this::querySingleValueNoStep);
    }

    @Override
    public ActualPath actualPath() {
        return ACTUAL_PATH;
    }

    @Override
    public TokenizedMessage describe() {
        return appendParamsIfRequired(tokenizedMessage(queryValue(query)));
    }

    @Override
    public StepReportOptions shouldReportOption() {
        return StepReportOptions.REPORT_ALL;
    }

    boolean isSingleValue(TableData result) {
        return result.numberOfRows() == 1 && result.getHeader().size() == 1;
    }

    <E> E getUnderlyingSingleValue(TableData result) {
        return result.row(0).get(0);
    }

    TableData queryTableDataNoStep() {
        return convertToTable(dataFetcher.get());
    }

    <E> E querySingleValueNoStep() {
        TableData table = queryTableDataNoStep();
        if (!isSingleValue(table)) {
            throw new RuntimeException(query + " result is not a single value:\n" + DataRenderers.render(table));
        }

        return getUnderlyingSingleValue(table);
    }

    private <E> E fetchValueAsStep(Supplier<Object> supplier) {
        WebTauStep step = createStep(null,
                queryMessage("running DB query"),
                () -> queryMessage("ran DB query"),
                supplier);

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    private TokenizedMessage queryMessage(String actionLabel) {
        return appendParamsIfRequired(
                tokenizedMessage(action(actionLabel), stringValue(query), ON, id(dataSourceLabel)));
    }

    private TokenizedMessage appendParamsIfRequired(TokenizedMessage message) {
        if (params.isEmpty()) {
            return message;
        }

        return message.add(WITH, stringValue(params));
    }

    private TableData convertToTable(List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            return new TableData(Collections.emptyList());
        }

        List<String> columns = result.get(0).keySet().stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        TableDataHeader header = new TableDataHeader(columns.stream());
        TableData tableData = new TableData(header);
        result.forEach(row -> tableData.addRow(row.values().stream()));

        return tableData;
    }
}
