/*
 * Copyright 2023 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.webtau.data.table.comparison;

import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class TableDataComparisonReport {
    private final TableDataComparisonAdditionalResult result;

    public TableDataComparisonReport(TableDataComparisonAdditionalResult result) {
        this.result = result;
    }

    public TokenizedMessage extraRowsReport() {
        return tokenizedMessage().error("extra rows").colon().value(result.getExtraRows());
    }

    public TokenizedMessage missingRowsReport() {
        return tokenizedMessage().error("missing rows").colon().value(result.getMissingRows());
    }

    public TokenizedMessage missingColumnsReport() {
        return tokenizedMessage().error("missing columns").colon().value(result.getMissingColumns());
    }
}
