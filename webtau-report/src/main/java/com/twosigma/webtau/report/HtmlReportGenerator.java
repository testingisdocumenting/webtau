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

package com.twosigma.webtau.report;

import com.twosigma.webtau.cfg.ConfigValue;
import com.twosigma.webtau.console.ConsoleOutputs;
import com.twosigma.webtau.console.ansi.Color;
import com.twosigma.webtau.utils.FileUtils;
import com.twosigma.webtau.utils.JsonUtils;
import com.twosigma.webtau.utils.ResourceUtils;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.twosigma.webtau.cfg.WebTauConfig.getCfg;
import static java.util.stream.Collectors.toList;

public class HtmlReportGenerator implements ReportGenerator {
    private String css;
    private String bundleJavaScript;

    public HtmlReportGenerator() {
        Map<String, Object> manifest = loadManifest();

        css = ResourceUtils.textContent(manifest.get("main.css").toString());
        bundleJavaScript = ResourceUtils.textContent(manifest.get("main.js").toString());
    }

    public void generate(Report report) {
        Path reportPath = getCfg().getReportPath().toAbsolutePath();

        FileUtils.writeTextContent(reportPath, generateHtml(report));
        ConsoleOutputs.out(Color.BLUE, "report is generated: ", Color.PURPLE, " ", reportPath);
    }

    private String generateHtml(Report report) {
        Map<String, Object> reportAsMap = new LinkedHashMap<>();
        reportAsMap.put("config", configAsListOfMaps(getCfg().getCfgValuesStream()));
        reportAsMap.put("summary", report.createSummary().toMap());
        reportAsMap.put("tests", report.getTestEntries().stream()
                .map(ReportTestEntry::toMap).collect(Collectors.toList()));

        report.extractReportCustomData().stream()
                .map(ReportCustomData::toMap)
                .forEach(reportAsMap::putAll);

        return generateHtml(reportAsMap);
    }

    String generateHtml(Map<String, Object> report) {
        return generateHtml("testReport = " + JsonUtils.serializePrettyPrint(report) + ";");
    }

    private String generateHtml(String reportAssignmentJavaScript) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<meta charset=\"UTF-8\"/>\n" +
                "<head>\n" +
                "<style>\n" +
                css +
                "</style>" +
                "<title>WebTau Report</title>" +
                "\n</head>\n" +
                "<body><div id=\"root\"/>\n" +
                "<script>\n" +
                reportAssignmentJavaScript +
                bundleJavaScript +
                "</script>\n" +
                "\n</body>" +
                "\n</html>\n";

    }

    private List<Map<String, Object>> configAsListOfMaps(Stream<ConfigValue> cfgValuesStream) {
        return cfgValuesStream
                .filter(v -> !v.isDefault() || v.getKey().equals("env"))
                .map(ConfigValue::toMap).collect(toList());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadManifest() {
        String assetManifest = ResourceUtils.textContent("asset-manifest.json");
        return (Map<String, Object>) JsonUtils.deserialize(assetManifest);
    }
}
