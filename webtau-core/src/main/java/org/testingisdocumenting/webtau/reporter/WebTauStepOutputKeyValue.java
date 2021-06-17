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
import org.testingisdocumenting.webtau.utils.CollectionUtils;

import java.util.Map;

public class WebTauStepOutputKeyValue implements WebTauStepOutput {
    private final Map<String, Object> data;

    private WebTauStepOutputKeyValue(Map<String, Object> data) {
        this.data = data;
    }

    public static WebTauStepOutput stepOutput(Map<String, Object> data) {
        return new WebTauStepOutputKeyValue(data);
    }

    public static WebTauStepOutput stepOutput(Object... keyValues) {
        return new WebTauStepOutputKeyValue(CollectionUtils.aMapOf(keyValues));
    }

    @Override
    public void prettyPrint(ConsoleOutput console) {
        WebTauStepKeyValue.prettyPrint(console, data);
    }

    @Override
    public Map<String, ?> toMap() {
        return data;
    }
}
