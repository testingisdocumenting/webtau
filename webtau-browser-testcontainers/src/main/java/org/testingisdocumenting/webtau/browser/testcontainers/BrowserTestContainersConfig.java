/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.testcontainers;

import org.testingisdocumenting.webtau.cfg.ConfigValue;
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler;

import java.util.stream.Stream;

public class BrowserTestContainersConfig implements WebTauConfigHandler {
    private static final ConfigValue browserTestcontainersEnabled = ConfigValue.declare("browserTestcontainersEnabled",
            "pass true to get connection to web drivers using Test Containers", () -> false);

    public static boolean isEnabled() {
        return browserTestcontainersEnabled.getAsBoolean();
    }

    @Override
    public Stream<ConfigValue> additionalConfigValues() {
        return Stream.of(browserTestcontainersEnabled);
    }
}
