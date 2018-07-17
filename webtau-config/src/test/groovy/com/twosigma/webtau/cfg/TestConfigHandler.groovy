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

package com.twosigma.webtau.cfg

import java.util.stream.Stream

import static com.twosigma.webtau.cfg.ConfigValue.declare

class TestConfigHandler implements WebTauConfigHandler {
    private static final ConfigValue customConfig = declare('customConfig', 'custom config description',
        { -> 'default config value' });

    @Override
    Stream<ConfigValue> additionalConfigValues() {
        return [customConfig].stream()
    }
}
