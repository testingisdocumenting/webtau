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
    private static final cfg = WebTauConfig.INSTANCE

    @Test
    void "should use default environment values when evn is not specified"() {
        def cliConfig = new WebTauTestCliConfig("--config=src/test/resources/test.cfg")
        cliConfig.parseConfig(groovy)

        cfg.baseUrl.should == "http://localhost:8180"
    }

    @Test
    void "should use environment specific values when evn is specified"() {
        def cliConfig = new WebTauTestCliConfig("--config=src/test/resources/test.cfg", "--env=dev")
        cliConfig.parseConfig(groovy)

        cfg.baseUrl.should == "http://dev.host:8080"
    }
}
