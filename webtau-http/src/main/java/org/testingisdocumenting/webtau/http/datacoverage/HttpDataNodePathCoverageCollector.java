/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.http.datacoverage;

import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler;
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandler;
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult;
import org.testingisdocumenting.webtau.report.ReportDataProvider;
import org.testingisdocumenting.webtau.report.ReportGenerator;
import org.testingisdocumenting.webtau.reporter.WebTauReport;
import org.testingisdocumenting.webtau.reporter.WebTauReportCustomData;
import org.testingisdocumenting.webtau.reporter.WebTauReportLog;
import org.testingisdocumenting.webtau.reporter.WebTauTestList;

import java.util.*;
import java.util.stream.Stream;

public class HttpDataNodePathCoverageCollector implements HttpValidationHandler, ReportGenerator, WebTauConfigHandler, ReportDataProvider {
    private final static Map<String, HttpOperationDataCoverage> coverageByOperationId = new HashMap<>();

    @Override
    public void validate(HttpValidationResult validationResult) {
        if (validationResult.getOperationId().isEmpty()) {
            return;
        }

        HttpOperationDataCoverage coverage = coverageByOperationId.
                computeIfAbsent(validationResult.getOperationId(), (id) -> new HttpOperationDataCoverage());

        coverage.addObservedPaths(validationResult.getAllNormalizedPaths());
        coverage.addTouchedPaths(validationResult.getPassedNormalizedPaths());
    }

    // we use this callback as a signal that another webtau run started within the same JVM
    @Override
    public void onBeforeCreate(WebTauConfig cfg) {
        coverageByOperationId.clear();
    }

    @Override
    public Stream<WebTauReportCustomData> provide(WebTauTestList tests, WebTauReportLog log) {
        if (coverageByOperationId.isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(new WebTauReportCustomData("httpDataCoverage", buildCoverageData()));
    }

    private List<Map<String, Object>> buildCoverageData() {
        List<Map<String, Object>> result = new ArrayList<>();
        coverageByOperationId.forEach((operationId, coverage) -> {
            int touchedCount = coverage.countNumberOfTouchedPaths();
            if (touchedCount == 0) {
                return;
            }

            int untouchedCount = coverage.countNumberOfUntouchedPaths();
            int untouchedPercent = (int) Math.round(untouchedCount / (1.0 *coverage.countTotalNumberOfPaths()) * 100);

            Map<String, Object> data = new HashMap<>();
            data.put("id", operationId);
            data.put("touchedPathsCount", touchedCount);
            data.put("untouchedPathsCount", untouchedCount);
            data.put("untouchedPercent", untouchedPercent);
            data.put("untouchedPaths", coverage.computeUntouchedPaths());

            result.add(data);
        });

        return result;
    }

    @Override
    public void generate(WebTauReport report) {
    }
}
