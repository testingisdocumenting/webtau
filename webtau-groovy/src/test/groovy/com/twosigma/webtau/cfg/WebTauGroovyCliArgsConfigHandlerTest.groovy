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

class WebTauGroovyCliArgsConfigHandlerTest {
    @Test
    void "sets file config related values during first run and overrides other cfg values during second"() {
        def handler = new WebTauGroovyCliArgsConfigHandler('--env=dev', '--workingDir=/root/a',
            '--config=abc.cfg', '--url=http://localhost:3434', "test.groovy")

        def cfg = createConfig()

        handler.onAfterCreate(cfg)

        cfg.env.should == 'dev'
        cfg.workingDir.toString().should == '/root/a'
        cfg.configFileName.asString.should == 'abc.cfg'
        cfg.baseUrl.should == ""

        handler.onAfterCreate(cfg)

        cfg.baseUrl.should == 'http://localhost:3434'
    }

    private static WebTauConfig createConfig() {
        return new WebTauConfig()
    }
}
