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

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString

class WebTauConfigTest implements ConsoleOutput {
    List<String> consoleOut = []

    @Before
    void initiateStepReporter() {
        consoleOut.clear()
        ConsoleOutputs.add(this)
    }

    @After
    void unregisterStepReporter() {
        ConsoleOutputs.remove(this)
    }

    @Test
    void "inits config values from env vars and overrides them from system properties"() {
        System.setProperty('url', 'test-base-url')
        WebTauConfig cfg = new WebTauConfig()
        cfg.getBaseUrl().should == 'test-base-url'
    }

    @Test
    void "allows to manually override base url"() {
        System.setProperty('url', 'original-base-url')

        WebTauConfig cfg = new WebTauConfig()
        cfg.setBaseUrl('new-url')

        cfg.getBaseUrl().should == "new-url"
        cfg.baseUrlConfigValue.getSources().should == ["manual", "system property"]
    }

    @Test
    void "automatically registers user defined config values"() {
        System.setProperty('userDefined', 'user-1')
        WebTauConfig cfg = new WebTauConfig()

        cfg.get('userDefined').should == 'user-1'
    }

    @Test
    void "let register additional config values via service loaders"() {
        WebTauConfig cfg = new WebTauConfig()
        cfg.get('customConfig').should == 'default config value'
    }

    @Test
    void "convert uppercase underscore to property name"() {
        WebTauConfig.convertToCamelCase('WEBTAU_NAME').should == 'name'
        WebTauConfig.convertToCamelCase('WEBTAU_PROP_NAME').should == 'propName'
    }

    @Test
    void "setting url should have step report url and source"() {
        WebTauConfig cfg = new WebTauConfig()
        cfg.setBaseUrl("test-source", "http://test")

        def output = consoleOut.join("\n")
        output = output.replaceAll("\\(\\d+ms\\)", "(Xms)")

        output.should == "> setting url\n" +
                "    source: test-source\n" +
                "    url: http://test\n" +
                ". set url (Xms)"
    }

    @Override
    void out(Object... styleOrValues) {
        consoleOut.add(new IgnoreAnsiString(styleOrValues).toString())
    }

    @Override
    void err(Object... styleOrValues) {

    }
}
