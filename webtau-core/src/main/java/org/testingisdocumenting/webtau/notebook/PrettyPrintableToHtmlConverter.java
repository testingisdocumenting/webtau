/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.notebook;

import org.testingisdocumenting.webtau.data.render.PrettyPrinter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * converter from pretty print to HTML for notebooks integration
 */
public class PrettyPrintableToHtmlConverter {
    private PrettyPrintableToHtmlConverter() {
    }

    public static String convert(Object value) {
        PrettyPrinter printer = new PrettyPrinter(0);
        printer.printObject(value);

        List<List<Map<String, Object>>> styledText = printer.generateStyledTextListOfListsOfMaps();

        StringBuilder result = new StringBuilder();
        result.append("<pre>");

        int lineIdx = 0;
        for (List<Map<String, Object>> line : styledText) {
            for (Map<String, Object> styleAndText : line) {
                String text = styleAndText.get("text").toString();
                @SuppressWarnings("unchecked")
                List<String> styles = (List<String>) styleAndText.get("styles");

                result.append(buildSpan(text, styles));
            }

            boolean isLastLine = lineIdx == styledText.size() - 1;
            if (!isLastLine) {
                result.append("\n");
            }

            lineIdx++;
        }

        result.append("</pre>");
        return result.toString();
    }

    private static String buildSpan(String text, List<String> styles) {
        return "<span style=\"" + convertToHtmlStyle(styles) + "\">" + text + "</span>";
    }

    private static String convertToHtmlStyle(List<String> styles) {
        return styles.stream()
                .map(PrettyPrintableToHtmlConverter::convertToHtmlStyle)
                .collect(Collectors.joining(" "));
    }

    private static String convertToHtmlStyle(String style) {
        switch (style) {
            case "black":
                return "color: var(--md-black-500)";
            case "red":
                return "color: var(--md-red-500)";
            case "green":
                return "color: var(--md-green-500)";
            case "yellow":
                return "color: var(--md-yellow-700)";
            case "blue":
                return "color: var(--md-blue-500)";
            case "purple":
                return "color: var(--md-purple-300)";
            case "cyan":
                return "color: var(--md-cyan-500)";
            case "white":
                return "color: var(--md-white-500)";
            case "bold":
                return "font-weight: bold";
            default:
                return "";
        }
    }
}
