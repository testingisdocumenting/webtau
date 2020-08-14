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


import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.webtau.http.testserver.FixedResponsesHandler

import static org.testingisdocumenting.webtau.WebTauDsl.http
import static org.testingisdocumenting.webtau.featuretesting.FeaturesDocArtifactsExtractor.extractCodeSnippets

class WebTauRestFeaturesTest {
    private static WebTauEndToEndTestRunner testRunner


    @BeforeClass
    static void init() {
        FixedResponsesHandler handler = new FixedResponsesHandler()
        testRunner = new WebTauEndToEndTestRunner(handler)

        WebTauRestFeaturesTestData.registerEndPoints(testRunner.testServer, handler)

        testRunner.startTestServer()
    }

    @AfterClass
    static void cleanup() {
        testRunner.stopTestServer()
    }

    @Test
    void "simple get"() {
        runCli('simpleGet.groovy', 'urlOnlyCfg.groovy', "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "simple post"() {
        runCli('simplePost.groovy', 'docArtifactsCfg.groovy', "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "ping"() {
        runCli('ping.groovy', 'docArtifactsCfg.groovy', "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "ping extract snippets"() {
        extractCodeSnippets(
                'ping', 'examples/scenarios/rest/ping.groovy', [
                'pingIfCheck.groovy': 'ping',
                'pingOverloads.groovy': 'ping overloads',
        ])
    }

    @Test
    void "schema validation"() {
        runCli('jsonSchema/validateSchema.groovy', 'jsonSchema/webtau.groovy', "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "schema validation extract snippets"() {
        extractCodeSnippets(
                'json-schema', 'examples/scenarios/rest/jsonSchema/validateSchema.groovy', [
                'validateBody.groovy': 'valid schema',
                'validateField.groovy': 'validate specific field',
        ])
    }

    @Test
    void "redirect"() {
        runCli('redirect/redirectOn.groovy', 'urlOnlyCfg.groovy', "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "redirect disabled"() {
        runCli('redirect/redirectOff.groovy', 'urlOnlyCfg.groovy', "--url=${testRunner.testServer.uri}", '--disableRedirects')
    }

    @Test
    void "open api disable"() {
        runCli('openapi/disableOpenApiValidation.groovy', 'openapi/webtau.groovy', "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "open api extract snippets"() {
        extractCodeSnippets(
                'openapi', 'examples/scenarios/rest/openapi/disableOpenApiValidation.groovy', [
                'disableAll.groovy': 'disable all validation',
                'disableRequest.groovy': 'disable request validation',
                'disableResponse.groovy': 'disable response validation',
        ])
    }

    @Test
    void "crud"() {
        runCli('springboot/customerCrud.groovy', 'springboot/webtau.groovy', "--url=$customersBaseUrl")
    }

    @Test
    void "crud separated"() {
        runCli('springboot/customerCrudSeparated.groovy', 'springboot/webtau.groovy', "--url=$customersBaseUrl")
    }

    @Test
    void "crud separated missing method"() {
        runCli('springboot/customerCrudSeparatedMissingMethod.groovy', 'springboot/withSpecCfg.groovy',
                "--url=$customersBaseUrl")
    }

    @Test
    void "list contain"() {
        http.post(customersUrl(), [firstName: 'FN1', lastName: 'LN1'])
        runCli('springboot/listContain.groovy', 'springboot/webtau.groovy', "--url=$customersBaseUrl")
    }

    @Test
    void "list match"() {
        deleteCustomers()
        runCli('springboot/listMatch.groovy', 'springboot/webtau.groovy', "--url=$customersBaseUrl")
    }

    @Test
    void "list match by key"() {
        deleteCustomers()
        runCli('springboot/listMatchByKey.groovy', 'springboot/webtau.groovy', "--url=$customersBaseUrl")
    }

    @Test
    void "recursive scenario discovery"() {
        testRunner.runCli("recursive/scenarios", "urlOnlyCfg.groovy", "--url=${testRunner.testServer.uri}")
    }

    private static void runCli(String restTestName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/rest/$restTestName",
            "scenarios/rest/$configFileName", additionalArgs)
    }

    private void deleteCustomers() {
        def ids = http.get(customersUrl()) {
            return body.id
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
