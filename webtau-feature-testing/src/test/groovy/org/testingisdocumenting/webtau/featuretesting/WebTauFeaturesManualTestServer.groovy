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

package org.testingisdocumenting.webtau.featuretesting

import org.testingisdocumenting.webtau.http.testserver.FixedResponsesHandler
import org.testingisdocumenting.webtau.http.testserver.TestServer

/**
 * Test server for manual run and ad-hoc test scripts execution
 */
class WebTauFeaturesManualTestServer {
    private TestServer testServer

    WebTauFeaturesManualTestServer() {
        FixedResponsesHandler handler = new FixedResponsesHandler()

        testServer = new TestServer(handler)
        WebTauRestFeaturesTestData.registerEndPoints(testServer, handler)
        WebTauBrowserFeaturesTest.registerEndPoints(handler)
    }

    void start(int port) {
        testServer.start(port)
    }

    void stop() {
        testServer.stop()
    }

    static void main(String[] args) {
        def testServer = new WebTauFeaturesManualTestServer()
        testServer.start(8180)
    }
}
