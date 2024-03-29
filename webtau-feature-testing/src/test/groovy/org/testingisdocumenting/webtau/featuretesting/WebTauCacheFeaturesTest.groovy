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

package org.testingisdocumenting.webtau.featuretesting

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.webtau.http.testserver.FixedResponsesHandler

class WebTauCacheFeaturesTest {
    private static WebTauEndToEndTestRunner testRunner

    @BeforeClass
    static void init() {
        FixedResponsesHandler handler = new FixedResponsesHandler()
        testRunner = new WebTauEndToEndTestRunner(handler)

        WebTauRestFeaturesTestData.registerEndPoints(testRunner.testServer, handler)

        testRunner.startTestServer()
    }

    @AfterClass
    static void cleanup() {
        testRunner.stopTestServer()
    }

    @Test
    void "cached value"() {
        runCli('cachedValue.groovy', 'webtau.cfg.groovy', "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "cached path value"() {
        runCli('cachedPathValue.groovy', 'webtau.cfg.groovy', "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "cached value with expiration"() {
        runCli('cachedExpirationValue.groovy', 'webtau.cfg.groovy', "--url=${testRunner.testServer.uri}")
    }

    private static void runCli(String testName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/cache/$testName",
                configFileName.isEmpty() ? "" : "scenarios/cache/$configFileName", additionalArgs)
    }
}
