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

package org.testingisdocumenting.webtau.console.ansi;

import org.testingisdocumenting.webtau.console.ConsoleOutput;

import java.util.*;
import java.util.stream.Collectors;

/**
 * converts ansi sequences into easy to consume textual representations to be used on web report
 */
public class AnsiAsStylesValuesListConsoleOutput implements ConsoleOutput {
    private StyledLine currentLine = new StyledLine();
    private final List<StyledLine> lines = new ArrayList<>();
    private final List<String> currentStyles = new ArrayList<>();
    private final List<String> currentValues = new ArrayList<>();

    @Override
    public void out(Object... styleOrValues) {
        for (Object styleOrValue : styleOrValues) {
            handleStyleOrValue(styleOrValue);
        }

        flushCurrentValues();

        lines.add(currentLine);
        currentLine = new StyledLine();
    }

    @Override
    public void err(Object... styleOrValues) {
        throw new UnsupportedOperationException();
    }

    public List<List<Map<String, Object>>> toListOfListsOfMaps() {
        return lines.stream().map(StyledLine::toListOfMaps).collect(Collectors.toList());
    }

    private void handleStyleOrValue(Object styleOrValue) {
        boolean isStyle = styleOrValue instanceof Color || styleOrValue instanceof FontStyle;
        if (isStyle) {
            if (!currentValues.isEmpty()) {
                flushCurrentValues();
            }

            currentStyles.add(createStyleName(styleOrValue));
        } else {
            currentValues.add(Objects.toString(styleOrValue.toString()));
        }
    }

    private void flushCurrentValues() {
        if (!currentValues.isEmpty()) {
            currentLine.parts.add(new StyledText(new ArrayList<>(currentStyles), String.join("", currentValues)));
        }

        currentStyles.clear();
        currentValues.clear();
    }

    private String createStyleName(Object style) {
        if (style instanceof FontStyle) {
            return createStyleNameFromFontStyle((FontStyle) style);
        }

        if (style instanceof Color) {
            return createStyleNameFromColor((Color) style);
        }

        throw new IllegalStateException();
    }

    private String createStyleNameFromFontStyle(FontStyle style) {
        switch (style) {
            case BOLD:
                return "bold";
            case RESET:
                return "normal";
            default:
                throw new IllegalArgumentException("unsupported style: " + style);
        }
    }

    private String createStyleNameFromColor(Color color) {
        switch (color) {
            case BLACK:
                return "black";
            case RED:
                return "red";
            case GREEN:
                return "green";
            case YELLOW:
                return "yellow";
            case BLUE:
                return "blue";
            case PURPLE:
                return "purple";
            case CYAN:
                return "cyan";
            case WHITE:
                return "white";
            case BACKGROUND_YELLOW:
                return "background-yellow";
            case BACKGROUND_RED:
                return "background-red";
            case BACKGROUND_BLUE:
                return "background-blue";
            case RESET:
                return "reset";
            default:
                throw new IllegalArgumentException("unsupported color: " + color);
        }
    }

    private static class StyledText {
        private final List<String> styles;
        private final String text;

        StyledText(List<String> styles, String text) {
            this.styles = styles;
            this.text = text;
        }

        Map<String, Object> toMap() {
            Map<String, Object> result = new HashMap<>();
            result.put("styles", styles);
            result.put("text", text);
            return result;
        }
    }

    private static class StyledLine {
        private final List<StyledText> parts = new ArrayList<>();

        List<Map<String, Object>> toListOfMaps() {
            return parts.stream().map(StyledText::toMap).collect(Collectors.toList());
        }
    }
}
