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

package com.twosigma.webtau.featuretesting

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import static com.twosigma.webtau.cfg.WebTauConfig.cfg

class WebTauConceptFeaturesTest {
    private static WebTauEndToEndTestRunner testRunner

    @BeforeClass
    static void init() {
        testRunner = new WebTauEndToEndTestRunner()
        testRunner.captureMeta = true
    }

    @Before
    void before() {
        cfg.envConfigValue.set('test', 'local')
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
    void "run selected tests only"() {
        runCli('runOnlySelected.groovy', 'webtau.cfg')
    }

    @Test
    void "disable tests"() {
        runCli('skipTests.groovy', 'webtau.cfg')
    }

    @Test
    void "conditional tests custom condition skip"() {
        runCli('conditionalCustomRegistrationSkip.groovy', 'webtau.cfg')
    }

    @Test
    void "conditional tests based on env registration skip"() {
        runCli('conditionalEnvRegistrationSkip.groovy', 'webtau.cfg')
    }

    @Test
    void "conditional tests based on env registration run"() {
        runCli('conditionalEnvRegistrationRun.groovy', 'experimental.cfg')
    }

    @Test
    void "custom test meta data driven by raw key value"() {
        runCli('metaDataRaw.groovy', 'webtau.cfg')
    }

    @Test
    void "custom test meta data driven by method call"() {
        runCli('metaDataMethodBased.groovy', 'webtau.cfg')
    }

    @Test // second similar test is to make sure we clean current test meta in between tests
    void "custom test meta data driven by method sanity check"() {
        runCli('metaDataMethodBasedSanityCheck.groovy', 'webtau.cfg')
    }

    private static void runCli(String testName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/concept/$testName",
                "scenarios/concept/$configFileName", additionalArgs)
    }
}
