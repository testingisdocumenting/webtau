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

package com.twosigma.webtau.featuretesting


import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import static com.twosigma.webtau.WebTauDsl.http
import static com.twosigma.webtau.featuretesting.FeaturesDocArtifactsExtractor.extractCodeSnippets

class WebTauRestFeaturesTest {
    private static WebTauEndToEndTestRunner testRunner


    @BeforeClass
    static void init() {
        testRunner = new WebTauEndToEndTestRunner()

        def testServer = testRunner.testServer
        WebTauRestFeaturesTestData.registerEndPoints(testServer)

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
    void "schema validation extract snippets"() {
        def root = 'doc-artifacts/snippets/json-schema'

        extractCodeSnippets(
                root, 'examples/scenarios/rest/jsonSchema/validateSchema.groovy', [
                'validateBody.groovy': 'valid schema',
                'validateField.groovy': 'validate specific field',
        ])
    }

    @Test
    void "redirect"() {
        runCli('redirect/redirectOn.groovy', 'urlOnly.cfg', "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "redirect disabled"() {
        runCli('redirect/redirectOff.groovy', 'urlOnly.cfg', "--url=${testRunner.testServer.uri}", '--disableRedirects')
    }

    @Test
    void "open api disable"() {
        runCli('openapi/disableOpenApiValidation.groovy', 'openapi/webtau.cfg', "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "open api extract snippets"() {
        def root = 'doc-artifacts/snippets/openapi'

        extractCodeSnippets(
                root, 'examples/scenarios/rest/openapi/disableOpenApiValidation.groovy', [
                'disableAll.groovy': 'disable all validation',
                'disableRequest.groovy': 'disable request validation',
                'disableResponse.groovy': 'disable response validation',
        ])
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
    void "crud separated missing method"() {
        runCli('springboot/customerCrudSeparatedMissingMethod.groovy', 'springboot/withSpec.cfg',
                "--url=$customersBaseUrl")
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

    @Test
    void "list match by key"() {
        deleteCustomers()
        runCli('springboot/listMatchByKey.groovy', 'springboot/webtau.cfg', "--url=$customersBaseUrl")
    }

    @Test
    void "recursive scenario discovery"() {
        testRunner.runCli("recursive/scenarios", "urlOnly.cfg", "--url=${testRunner.testServer.uri}")
    }

    private static void runCli(String restTestName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/rest/$restTestName",
            "scenarios/rest/$configFileName", additionalArgs)
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
        String port = System.getProperty('springboot.http.port')
        if (!port || port.isEmpty()) {
            port = '8080'
        }

        return "http://localhost:$port"
    }

    private static customersUrl() {
        return "$customersBaseUrl/customers"
    }

    private static customerUrl(String id) {
        return "$customersBaseUrl/customers/$id"
    }
}
