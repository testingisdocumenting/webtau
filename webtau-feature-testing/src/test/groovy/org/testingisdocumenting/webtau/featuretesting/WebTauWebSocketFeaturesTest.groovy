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

package org.testingisdocumenting.webtau.featuretesting

import org.junit.BeforeClass
import org.junit.Test

class WebTauWebSocketFeaturesTest {
    private static WebTauEndToEndTestRunner testRunner

    @BeforeClass
    static void init() {
        testRunner = new WebTauEndToEndTestRunner()
    }

    @Test
    void "standard operations"() {
        runCli("springBoot.groovy", "webtau.socket.cfg.groovy",
                "--url=${SpringBootDemoAppUrl.baseUrl}")

    }

    private static void runCli(String testName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/websocket/$testName",
                configFileName.isEmpty() ? "" : "scenarios/websocket/$configFileName", additionalArgs)
    }
}
