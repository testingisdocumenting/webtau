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

import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.data.table.header.TableDataHeader;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.*;

/**
 * <code>DbQuery</code> defines a query to be evaluated at later stage.
 * It is compatible with <code>should</code> and <code>waitTo</code> matchers.
 * <p>
 * To define a query use <code>db.query("select * from table where id=:id", [id: 'my-id'])</code>
 */
public class DbQuery implements ActualValueExpectations, ActualPathAndDescriptionAware {
    private static final ValuePath ACTUAL_PATH = new ValuePath("query result");

    private final Supplier<String> dataSourceLabelSupplier;
    private final Supplier<List<Map<String, Object>>> dataFetcher;
    private final String query;
    private final Map<String, Object> params;

    DbQuery(Supplier<String> dataSourceLabelSupplier, Supplier<List<Map<String, Object>>> dataFetcher, String query, Map<String, Object> params) {
        this.dataSourceLabelSupplier = dataSourceLabelSupplier;
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
    public ValuePath actualPath() {
        return ACTUAL_PATH;
    }

    @Override
    public TokenizedMessage describe() {
        return appendParamsIfRequired(tokenizedMessage().query(query));
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
        return convertToTable(dataFetcher.get(), TableHeaderConverters::toUpperCase);
    }

    TableData queryTableDataNoStep(Function<String, String> headerConverter) {
        return convertToTable(dataFetcher.get(), headerConverter);
    }

    <E> E querySingleValueNoStep() {
        TableData table = queryTableDataNoStep();
        if (!isSingleValue(table)) {
            throw new RuntimeException(query + " result is not a single value:\n" + PrettyPrinter.renderAsTextWithoutColors(table));
        }

        return getUnderlyingSingleValue(table);
    }

    private <E> E fetchValueAsStep(Supplier<Object> supplier) {
        WebTauStep step = createStep(
                queryMessage("running DB query"),
                () -> queryMessage("ran DB query"),
                supplier);

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    private TokenizedMessage queryMessage(String actionLabel) {
        return appendParamsIfRequired(
                tokenizedMessage().action(actionLabel).string(query).on().id(dataSourceLabelSupplier.get()));
    }

    private TokenizedMessage appendParamsIfRequired(TokenizedMessage message) {
        if (params.isEmpty()) {
            return message;
        }

        return message.with().string(params);
    }

    private TableData convertToTable(List<Map<String, Object>> result,
                                     Function<String, String> headerConverter) {
        if (result.isEmpty()) {
            return new TableData(Collections.emptyList());
        }

        List<String> columns = result.get(0).keySet().stream()
                .map(headerConverter)
                .toList();

        TableDataHeader header = new TableDataHeader(columns.stream());
        TableData tableData = new TableData(header);
        result.forEach(row -> tableData.addRow(row.values().stream()));

        return tableData;
    }
}
