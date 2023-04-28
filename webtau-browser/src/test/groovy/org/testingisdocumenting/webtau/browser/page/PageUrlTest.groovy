/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.browser.page

import org.junit.Test
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.reporter.StepReporters
import org.testingisdocumenting.webtau.utils.ResourceUtils

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*

class PageUrlTest {
    private static def browser = [
            url: new PageUrl({ -> sampleUrl() })]

    static String sampleUrl() {
        return ResourceUtils.textContent('sampleBrowserPageUrl.txt')
    }

//    @Test
//    void "should expose url parts with should"() {
//        browser.url.path.should == '/resource/id'
//        browser.url.path.should contain('/id')
//        browser.url.query.should == 'type=full&debug=true'
//        browser.url.ref.should == 'subId'
//    }
//
//    @Test
//    void "should expose url parts with wait"() {
//        browser.url.path.waitTo == '/resource/id'
//        browser.url.query.waitTo == 'type=full&debug=true'
//        browser.url.ref.waitTo == 'subId'
//    }
//
//    @Test
//    void "should expose full url"() {
//        browser.url.full.should == 'http://example.com/resource/id?type=full&debug=true#subId'
//    }
//
//    @Test
//    void "full part should be optional and default during assertion"() {
//        browser.url.should == 'http://example.com/resource/id?type=full&debug=true#subId'
//        browser.url.should contain('resource/id?type=')
//    }
//
//    @Test
//    void "shouldNot should work with url and its parts"() {
//        browser.url.full.shouldNot contain('resource-a/id?type=')
//        browser.url.shouldNot contain('resource-a/id?type=')
//    }
//
//    @Test
//    void "simple underlying value should be exposed for if-else"() {
//        browser.url.path.get().class.should == String
//        browser.url.path.get().should == '/resource/id'
//    }
//
    @Test
    void "should provide context of the failure in case of failed should statement"() {
        println "#####################"

        ConsoleOutputs.add(ConsoleOutputs.defaultOutput);
        StepReporters.add(StepReporters.defaultStepReporter);
//        runExpectExceptionAndValidateOutput(AssertionError, "> expecting page url query of browser to equal \"wrong-value\"\n" +
//                "X failed expecting page url query of browser to equal \"wrong-value\":\n" +
//                "    page url query:  actual: \"type=full&debug=true\" <java.lang.String>\n" +
//                "                   expected: \"wrong-value\" <java.lang.String>\n" +
//                "                              ^ (Xms)\n" +
//                "  \n" +
//                "  browser page url query: \"type=full&debug=true\"") {
        try {
            WebTauConfig.getCfg().setVerbosityLevel(1000)

            browser.url.query.should == 'wrong-value'
        } finally {
            StepReporters.remove(StepReporters.defaultStepReporter);
            ConsoleOutputs.remove(ConsoleOutputs.defaultOutput);
        }
//        }

    }

//    @Test
//    void "should re-fetch url every time"() {
//        def idx = 0
//        def urls = [
//                'http://example.com/resource/id1',
//                'http://example.com/resource/id2',
//                'http://example.com/resource/id3',
//        ]
//
//        def dynamicUrl = new PageUrl({ -> urls[idx++]})
//        dynamicUrl.path.should == '/resource/id1'
//        dynamicUrl.path.should == '/resource/id2'
//        dynamicUrl.path.should == '/resource/id3'
//    }
}
