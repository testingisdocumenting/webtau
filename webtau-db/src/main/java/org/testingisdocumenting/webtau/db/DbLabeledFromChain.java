/*
 * Copyright 2023 webtau maintainers
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

import org.testingisdocumenting.webtau.reporter.MessageToken;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.reporter.WebTauStepInputKeyValue;

import javax.sql.DataSource;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;

public class DbLabeledFromChain {
    private final String label;

    DbLabeledFromChain(String label) {
        this.label = label;
    }

    public Database fromJdbc(String url, String user, String password, String driverClassName) {
        return from(() -> {
            MessageToken dataSourceToken = classifier("DataSource");
            MessageToken jdbcToken = classifier("JDBC driver");
            WebTauStep step = WebTauStep.createStep(tokenizedMessage(action("getting"), dataSourceToken, AS, id(label), FROM, jdbcToken),
                    () -> tokenizedMessage(action("got"), dataSourceToken, AS, id(label), FROM, jdbcToken),
                    () -> new LabeledDataSource(label,
                            HikariDataSourceUtils.create(url, user, password, driverClassName)));

            step.setInput(WebTauStepInputKeyValue.stepInput("url", url,
                    "user", user,
                    "driverClassName", driverClassName));

            return step.execute(StepReportOptions.REPORT_ALL);
        });
    }

    public Database fromDataSource(DataSource dataSource) {
        return from(() -> new LabeledDataSource(label, dataSource));
    }

    private Database from(LabeledDataSourceProvider labeledDataSourceProvider) {
        return new Database(new LabeledDataSourceCachedProvider(labeledDataSourceProvider));
    }
}
