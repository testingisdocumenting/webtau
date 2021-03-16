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

package org.testingisdocumenting.webtau.cli;

import org.testingisdocumenting.webtau.cfg.ConfigValue;
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.cfg.ConfigValue.*;

public class CliConfig implements WebTauConfigHandler {
    private static final ConfigValue cliPath = declare("cliPath", "path items to append to path used for cli runs",
            Collections::emptyList);

    private static final ConfigValue cliTimeout = declare("cliTimeout", "cli foreground command run timeout (ms)",
            () -> 30000L);

    public static List<String> getPath() {
        return cliPath.getAsList();
    }

    public static ConfigValue getCliPathConfigValue() {
        return cliPath;
    }

    public static long getCliTimeoutMs() {
        return cliTimeout.getAsLong();
    }

    public static ConfigValue getCliTimeoutConfigValue() {
        return cliTimeout;
    }

    @Override
    public Stream<ConfigValue> additionalConfigValues() {
        return Stream.of(cliPath, cliTimeout);
    }
}
