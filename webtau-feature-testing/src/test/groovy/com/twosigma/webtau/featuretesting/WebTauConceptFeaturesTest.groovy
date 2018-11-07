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

package com.twosigma.webtau.featuretesting

import org.junit.BeforeClass
import org.junit.Test

class WebTauConceptFeaturesTest {
    private static WebTauTestRunner testRunner

    @BeforeClass
    static void init() {
        testRunner = new WebTauTestRunner()
    }

    @Test
    void "simple dynamic scenario"() {
        runCli('simpleDynamicScenario.groovy', 'webtau.cfg')
    }

    @Test
    void "data driven scenarios from csv"() {
        runCli('dataDrivenCsv.groovy', 'webtau.cfg')
    }

    @Test
    void "data driven scenarios from table"() {
        runCli('dataDrivenTableData.groovy', 'webtau.cfg')
    }

    @Test
    void "hard tests termination"() {
        runCli('testsTermination.groovy', 'webtau.cfg')
    }

    @Test
    void "conditional tests registration skip"() {
        runCli('conditionalRegistrationSkip.groovy', 'webtau.cfg')
    }

    @Test
    void "conditional tests registration run"() {
        runCli('conditionalRegistrationRun.groovy', 'experimental.cfg')
    }

    private static void runCli(String testName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/concept/$testName",
                "scenarios/concept/$configFileName", additionalArgs)
    }
}
