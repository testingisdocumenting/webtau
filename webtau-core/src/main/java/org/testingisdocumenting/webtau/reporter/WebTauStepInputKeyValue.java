/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.reporter;

import org.testingisdocumenting.webtau.console.ConsoleOutput;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.utils.CollectionUtils;

import java.util.Map;

public class WebTauStepInputKeyValue implements WebTauStepInput {
    private final Map<String, Object> data;

    private WebTauStepInputKeyValue(Map<String, Object> data) {
        this.data = data;
    }

    private WebTauStepInputKeyValue(Object... keyValues) {
        this(CollectionUtils.aMapOf(keyValues));
    }

    public static WebTauStepInput stepInput(Map<String, Object> data) {
        return new WebTauStepInputKeyValue(data);
    }

    public static WebTauStepInput stepInput(Object... keyValues) {
        return new WebTauStepInputKeyValue(keyValues);
    }

    @Override
    public void prettyPrint(ConsoleOutput console) {
        data.forEach((key, value) -> console.out(Color.PURPLE, key, Color.WHITE, ": ", valueColor(value), value));
    }

    @Override
    public Map<String, ?> toMap() {
        return data;
    }

    private static Color valueColor(Object value) {
        if (value instanceof CharSequence) {
            return Color.GREEN;
        }

        return Color.CYAN;
    }
}
