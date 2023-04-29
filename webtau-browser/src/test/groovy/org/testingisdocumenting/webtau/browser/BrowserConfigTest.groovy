/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.browser

import org.junit.Test
import org.testingisdocumenting.webtau.cfg.WebTauConfig

import static org.testingisdocumenting.webtau.browser.Browser.browser

class BrowserConfigTest {
    @Test
    void "base url port"() {
        WebTauConfig.getCfg().setUrl("http://localhost:8903")
        BrowserConfig.getBaseUrlPort().should == 8903
    }

    @Test
    void "shortcut through browser module"() {
        WebTauConfig.getCfg().setUrl("http://localhost:8903")
        // on separate lines for docs extraction
        // base-url
        browser.getBaseUrl()
        // base-url
                    .should == "http://localhost:8903"
        // base-port
        browser.getBaseUrlPort()
        // base-port
                .should == 8903
    }
}
