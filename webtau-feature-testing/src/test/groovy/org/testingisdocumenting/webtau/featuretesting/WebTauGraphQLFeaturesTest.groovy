/*
 * Copyright 2020 webtau maintainers
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

import graphql.schema.GraphQLSchema
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.webtau.http.testserver.GraphQLResponseHandler

class WebTauGraphQLFeaturesTest {
    private static WebTauEndToEndTestRunner testRunner

    @BeforeClass
    static void init() {
        GraphQLSchema schema = WebTauGraphQLFeaturesTestData.getSchema()
        GraphQLResponseHandler handler = new GraphQLResponseHandler(schema)
        testRunner = new WebTauEndToEndTestRunner(handler)

        testRunner.startTestServer()
    }

    @AfterClass
    static void cleanup() {
        testRunner.stopTestServer()
    }

    @Test
    void "simple query and mutation"() {
        runCli("queryAndMutation.groovy", "webtau.cfg", "--url=${testRunner.testServer.uri}")
    }

    private static void runCli(String restTestName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/graphql/$restTestName",
            "scenarios/graphql/$configFileName", additionalArgs)
    }
}
