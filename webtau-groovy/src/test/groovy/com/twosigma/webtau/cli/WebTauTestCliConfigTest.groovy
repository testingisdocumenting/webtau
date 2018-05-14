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

package com.twosigma.webtau.cli

import com.twosigma.webtau.runner.standalone.GroovyStandaloneEngine
import com.twosigma.webtau.cfg.WebTauConfig
import org.junit.Test

import java.nio.file.Paths

class WebTauTestCliConfigTest {
    private static def groovy = GroovyStandaloneEngine.createWithoutDelegating(Paths.get(""), [])
    private final cfg = new WebTauConfig()

    @Test
    void "should use default environment values when env is not specified"() {
        def cliConfig = new WebTauTestCliConfig(cfg, "--config=src/test/resources/test.cfg", "testFile")
        cliConfig.parseConfig(groovy)

        cfg.baseUrl.should == "http://localhost:8180"
    }

    @Test
    void "should use environment specific values when env is specified"() {
        def cliConfig = new WebTauTestCliConfig(cfg, "--config=src/test/resources/test.cfg", "--env=dev", "testFile")
        cliConfig.parseConfig(groovy)

        cfg.baseUrl.should == "http://dev.host:8080"
    }

    @Test
    void "should exit if only config file provided"() {
        Integer retCode
        new WebTauTestCliConfig(cfg, { retCode = it }, "--config=src/test/resources/test.cfg", "--env=dev")

        retCode.should == 1
    }

    @Test
    void "should set command line args source when env is specified"() {
        def cliConfig = new WebTauTestCliConfig(cfg, "--config=src/test/resources/test.cfg", "--env=dev", "testFile")
        cliConfig.parseConfig(groovy)

        cfg.envConfigValue.source.should == "command line argument"
    }

    @Test
    void "should set default source when env is not specified"() {
        def cliConfig = new WebTauTestCliConfig(cfg, "--config=src/test/resources/test.cfg", "testFile")
        cliConfig.parseConfig(groovy)

        cfg.envConfigValue.source.should == "default"
    }

    @Test
    void "should set default source and value when cfg is not specified"() {
        def cliConfig = new WebTauTestCliConfig(cfg, "testFile")
        cliConfig.parseConfig(groovy)

        cfg.configFileName.asString.should == "test.cfg"
        cfg.configFileName.source.should == "default"
    }
}
