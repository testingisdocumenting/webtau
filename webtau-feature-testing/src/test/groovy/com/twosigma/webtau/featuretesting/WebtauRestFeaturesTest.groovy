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

package com.twosigma.webtau.featuretesting

import com.twosigma.webtau.http.testserver.TestServerJsonResponse
import com.twosigma.webtau.http.testserver.TestServerResponse
import com.twosigma.webtau.utils.JsonUtils
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class WebtauRestFeaturesTest {
    private static WebTauTestRunner testRunner

    @BeforeClass
    static void init() {
        testRunner = new WebTauTestRunner()

        def testServer = testRunner.testServer
        testServer.registerGet("/weather", new TestServerJsonResponse("{\"temperature\": 88}"))
        testServer.registerPost("/employee", json([id: 'id-generated-2'], 201))
        testServer.registerGet("/employee/id-generated-2", json([firstName: 'FN', lastName: 'LN']))

        testRunner.startTestServer()
    }

    @AfterClass
    static void cleanup() {
        testRunner.stopTestServer()
    }

    @Test
    void "simple get"() {
        runCli('simpleGet.groovy', 'url.cfg')
    }

    @Test
    void "simple post"() {
        runCli('simplePost.groovy', 'docArtifacts.cfg')
    }

//    @Test
//    void "crud"() {
//        runCli('springboot/customerCrud.groovy', 'springboot/webtau.cfg')
//        http.delete('http://localhost:8080/customers/1')
//    }
//
    private static void runCli(String restTestName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/rest/$restTestName",
                "scenarios/rest/$configFileName", additionalArgs)
    }

    private static TestServerResponse json(Map response, statusCode = 200) {
        return new TestServerJsonResponse(JsonUtils.serialize(response), statusCode)
    }
}
