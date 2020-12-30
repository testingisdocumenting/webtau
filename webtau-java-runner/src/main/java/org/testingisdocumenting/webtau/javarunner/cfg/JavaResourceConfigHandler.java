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

package org.testingisdocumenting.webtau.javarunner.cfg;

import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler;
import org.testingisdocumenting.webtau.utils.ResourceUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class JavaResourceConfigHandler implements WebTauConfigHandler {
    private static final String CFG_RESOURCE_PATH = "webtau.properties";

    @Override
    public void onBeforeCreate(WebTauConfig cfg) {
    }

    @Override
    public void onAfterCreate(WebTauConfig cfg) {
        if (!ResourceUtils.hasResource(CFG_RESOURCE_PATH)) {
            return;
        }

        try {
            Properties properties = new Properties();
            properties.load(ResourceUtils.resourceStream(CFG_RESOURCE_PATH));

            String environmentPrefix = "environments.";
            String environmentNamePrefix = environmentPrefix + cfg.getEnv() + ".";

            Map<String, Object> asMap = new LinkedHashMap<>();
            properties.forEach((k, v) -> {
                String keyAsString = k.toString();

                if (!keyAsString.startsWith(environmentPrefix)) {
                    asMap.put(keyAsString, v);
                }
            });

            // handle environment specific
            properties.forEach((k, v) -> {
                String keyAsString = k.toString();

                if (keyAsString.startsWith(environmentNamePrefix)) {
                    asMap.put(keyAsString.substring(environmentNamePrefix.length()), v);
                }
            });

            cfg.acceptConfigValues(CFG_RESOURCE_PATH, asMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
