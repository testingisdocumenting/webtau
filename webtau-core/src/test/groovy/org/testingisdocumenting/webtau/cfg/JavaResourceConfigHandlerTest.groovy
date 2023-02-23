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

package org.testingisdocumenting.webtau.cfg

import org.junit.After
import org.junit.Before
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.actual
import static org.testingisdocumenting.webtau.Matchers.equal

class JavaResourceConfigHandlerTest {
    def cfg = WebTauConfig.cfg

    @Before
    @After
    void init() {
        System.setProperty("webtau.properties", "webtau.properties")
        cfg.reset()
    }

    @Test
    void "should merge-in environment specific values"() {
        def handler = new JavaResourceConfigHandler()
        cfg.envConfigValue.set("test", "qa")

        handler.onAfterCreate(cfg)

        actual(cfg.baseUrl).should(equal('http://server.qa:8080'))
    }

    @Test
    void "should allow to override path for config"() {
        System.setProperty("webtau.properties", "webtau.override.properties")
        cfg.triggerConfigHandlers()

        actual(cfg.baseUrl).should(equal("http://local-override"))
    }
}
