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
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;

public class DbDataSourceProviders {
    private static final List<DbDataSourceProvider> providers = ServiceLoaderUtils.load(DbDataSourceProvider.class);

    public static DataSource provideByName(String name) {
        WebTauStep step = WebTauStep.createStep(TokenizedMessage.tokenizedMessage(action("creating"), classifier("db datasource"), id(name)),
                () -> TokenizedMessage.tokenizedMessage(action("created"), classifier("db datasource"), id(name)),
                () -> createDataSource(name));

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    public static void add(DbDataSourceProvider provider) {
        providers.add(provider);
    }

    public static void remove(DbDataSourceProvider provider) {
        providers.remove(provider);
    }

    private static DataSource createDataSource(String name) {
        return providers.stream()
                .map(p -> p.provide(name))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> noProviderFound(name));
    }

    private static RuntimeException noProviderFound(String name) {
        return new RuntimeException("no DbDataSourceProvider found for " + name + " database");
    }
}

