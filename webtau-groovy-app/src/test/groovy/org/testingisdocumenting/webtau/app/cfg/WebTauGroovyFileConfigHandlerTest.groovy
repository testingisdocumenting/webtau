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

package org.testingisdocumenting.webtau.app.cfg

import org.junit.Test
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.report.ReportGenerators
import org.testingisdocumenting.webtau.reporter.WebTauReport
import org.testingisdocumenting.webtau.reporter.WebTauReportName
import org.testingisdocumenting.webtau.reporter.WebTauTestList

import java.nio.file.Files

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.WebTauCore.*

class WebTauGroovyFileConfigHandlerTest {
    @Test
    void "should use default environment values when env is not specified"() {
        def cfg = createConfig()
        handle(cfg)

        cfg.url.should == 'http://localhost:8180'
    }

    @Test
    void "should use environment specific values when env is specified"() {
        def cfg = createConfig()
        cfg.envConfigValue.set('test', 'dev')

        handle(cfg)

        cfg.list.should == []
        cfg.url.should == 'http://dev.host:8080'
    }

    @Test
    void "should let specify custom reporter"() {
        def cfg = createConfig()
        cfg.envConfigValue.set('test', 'prod')

        handle(cfg)

        cfg.reportPath.toFile().deleteOnExit()

        def customReportPath = cfg.reportPath.toAbsolutePath().parent.resolve('custom-report.txt')
        customReportPath.toFile().deleteOnExit()

        def report = new WebTauReport(new WebTauReportName("my report", ""), new WebTauTestList(), 0, 0)
        ReportGenerators.generate(report)

        Files.exists(customReportPath).should == true
        customReportPath.text.should == 'test report'
    }

    @Test
    void "should validate passed environment presence"() {
        def cfg = createConfig()
        cfg.envConfigValue.set('test', 'unknown')

        code {
            handle(cfg)
        } should throwException(IllegalArgumentException, ~/environment <unknown> is not defined in/)
    }

    @Test
    void "should capture nested config values"() {
        def cfg = createNestedConfig()
        cfg.envConfigValue.set('test', 'prod')
        handle(cfg)

        cfg.get('parent').should == [child: 'pc-1', another: 'pc-2']
        cfg.parent.child.should == 'pc-1'

        cfg.get('additionalUrls').should == [appOne: 'http://app-one-prod', appTwo: 'http://app-two-prod']
    }

    @Test
    void "should allow to override a single value within a nested object for an env"() {
        def cfg = createNestedConfig()
        cfg.envConfigValue.set('test', 'dev')
        handle(cfg)

        cfg.get('additionalUrls').should == [appOne: 'http://app-one-dev', appTwo: 'http://app-two']
    }

    @Test
    void "should allow to define a persona specific value"() {
        def John = persona('John')
        def Bob = persona('Bob')

        def cfg = createPersonaConfig()
        handle(cfg)

        cfg.configValue.should == 'customDefaultValue'

        John {
            cfg.configValue.should == 'JohnCustomValue'
        }

        Bob {
            cfg.configValue.should == 'BobCustomValue'
        }
    }

    @Test
    void "should allow to define a persona specific value per environment"() {
        def John = persona('John')
        def Bob = persona('Bob')

        def cfg = createPersonaConfig()
        cfg.envConfigValue.set('manual', 'dev')
        handle(cfg)

        cfg.configValue.should == 'customDefaultValue-dev'

        John {
            cfg.configValue.should == 'JohnCustomValue-dev'
        }

        Bob {
            cfg.configValue.should == 'BobCustomValue-dev'
        }
    }

    private static void handle(WebTauConfig cfg) {
        def handler = new WebTauGroovyFileConfigHandler()
        handler.onAfterCreate(cfg)
    }

    private static WebTauConfig createConfig() {
        return createConfigFromFile('src/test/resources/webtau.cfg.groovy')
    }

    private static WebTauConfig createNestedConfig() {
        return createConfigFromFile('src/test/resources/webtau-nested.cfg.groovy')
    }

    private static WebTauConfig createPersonaConfig() {
        return createConfigFromFile('src/test/resources/webtau-persona.cfg.groovy')
    }

    private static WebTauConfig createConfigFromFile(String filePath) {
        def cfg = new WebTauConfig()
        cfg.configFileNameValue.set('test', filePath)

        return cfg
    }
}
