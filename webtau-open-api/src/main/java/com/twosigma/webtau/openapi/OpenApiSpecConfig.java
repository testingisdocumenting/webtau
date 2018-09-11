/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.openapi;

import com.twosigma.webtau.cfg.ConfigValue;
import com.twosigma.webtau.cfg.WebTauConfigHandler;

import java.util.stream.Stream;

import static com.twosigma.webtau.cfg.ConfigValue.declare;
import static com.twosigma.webtau.cfg.WebTauConfig.getCfg;

public class OpenApiSpecConfig implements WebTauConfigHandler {
    static final ConfigValue specUrl = declare("openApiSpecUrl",
            "url of OpenAPI 2 spec against which to validate http calls", () -> "");

    static final ConfigValue ignoreAdditionalProperties = declare("openApiIgnoreAdditionalProperties",
            "ignore additional OpenAPI properties ", () -> false);

    public static String specFullPath() {
        if (specUrl.getAsString().isEmpty()) {
            return "";
        }

        return getCfg().getWorkingDir()
                .resolve(specUrl.getAsString())
                .toString();
    }

    @Override
    public Stream<ConfigValue> additionalConfigValues() {
        return Stream.of(specUrl, ignoreAdditionalProperties);
    }
}
