/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.report;

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;
import static java.util.stream.Collectors.toList;

import org.testingisdocumenting.webtau.cfg.ConfigValue;
import org.testingisdocumenting.webtau.console.ConsoleOutputs;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.reporter.WebTauReportCustomData;
import org.testingisdocumenting.webtau.reporter.WebTauReport;
import org.testingisdocumenting.webtau.reporter.WebTauTest;
import org.testingisdocumenting.webtau.utils.FileUtils;
import org.testingisdocumenting.webtau.utils.JsonUtils;
import org.testingisdocumenting.webtau.utils.ResourceUtils;
import org.testingisdocumenting.webtau.version.WebtauVersion;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HtmlReportGenerator implements ReportGenerator {
    private final ReactJsBundle reactJsBundle;
    private final String themeCode = ResourceUtils.textContent("webtau-theme.js");

    public HtmlReportGenerator() {
        reactJsBundle = new ReactJsBundle();
    }

    @Override
    public void generate(WebTauReport report) {
        Path reportPath = reportPath(report);

        FileUtils.writeTextContent(reportPath, generateHtml(report));
        ConsoleOutputs.out(Color.BLUE, "report is generated: ", Color.PURPLE, " ", reportPath);
    }

    private Path reportPath(WebTauReport report) {
        if (report.isFailed()) {
            Path failedReportPath = getCfg().getFailedReportPath();
            return failedReportPath != null ? failedReportPath : getCfg().getReportPath();
        }

        return getCfg().getReportPath();
    }

    private String generateHtml(WebTauReport report) {
        Map<String, Object> reportAsMap = new LinkedHashMap<>();
        reportAsMap.put("name", report.getReportName().getName());
        reportAsMap.put("nameUrl", report.getReportName().getUrl());
        reportAsMap.put("config", configAsListOfMaps(getCfg().getEnumeratedCfgValuesStream()));
        reportAsMap.put("envVars", envVarsAsListOfMaps());
        reportAsMap.put("summary", reportSummaryToMap(report));
        reportAsMap.put("version", WebtauVersion.getVersion());
        reportAsMap.put("tests", report.getTests().stream()
                .map(WebTauTest::toMap).collect(Collectors.toList()));

        reportAsMap.put("log", report.getReportLog().toMap());

        report.getCustomDataStream()
                .map(WebTauReportCustomData::toMap)
                .forEach(reportAsMap::putAll);

        return generateHtml(reportAsMap);
    }

    String generateHtml(Map<String, Object> report) {
        String serializedJson = JsonUtils.serialize(report);
        String compressed = ReportDataCompressor.compressAndBase64(serializedJson);

        return generateHtml(
                "compressedTestReport = '" + compressed + "';");
    }

    private String generateHtml(String reportAssignmentJavaScript) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<meta charset=\"UTF-8\"/>\n" +
                "<head>\n" +
                "<style>\n" +
                reactJsBundle.getCss() + "\n" +
                "</style>" +
                genFavIconBase64() + "\n" +
                "<title>WebTau Report</title>" +
                "\n</head>\n" +
                "<body class=\"webtau-light\"><div id=\"root\"/>\n" +
                "<script>\n" +
                themeCode + "\n" +
                reportAssignmentJavaScript + "\n" +
                reactJsBundle.getJavaScript() + "\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";
    }

    private List<Map<String, Object>> configAsListOfMaps(Stream<ConfigValue> cfgValuesStream) {
        return cfgValuesStream
                .filter(v -> !v.isDefault() || v.getKey().equals("env"))
                .map(ConfigValue::toMap).collect(toList());
    }

    private List<Map<String, String>> envVarsAsListOfMaps() {
        return System.getenv().entrySet().stream()
                .map(e -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("key", e.getKey());
                    map.put("value", e.getValue());

                    return map;
                })
                .collect(toList());
    }

    private String genFavIconBase64() {
        byte[] content = ResourceUtils.binaryContent("webtau-icon.png");
        String encoded = Base64.getEncoder().encodeToString(content);

        return "<link rel=\"shortcut icon\" href=\"data:image/png;base64," + encoded + "\">";
    }

    private Map<String, Object> reportSummaryToMap(WebTauReport report) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", report.getTotal());
        result.put("passed", report.getPassed());
        result.put("failed", report.getFailed());
        result.put("skipped", report.getSkipped());
        result.put("errored", report.getErrored());
        result.put("startTime", report.getStartTime());
        result.put("stopTime", report.getStopTime());
        result.put("duration", report.getDuration());

        return result;
    }
}
