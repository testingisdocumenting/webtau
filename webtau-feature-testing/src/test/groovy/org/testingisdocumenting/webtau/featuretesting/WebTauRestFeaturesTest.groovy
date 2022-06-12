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
import org.testingisdocumenting.webtau.openapi.OpenApi

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
        runCli("simpleGet.groovy", "urlOnly.cfg.groovy", "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "simple post"() {
        runCli("simplePost.groovy", "docArtifacts.cfg.groovy", "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "simple get text"() {
        runCli("simpleGetText.groovy", "urlOnly.cfg.groovy", "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "ping"() {
        runCli("ping.groovy", "docArtifacts.cfg.groovy", "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "ping extract snippets"() {
        extractCodeSnippets(
                "ping", "examples/scenarios/rest/ping.groovy", [
                "pingIfCheck.groovy": "ping",
                "pingOverloads.groovy": "ping overloads",
        ])
    }

    @Test
    void "persona get"() {
        runCli("headers/personaGet.groovy", "headers/webtau.persona.cfg.groovy", "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "schema validation"() {
        runCli("jsonSchema/validateSchema.groovy", "jsonSchema/webtau.cfg.groovy", "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "schema validation extract snippets"() {
        extractCodeSnippets(
                "json-schema", "examples/scenarios/rest/jsonSchema/validateSchema.groovy", [
                "validateBody.groovy": "valid schema",
                "validateField.groovy": "validate specific field",
        ])
    }

    @Test
    void "redirect"() {
        runCli("redirect/redirectOn.groovy", "urlOnly.cfg.groovy", "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "redirect disabled"() {
        runCli("redirect/redirectOff.groovy", "urlOnly.cfg.groovy", "--url=${testRunner.testServer.uri}", "--disableRedirects")
    }

    @Test
    void "open api http spec"() {
        runCli("openapi/openApiHttpSpec.groovy", "openapi/webtau.httpspec.cfg.groovy",
                "--url=${customersBaseUrl}",
                "--openApiSpecUrl=/v3/api-docs")
    }

    @Test
    void "open api unspecified url"() {
        runCli("openapi/unspecifiedUrl.groovy", "openapi/webtau.httpspec.cfg.groovy",
                "--url=${customersBaseUrl}",
                "--openApiSpecUrl=scenarios/rest/openapi/not-full-spec.json")
    }

    @Test
    void "open api disable"() {
        runCli("openapi/disableOpenApiValidation.groovy", "openapi/webtau.cfg.groovy", "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "open api extract snippets"() {
        extractCodeSnippets(
                "openapi", "examples/scenarios/rest/openapi/disableOpenApiValidation.groovy", [
                "disableAll.groovy": "disable all validation",
                "disableRequest.groovy": "disable request validation",
                "disableResponse.groovy": "disable response validation",
        ])
    }

    @Test
    void "crud"() {
        runCli("springboot/customerCrud.groovy", "springboot/webtau.cfg.groovy", "--url=$customersBaseUrl")
    }

    @Test
    void "crud separated"() {
        runCli("springboot/customerCrudSeparated.groovy", "springboot/webtau.cfg.groovy", "--url=$customersBaseUrl")
    }

    @Test
    void "crud separated missing method"() {
        runCli("springboot/customerCrudSeparatedMissingMethod.groovy", "springboot/withSpec.cfg.groovy",
                "--url=$customersBaseUrl")
    }

    @Test
    void "list contain"() {
        OpenApi.withoutValidation {
            http.post(customersUrl(), [firstName: "FN1", lastName: "LN1"])
        }

        runCli("springboot/listContain.groovy", "springboot/webtau.cfg.groovy", "--url=$customersBaseUrl")
    }

    @Test
    void "list match"() {
        deleteCustomers()
        runCli("springboot/listMatch.groovy", "springboot/webtau.cfg.groovy", "--url=$customersBaseUrl")
    }

    @Test
    void "list match by key"() {
        deleteCustomers()
        runCli("springboot/listMatchByKey.groovy", "springboot/webtau.cfg.groovy", "--url=$customersBaseUrl")
    }

    @Test
    void "handle invalid json request and response in report"() {
        runCli("invalidJsonRequestResponse.groovy", "webtau.cfg.groovy", "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "proxy config validate"() {
        runCli("proxy/validateProxy.groovy", "proxy/webtau.proxy.cfg.groovy", "--url=${testRunner.testServer.uri}")
    }

    @Test
    void "start server before first test and stop after"() {
        runCli("springboot/startAndStopAsPartOfSuite.groovy", "springboot/webtau-auto-start.cfg.groovy")
    }

    @Test
    void "use existing server before first test"() {
        runCli("springboot/checkExistingServerIsStillUp.groovy", "springboot/webtau-auto-start.cfg.groovy",
                "--url=$customersBaseUrl")
    }

    private static void runCli(String restTestName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/rest/$restTestName",
            "scenarios/rest/$configFileName", additionalArgs)
    }

    private void deleteCustomers() {
        OpenApi.withoutValidation {
            def ids = http.get(customersUrl()) {
                return body.id
            }

            ids.each {
                http.delete(customerUrl("$it"))
            }
        }
    }

    private static String getCustomersBaseUrl() {
        return SpringBootDemoAppUrl.baseUrl
    }

    private static customersUrl() {
        return "$customersBaseUrl/customers"
    }

    private static customerUrl(String id) {
        return "$customersBaseUrl/customers/$id"
    }
}
