/*
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

package org.testingisdocumenting.webtau.http.report

import org.testingisdocumenting.webtau.report.ReportDataProviders
import org.testingisdocumenting.webtau.reporter.WebTauReportLog
import org.testingisdocumenting.webtau.reporter.WebTauTestList
import org.testingisdocumenting.webtau.reporter.WebTauTest
import org.testingisdocumenting.webtau.reporter.TestResultPayload
import org.junit.Test

class HttpCallsReportDataProviderTest {
    @Test
    void "should extract http calls from tests and provide as custom report"() {
        def testA = new WebTauTest()
        testA.setId("testA")
        testA.addTestResultPayload(new TestResultPayload('httpCalls', [[
                method: 'GET',
                url: '/url',
                elapsedTime: 200
        ]]))

        def testB = new WebTauTest()
        testB.setId("testB")
        testB.addTestResultPayload(new TestResultPayload('httpCalls', [[
                method: 'PUT',
                url: '/risk',
                elapsedTime: 320
        ]]))

        def tests = new WebTauTestList()
        tests.add(testA)
        tests.add(testB)

        def httpCalls = ReportDataProviders.provide(tests, new WebTauReportLog()).find { it.id == 'httpCalls'}
        httpCalls.data.should == ['method' | 'url'   | 'elapsedTime'] {
                                  ___________________________________
                                   'GET'   | '/url'  | 200
                                   'PUT'   | '/risk' | 320   }
    }
}
