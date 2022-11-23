/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.http.config;

import org.testingisdocumenting.webtau.cfg.ConfigValue;
import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler;

import java.nio.file.Path;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.cfg.ConfigValue.*;

public class HttpConfig implements WebTauConfigHandler {
    private static final ConfigValue httpRoutesPath = declare("httpRoutesPath", "path to a file with operations in a format METHOD route",
            () -> WebTauConfig.getCfg().fullPath("http-operations.txt"));

    @Override
    public Stream<ConfigValue> additionalConfigValues() {
        return Stream.of(httpRoutesPath);
    }

    public static Path getTextOperationsPath() {
        return WebTauConfig.getCfg().fullPath(httpRoutesPath.getAsPath());
    }
}
