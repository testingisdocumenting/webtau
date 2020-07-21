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

package org.testgisdocumenting.webtau.graphql;

import org.testingisdocumenting.webtau.report.ReportCustomData;
import org.testingisdocumenting.webtau.report.ReportDataProvider;
import org.testingisdocumenting.webtau.reporter.WebTauTestList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphQLReportDataProvider implements ReportDataProvider {
    @Override
    public Stream<ReportCustomData> provide(WebTauTestList tests) {
        List<? extends Map<String, ?>> nonCoveredOperations = GraphQL.getCoverage().nonCoveredOperations()
                .map(GraphQLOperation::toMap)
                .collect(Collectors.toList());

        List<? extends Map<String, ?>> coveredOperations = GraphQL.getCoverage().coveredOperations()
                .map(GraphQLOperation::toMap)
                .collect(Collectors.toList());

        return Stream.of(
                new ReportCustomData("graphQLSkippedOperations", nonCoveredOperations),
                new ReportCustomData("graphQLCoveredOperations", coveredOperations));
    }
}
