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

import com.twosigma.webtau.http.testserver.TestServer
import com.twosigma.webtau.http.testserver.TestServerJsonResponse
import com.twosigma.webtau.http.testserver.TestServerResponse
import com.twosigma.webtau.utils.JsonUtils
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import static com.twosigma.webtau.WebTauDsl.http

class WebTauRestFeaturesTest {
    private static WebTauTestRunner testRunner

    static void registerEndPoints(TestServer testServer) {
        def temperature = [temperature: 88]
        testServer.registerGet("/weather", json(temperature))
        testServer.registerGet("/city/London", json([time: "2018-11-27 13:05:00", weather: temperature]))
        testServer.registerPost("/employee", json([id: 'id-generated-2'], 201))
        testServer.registerGet("/employee/id-generated-2", json([firstName: 'FN', lastName: 'LN']))
    }

    @BeforeClass
    static void init() {
        testRunner = new WebTauTestRunner()

        def testServer = testRunner.testServer
        registerEndPoints(testServer)

        testRunner.startTestServer()
    }

    @AfterClass
    static void cleanup() {
        testRunner.stopTestServer()
    }

    @Test
    void "simple get"() {
        runCli('simpleGet.groovy', 'urlOnly.cfg', "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "simple post"() {
        runCli('simplePost.groovy', 'docArtifacts.cfg', "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "schema validation"() {
        runCli('jsonSchema/validateSchema.groovy', 'jsonSchema/webtau.cfg', "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "crud"() {
        runCli('springboot/customerCrud.groovy', 'springboot/webtau.cfg', "--url=$customersBaseUrl")
    }

    @Test
    void "crud separated"() {
        runCli('springboot/customerCrudSeparated.groovy', 'springboot/webtau.cfg', "--url=$customersBaseUrl")
    }

    @Test
    void "list contain"() {
        http.post(customersUrl(), [firstName: 'FN1', lastName: 'LN1'])
        runCli('springboot/listContain.groovy', 'springboot/webtau.cfg', "--url=$customersBaseUrl")
    }

    @Test
    void "list match"() {
        deleteCustomers()
        runCli('springboot/listMatch.groovy', 'springboot/webtau.cfg', "--url=$customersBaseUrl")
    }

    private static void runCli(String restTestName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/rest/$restTestName",
            "scenarios/rest/$configFileName", additionalArgs)
    }

    private static TestServerResponse json(Map response, statusCode = 200) {
        return new TestServerJsonResponse(JsonUtils.serialize(response), statusCode)
    }

    private void deleteCustomers() {
        def ids = http.get(customersUrl()) {
            return _embedded.customers.id
        }

        ids.each {
            http.delete(customerUrl("$it"))
        }
    }

    private static String getCustomersBaseUrl() {
        String port = System.getProperty("springboot.http.port", "8080")
        return "http://localhost:$port"
    }

    private static customersUrl() {
        return "$customersBaseUrl/customers"
    }

    private static customerUrl(String id) {
        return "$customersBaseUrl/customers/$id"
    }
}
