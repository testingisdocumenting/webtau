/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.openapi;

import com.twosigma.webtau.report.ReportData;
import com.twosigma.webtau.report.ReportDataProvider;
import com.twosigma.webtau.report.ReportTestEntry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpenApiReportDataProvider implements ReportDataProvider {
    @Override
    public Stream<ReportData> provide(Stream<ReportTestEntry> testEntries) {
        List<? extends Map<String, ?>> nonCovered = OpenApi.coverage.nonCoveredOperations()
                .map(OpenApiOperation::toMap)
                .collect(Collectors.toList());

        return Stream.of(new ReportData("nonCoveredUrls", nonCovered));
    }
}
