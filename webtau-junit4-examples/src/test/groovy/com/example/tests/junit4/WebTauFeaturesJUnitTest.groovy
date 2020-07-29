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

package com.example.tests.junit4

import com.example.tests.featuretest.JUnitFeatureTestRunner
import org.testingisdocumenting.webtau.featuretesting.WebTauRestFeaturesTestData
import org.testingisdocumenting.webtau.http.testserver.FixedResponsesHandler
import org.testingisdocumenting.webtau.http.testserver.TestServer
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class WebTauFeaturesJUnitTest {
    private static final FixedResponsesHandler handler = new FixedResponsesHandler()
    private static final TestServer testServer = new TestServer(handler)

    private static final JUnitFeatureTestRunner testRunner = new JUnitFeatureTestRunner()

    private static final TODO_BASE_URL = 'https://jsonplaceholder.typicode.com/'

    @BeforeClass
    static void startServer() {
        testServer.startRandomPort()
        WebTauRestFeaturesTestData.registerEndPoints(testServer, handler)
    }

    @AfterClass
    static void stopServer() {
        testServer.stop()
    }

    @Test
    void weatherJavaTest() {
        testRunner.runAndValidate(WeatherJavaIT, testServer.uri.toString())
    }

    @Test
    void weatherGroovyTest() {
        testRunner.runAndValidate(WeatherGroovyIT, testServer.uri.toString())
    }

    @Test
    void todoListJavaTest() {
        testRunner.runAndValidate(TodoListJavaIT, TODO_BASE_URL)
    }

    @Test
    void todoListGroovyTest() {
        testRunner.runAndValidate(TodoListGroovyIT, TODO_BASE_URL)
    }
}
