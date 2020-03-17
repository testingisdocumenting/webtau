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

package org.testingisdocumenting.webtau.featuretesting

import org.junit.BeforeClass
import org.junit.Test

import static org.testingisdocumenting.webtau.cli.CliTestUtils.supportedPlatformOnly

class WebTauCliFeaturesTest {
    private static WebTauEndToEndTestRunner testRunner

    @BeforeClass
    static void init() {
        testRunner = new WebTauEndToEndTestRunner()
    }

    @Test
    void "simple script run"() {
        supportedPlatformOnly {
            runCli('simpleRun.groovy', 'webtau.cfg')
        }
    }

    @Test
    void "path based script run"() {
        supportedPlatformOnly {
            runCli('pathBasedScript.groovy', 'webtau-with-path.cfg')
        }
    }

    @Test
    void "error script run"() {
        supportedPlatformOnly {
            runCli('errorRuns.groovy', 'webtau.cfg')
        }
    }

    private static void runCli(String restTestName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/cli/$restTestName",
                "scenarios/cli/$configFileName", additionalArgs)
    }
}
