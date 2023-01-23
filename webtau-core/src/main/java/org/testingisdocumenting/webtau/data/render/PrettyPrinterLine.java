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

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.console.ansi.FontStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrettyPrinterLine {
    private int width = 0;

    private final List<Object> styleAndValues = new ArrayList<>();

    public void prepend(Object... styleAndValues) {
        this.styleAndValues.addAll(0, Arrays.asList(styleAndValues));
        updateWidth(styleAndValues);
    }

    public void append(Object... styleAndValues) {
        this.styleAndValues.addAll(Arrays.asList(styleAndValues));
        updateWidth(styleAndValues);
    }

    public int getWidth() {
        return width;
    }

    public List<Object> getStyleAndValues() {
        return styleAndValues;
    }

    private void updateWidth(Object... styleAndValues) {
        for (Object styleOrValue : styleAndValues) {
            if (styleOrValue instanceof Color || styleOrValue instanceof FontStyle) {
                continue;
            }

            width += styleOrValue.toString().length();
        }
    }
}
