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

package com.twosigma.webtau.cfg;

import com.twosigma.webtau.utils.ServiceLoaderUtils;

import java.util.List;
import java.util.stream.Collectors;

public class WebTauMeta {
    private static final WebTauVersionOverrider versionOverride = discover();

    private static final WebTauMeta INSTANCE = new WebTauMeta();

    private final String version;
    public static String getVersion() {
        return INSTANCE.version;
    }

    private WebTauMeta() {
        version = versionOverride.override(getClass().getPackage().getImplementationVersion());
    }

    private static WebTauVersionOverrider discover() {
        List<WebTauVersionOverrider> providers = ServiceLoaderUtils.load(WebTauVersionOverrider.class);
        if (providers.size() > 1) {
            throw new IllegalStateException("more than one version override is found: " + providers
                    .stream()
                    .map(p -> p.getClass().getCanonicalName())
                    .collect(Collectors.joining(";")));
        }

        return providers.isEmpty() ? new DefaultVersionOverrider() : providers.get(0);
    }

    private static class DefaultVersionOverrider implements WebTauVersionOverrider {
        @Override
        public String override(String originalVersion) {
            return originalVersion;
        }
    }
}
