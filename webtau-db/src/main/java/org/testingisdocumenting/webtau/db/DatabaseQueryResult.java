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
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.ValueMatcherExpectationSteps;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class DatabaseQueryResult {
    private final String query;
    private final TableData result;

    public DatabaseQueryResult(String query, List<Map<String, Object>> result) {
        this.query = query;
        this.result = convertToTable(result);
    }

    public boolean isSingleValue() {
        return result.numberOfRows() == 1 && result.getHeader().size() == 1;
    }

    public int getNumberOfRows() {
        return result.numberOfRows();
    }

    public <E> E getSingleValue() {
        if (!isSingleValue()) {
            throw new RuntimeException(query + " result is not a single value:\n" + DataRenderers.render(result));
        }

        return getUnderlyingSingleValue();
    }

    public TableData getTableData() {
        return result;
    }

    void should(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldStep(null, underlyingValue(), StepReportOptions.SKIP_START,
                assertionDescription(), valueMatcher);
    }

    void shouldNot(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldNotStep(null, underlyingValue(), StepReportOptions.SKIP_START,
                assertionDescription(), valueMatcher);
    }

    private TokenizedMessage assertionDescription() {
        return tokenizedMessage(IntegrationTestsMessageBuilder.queryValue(query));
    }

    private Object underlyingValue() {
        return isSingleValue() ? getUnderlyingSingleValue() : result;
    }

    private <E> E getUnderlyingSingleValue() {
        return result.row(0).get(0);
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
