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

package org.testingisdocumenting.webtau.app.cfg

import org.testingisdocumenting.webtau.cfg.ConfigValue
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler

import java.util.stream.Stream

class WebTauNumberOfThreadsConfigHandler implements WebTauConfigHandler {
    private static final ConfigValue numberOfThreads = ConfigValue.declare("numberOfThreads",
            "number of threads on which to run test files (one file per thread), -1 will use as many threads as there are files",
            { -> 1 })

    @Override
    Stream<ConfigValue> additionalConfigValues() {
        return [numberOfThreads].stream()
    }

    static int getNumberOfThreads() {
        return numberOfThreads.getAsInt()
    }
}
