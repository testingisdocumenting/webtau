/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.cfg

import org.testingisdocumenting.webtau.runner.standalone.GroovyStandaloneEngine
import org.junit.Test

import java.nio.file.Paths

class WebTauCliArgsConfigTest {
    private static def groovy = GroovyStandaloneEngine.createWithoutDelegating(Paths.get(""), [])
    private final cfg = new WebTauConfig()

    @Test
    void "should exit if only config file provided"() {
        Integer retCode = 0
        def config = new WebTauCliArgsConfig(cfg, { retCode = it },
            "--config=src/test/resources/webtau.groovy", "--env=dev")

        retCode.should == 1
    }

    @Test
    void "should set command line args source when env is specified"() {
        def cliConfig = new WebTauCliArgsConfig(cfg, "--config=src/test/resources/webtau.groovy", "--env=dev", "testFile")
        cliConfig.setConfigFileRelatedCfgIfPresent()

        cfg.envConfigValue.source.should == "command line argument"
    }

    @Test
    void "should set default source when env is not specified"() {
        def cliConfig = new WebTauCliArgsConfig(cfg, "--config=src/test/resources/webtau.groovy", "testFile")
        cfg.envConfigValue.source.should == "default"
    }

    @Test
    void "should set default source and value when cfg is not specified"() {
        def cliConfig = new WebTauCliArgsConfig(cfg, "testFile")

        cfg.configFileNameValue.asString.should == "webtau.groovy"
        cfg.configFileNameValue.source.should == "default"
    }
}
