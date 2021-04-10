/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.http.perf;

import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler;
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandler;
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult;
import org.testingisdocumenting.webtau.report.ReportCustomData;
import org.testingisdocumenting.webtau.report.ReportDataProvider;
import org.testingisdocumenting.webtau.report.perf.PerformanceReport;
import org.testingisdocumenting.webtau.reporter.WebTauTestList;

import java.util.stream.Stream;

public class HttpPerformanceValidationHandler implements HttpValidationHandler, ReportDataProvider, WebTauConfigHandler {
    private static final PerformanceReport performanceReport = new PerformanceReport("httpPerformance");

    public HttpPerformanceValidationHandler() {
    }

    @Override
    public void validate(HttpValidationResult validationResult) {
        performanceReport.addOperation(validationResult.getOperationId(),
                validationResult.getRequestMethod() + " " + validationResult.getFullUrl(),
                validationResult.getStartTime(),
                validationResult.getElapsedTime());
    }

    // we use this callback as a signal that another webtau run started within the same JVM
    @Override
    public void onBeforeCreate(WebTauConfig cfg) {
        performanceReport.reset();
    }

    @Override
    public Stream<ReportCustomData> provide(WebTauTestList tests) {
        return Stream.of(performanceReport.build());
    }
}
