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

package org.testingisdocumenting.webtau.report;

import org.testingisdocumenting.webtau.utils.ResourceUtils;

class ReactJsBundle {
    private static final String JS_BUNDLE_PATH = "static/js/webtau-report.js";
    private static final String CSS_BUNDLE_PATH = "static/css/webtau-report.css";

    private final String javaScript;
    private final String css;

    public ReactJsBundle() {
        this.javaScript = ResourceUtils.textContent(JS_BUNDLE_PATH);
        this.css = ResourceUtils.textContent(CSS_BUNDLE_PATH);
    }

    public String getJavaScript() {
        return javaScript;
    }

    public String getCss() {
        return css;
    }
}
