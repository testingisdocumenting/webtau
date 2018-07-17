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

package com.twosigma.webtau.http.report

import com.twosigma.webtau.report.ReportDataProviders
import com.twosigma.webtau.report.ReportTestEntries
import com.twosigma.webtau.report.ReportTestEntry
import com.twosigma.webtau.reporter.TestResultPayload
import org.junit.Test

class HttpCallsReportDataProviderTest {
    @Test
    void "should extract http calls from tests and provide as custom report"() {
        def testA = new ReportTestEntry()
        testA.addTestResultPayload(new TestResultPayload('httpCalls', [[
                method: 'GET',
                url: '/url',
                elapsedTime: 200
        ]]))

        def testB = new ReportTestEntry()
        testB.addTestResultPayload(new TestResultPayload('httpCalls', [[
                method: 'PUT',
                url: '/risk',
                elapsedTime: 320
        ]]))

        def testEntries = new ReportTestEntries()
        testEntries.add(testA)
        testEntries.add(testB)

        def httpCalls = ReportDataProviders.provide(testEntries).find { it.id == 'httpCalls'}
        httpCalls.data.should == ['method' | 'url'   | 'elapsedTime'] {
                                  ___________________________________
                                   'GET'   | '/url'  | 200
                                   'PUT'   | '/risk' | 320   }
    }
}
