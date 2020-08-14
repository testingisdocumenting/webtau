/*
 * Copyright 2020 webtau maintainers
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

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.cfg

class WebTauDbFeaturesTest {
    private static WebTauEndToEndTestRunner testRunner

    @BeforeClass
    static void init() {
        testRunner = new WebTauEndToEndTestRunner()
    }

    @Before
    void before() {
        cfg.envConfigValue.set('test', 'local')
    }

    @Test
    void "db provider from config"() {
        runCli('dbProviderThroughConfig.groovy', 'webtauDbProvider.groovy')
    }

    private static void runCli(String testName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/db/$testName",
                "scenarios/db/$configFileName", additionalArgs)
    }
}
