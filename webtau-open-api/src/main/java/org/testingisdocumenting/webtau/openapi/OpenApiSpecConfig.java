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

package org.testingisdocumenting.webtau.openapi;

import org.testingisdocumenting.webtau.cfg.ConfigValue;
import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler;

import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.cfg.ConfigValue.declare;

public class OpenApiSpecConfig implements WebTauConfigHandler {
    static final ConfigValue specUrl = declare("openApiSpecUrl",
            "url of OpenAPI 2 spec against which to validate http calls", () -> "");

    static final ConfigValue ignoreAdditionalProperties = declare("openApiIgnoreAdditionalProperties",
            "ignore additional OpenAPI properties ", () -> false);

    private static String fullPathOrUrl;

    static String getSpecFullPathOrUrl() {
        return fullPathOrUrl;
    }

    @Override
    public void onAfterCreate(WebTauConfig cfg) {
        fullPathOrUrl = resolveFullPathOrUrl(cfg, specUrl.getAsString());
        OpenApi.reset();
    }

    @Override
    public Stream<ConfigValue> additionalConfigValues() {
        return Stream.of(specUrl, ignoreAdditionalProperties);
    }

    private static String resolveFullPathOrUrl(WebTauConfig cfg, String configValue) {
        if (configValue.isEmpty()) {
            return "";
        }

        if (configValue.startsWith("http:") || configValue.startsWith("https:")) {
            return configValue;
        }

        return cfg.getWorkingDir().resolve(specUrl.getAsString()).toString();
    }
}
