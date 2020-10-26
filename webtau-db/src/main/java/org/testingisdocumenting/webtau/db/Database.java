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

import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStep.createStep;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class Database {
    private final LabeledDataSource dataSource;

    Database(LabeledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DatabaseTable table(String name) {
        return new DatabaseTable(dataSource, name);
    }

    public DatabaseQueryResult query(String query) {
        WebTauStep step = createStep(null,
                tokenizedMessage(action("running DB query"), stringValue(query), ON, id(dataSource.getLabel())),
                () -> tokenizedMessage(action("ran DB query"), stringValue(query), ON, id(dataSource.getLabel())),
                () -> QueryRunnerUtils.runQuery(dataSource.getDataSource(), query));

        return (DatabaseQueryResult) step.execute(StepReportOptions.REPORT_ALL);
    }

    public void update(String query) {
        WebTauStep step = createStep(null,
                tokenizedMessage(action("running DB update"), stringValue(query), ON, id(dataSource.getLabel())),
                (rows) -> tokenizedMessage(action("ran DB update"), stringValue(query), ON, id(dataSource.getLabel()),
                        action("affected"), numberValue(rows), classifier("rows")),
                () -> QueryRunnerUtils.runUpdate(dataSource.getDataSource(), query));

        step.execute(StepReportOptions.REPORT_ALL);
    }
}
