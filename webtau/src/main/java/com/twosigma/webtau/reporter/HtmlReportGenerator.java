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

package com.twosigma.webtau.reporter;

import com.twosigma.webtau.utils.JsonUtils;
import com.twosigma.webtau.utils.ResourceUtils;

import java.util.Map;

public class HtmlReportGenerator {
    private String css;
    private String bundleJavaScript;

    public HtmlReportGenerator() {
        Map<String, Object> manifest = loadManifest();

        css = ResourceUtils.textContent(manifest.get("main.css").toString());
        bundleJavaScript = ResourceUtils.textContent(manifest.get("main.js").toString());
    }

    public String generate(String reportJson) {
        return generateHtml("testReport = " + reportJson + ";");
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

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadManifest() {
        String assetManifest = ResourceUtils.textContent("asset-manifest.json");
        return (Map<String, Object>) JsonUtils.deserialize(assetManifest);
    }
}
