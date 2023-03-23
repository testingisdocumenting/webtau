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

package org.testingisdocumenting.webtau.console.ansi;

import java.util.stream.Stream;

public class IgnoreAnsiString {
    private final StringBuilder stringBuilder;

    public IgnoreAnsiString(Stream<?> styleOrValues) {
        this.stringBuilder = new StringBuilder();
        styleOrValues.forEach(this::append);
    }

    public IgnoreAnsiString(Object... styleOrValues) {
        this(Stream.of(styleOrValues));
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    private void append(Object styleOrValue) {
        if (styleOrValue instanceof Color || styleOrValue instanceof FontStyle) {
            return;
        }

        if (styleOrValue == null) {
            stringBuilder.append("null");
            return;
        }

        String result = styleOrValue.toString();
        for (Color value : Color.values()) {
            result = result.replace(value.toString(), "");
        }
        for (FontStyle value : FontStyle.values()) {
            result = result.replace(value.toString(), "");
        }

        stringBuilder.append(result);
    }
}
