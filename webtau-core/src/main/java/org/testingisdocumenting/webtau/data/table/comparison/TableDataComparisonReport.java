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

import org.testingisdocumenting.webtau.data.table.render.TableRenderer;

public class TableDataComparisonReport {
    private final TableDataComparisonAdditionalResult result;

    public TableDataComparisonReport(TableDataComparisonAdditionalResult result) {
        this.result = result;
    }

    public String extraRowsReport() {
        return "extra rows:\n" + TableRenderer.render(result.getExtraRows());
    }

    public String missingRowsReport() {
        return "missing rows:\n" + TableRenderer.render(result.getMissingRows());
    }

    public String missingColumnsReport() {
        return "missing columns: " + String.join(", ", result.getMissingColumns());
    }
}
