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
import org.testingisdocumenting.webtau.utils.UrlUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.cfg.ConfigValue.declare;
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;

public class OpenApiSpecConfig implements WebTauConfigHandler {
    static final ConfigValue specUrl = declare("openApiSpecUrl",
            "url of OpenAPI 2 spec against which to validate http calls", () -> "");

    static final ConfigValue ignoreAdditionalProperties = declare("openApiIgnoreAdditionalProperties",
            "ignore additional OpenAPI properties ", () -> false);

    static OpenApiSpecLocation determineSpecFullPathOrUrl() {
        return resolveFullPathOrUrl();
    }

    @Override
    public void onAfterCreate(WebTauConfig cfg) {
        OpenApi.reset();
    }

    @Override
    public Stream<ConfigValue> additionalConfigValues() {
        return Stream.of(specUrl, ignoreAdditionalProperties);
    }

    private static OpenApiSpecLocation resolveFullPathOrUrl() {
        String configValue = specUrl.getAsString();

        if (configValue.isEmpty()) {
            return OpenApiSpecLocation.undefined();
        }

        if (configValue.startsWith("/")) {
            if (Files.exists(Paths.get(configValue))) {
                return OpenApiSpecLocation.fromFs(Paths.get(configValue));
            }

            return OpenApiSpecLocation.fromUrl(UrlUtils.concat(getCfg().getBaseUrl(), configValue));
        }

        if (UrlUtils.isFull(configValue)) {
            return OpenApiSpecLocation.fromUrl(configValue);
        }

        return OpenApiSpecLocation.fromFs(getCfg().getWorkingDir().resolve(specUrl.getAsString()));
    }
}
