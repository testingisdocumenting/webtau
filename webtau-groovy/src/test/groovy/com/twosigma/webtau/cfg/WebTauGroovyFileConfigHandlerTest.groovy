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

package com.twosigma.webtau.cfg

import org.junit.Test

class WebTauGroovyFileConfigHandlerTest {
    @Test
    void "should use default environment values when env is not specified"() {
        def cfg = createConfig()

        def handler = new WebTauGroovyFileConfigHandler()
        handler.onAfterCreate(cfg)

        cfg.baseUrl.should == "http://localhost:8180"
    }

    @Test
    void "should use environment specific values when env is specified"() {
        def cfg = createConfig()
        cfg.envConfigValue.set("test", "dev")

        def handler = new WebTauGroovyFileConfigHandler()
        handler.onAfterCreate(cfg)

        cfg.baseUrl.should == "http://dev.host:8080"
    }

    private static WebTauConfig createConfig() {
        def cfg = new WebTauConfig()
        cfg.configFileName.set("test", "src/test/resources/webtau.cfg")

        return cfg
    }
}
