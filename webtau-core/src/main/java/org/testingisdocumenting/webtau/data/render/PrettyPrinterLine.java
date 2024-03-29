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

package org.testingisdocumenting.webtau.data.render;

import org.testingisdocumenting.webtau.console.ansi.AnsiConsoleUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrettyPrinterLine {
    private int width = 0;

    private final List<Object> styleAndValues = new ArrayList<>();

    public void appendStream(Stream<?> styleAndValues) {
        List<?> asList = styleAndValues.collect(Collectors.toList());
        this.styleAndValues.addAll(asList);

        updateWidth(asList);
    }

    public void append(Object... styleAndValues) {
        appendStream(Arrays.stream(styleAndValues));
    }

    public int getWidth() {
        return width;
    }

    public boolean hasNewLine() {
        return styleAndValues.contains("\n");
    }

    public int findWidthOfTheLastEffectiveLine() {
        int lastIndexOfNewLine = styleAndValues.lastIndexOf("\n");
        if (lastIndexOfNewLine == -1) {
            return width;
        }

        return AnsiConsoleUtils.calcEffectiveWidth(styleAndValues.subList(lastIndexOfNewLine + 1, styleAndValues.size()));
    }

    public void clear() {
        styleAndValues.clear();
    }

    public boolean isEmpty() {
        return styleAndValues.isEmpty();
    }

    public List<Object> getStyleAndValues() {
        return styleAndValues;
    }

    private void updateWidth(List<?> styleAndValues) {
        width += AnsiConsoleUtils.calcEffectiveWidth(styleAndValues);
    }
}
