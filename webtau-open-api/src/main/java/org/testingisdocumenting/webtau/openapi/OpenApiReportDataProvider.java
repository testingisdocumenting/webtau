/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.openapi;

import org.testingisdocumenting.webtau.reporter.WebTauReportCustomData;
import org.testingisdocumenting.webtau.report.ReportDataProvider;
import org.testingisdocumenting.webtau.reporter.WebTauReportLog;
import org.testingisdocumenting.webtau.reporter.WebTauTestList;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpenApiReportDataProvider implements ReportDataProvider {
    @Override
    public Stream<WebTauReportCustomData> provide(WebTauTestList tests, WebTauReportLog log) {
        if (OpenApi.isCoverageUninitialized()) {
            return Stream.empty();
        }

        List<? extends Map<String, ?>> nonCoveredOperations = OpenApi.getCoverage().nonCoveredOperations()
                .map(OpenApiOperation::toMap)
                .collect(Collectors.toList());

        List<? extends Map<String, ?>> coveredOperations = OpenApi.getCoverage().coveredOperations()
                .map(OpenApiOperation::toMap)
                .collect(Collectors.toList());

        List<? extends Map<String, ?>> coveredResponses = convertResponses(OpenApi.getCoverage().coveredResponses());
        List<? extends Map<String, ?>> nonCoveredResponses = convertResponses(OpenApi.getCoverage().nonCoveredResponses());

        return Stream.of(
                new WebTauReportCustomData("openApiSkippedOperations", nonCoveredOperations),
                new WebTauReportCustomData("openApiCoveredOperations", coveredOperations),
                new WebTauReportCustomData("openApiHttpCallIdsPerOperation",
                        OpenApi.getCoverage().httpCallIdsByOperationAsMap()),
                new WebTauReportCustomData("openApiHttpCallsPerOperation",
                        OpenApi.getCoverage().httpCallsByOperationAsMap()),
                new WebTauReportCustomData("openApiCoveredResponses", coveredResponses),
                new WebTauReportCustomData("openApiSkippedResponses", nonCoveredResponses));
    }

    private static List<? extends Map<String, ?>> convertResponses(Map<OpenApiOperation, Set<String>> responses) {
        return responses.entrySet()
                .stream()
                .flatMap(entry ->
                        entry.getValue().stream().map(statusCode -> {
                            Map<String, Object> responseMap = new HashMap<>(entry.getKey().toMap());
                            responseMap.put("statusCode", statusCode);

                            return responseMap;
                        }))
                .collect(Collectors.toList());
    }
}
