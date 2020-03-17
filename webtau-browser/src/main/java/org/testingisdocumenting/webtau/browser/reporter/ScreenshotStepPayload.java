/*
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

package org.testingisdocumenting.webtau.browser.reporter;

import org.testingisdocumenting.webtau.reporter.TestStepPayload;

import java.util.Collections;
import java.util.Map;

public class ScreenshotStepPayload implements TestStepPayload {
    private String base64png;

    ScreenshotStepPayload(String base64png) {
        this.base64png = base64png;
    }

    public String getBase64png() {
        return base64png;
    }

    @Override
    public Map<String, ?> toMap() {
        return Collections.singletonMap("base64png", base64png);
    }
}
