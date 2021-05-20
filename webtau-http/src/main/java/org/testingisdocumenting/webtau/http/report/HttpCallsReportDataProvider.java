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

package org.testingisdocumenting.webtau.http.report;

import org.testingisdocumenting.webtau.reporter.WebTauReportCustomData;
import org.testingisdocumenting.webtau.report.ReportDataProvider;
import org.testingisdocumenting.webtau.reporter.WebTauReportLog;
import org.testingisdocumenting.webtau.reporter.WebTauTestList;
import org.testingisdocumenting.webtau.reporter.WebTauTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.http.report.HttpCallsTestResultPayloadExtractor.HTTP_CALLS_PAYLOAD_NAME;

public class HttpCallsReportDataProvider implements ReportDataProvider {
    @Override
    public Stream<WebTauReportCustomData> provide(WebTauTestList tests, WebTauReportLog log) {
        List<Map<String, ?>> reportData = tests.stream()
                .flatMap(HttpCallsReportDataProvider::callsFromTest)
                .collect(Collectors.toList());

        return Stream.of(new WebTauReportCustomData(HTTP_CALLS_PAYLOAD_NAME, reportData));
    }

    @SuppressWarnings("unchecked")
    private static Stream<Map<String, ?>> callsFromTest(WebTauTest test) {
        return test.getPayloads().stream()
                .filter(p -> p.getPayloadName().equals(HTTP_CALLS_PAYLOAD_NAME))
                .flatMap(p -> ((List<Map<String, ?>>) p.getPayload()).stream())
                .map(HttpCallsReportDataProvider::httpCallReduced);
    }

    private static Map<String, ?> httpCallReduced(Map<String, ?> fullInfo) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("method", fullInfo.get("method"));
        result.put("url", fullInfo.get("url"));
        result.put("elapsedTime", fullInfo.get("elapsedTime"));

        return result;
    }
}
