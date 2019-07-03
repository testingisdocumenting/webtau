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

package com.example.tests.junit4

import com.example.tests.featuretest.JUnitFeatureTestRunner
import com.twosigma.webtau.cfg.WebTauConfig
import com.twosigma.webtau.featuretesting.WebTauRestFeaturesTestData
import com.twosigma.webtau.http.testserver.TestServer
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class WebTauFeaturesJUnitTest {
    private static final TestServer testServer = new TestServer()

    private static final JUnitFeatureTestRunner testRunner = new JUnitFeatureTestRunner()

    @BeforeClass
    static void startServer() {
        testServer.startRandomPort()
        WebTauRestFeaturesTestData.registerEndPoints(testServer)

        WebTauConfig.cfg.setBaseUrl(testServer.uri.toString())
    }

    @AfterClass
    static void stopServer() {
        testServer.stop()
    }

    @Test
    void weatherJavaTest() {
        testRunner.runAndValidate(WeatherJavaIT)
    }

    @Test
    void weatherGroovyTest() {
        testRunner.runAndValidate(WeatherGroovyIT)
    }
}
