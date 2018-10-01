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

package com.twosigma.webtau.javarunner.cfg;

import com.twosigma.webtau.cfg.WebTauConfig;
import com.twosigma.webtau.cfg.WebTauConfigHandler;
import com.twosigma.webtau.utils.ResourceUtils;

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

            Map<String, String> asMap = new LinkedHashMap<>();
            properties.forEach((k, v) -> asMap.put(k.toString(), v.toString()));

            cfg.acceptConfigValues(CFG_RESOURCE_PATH, asMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
