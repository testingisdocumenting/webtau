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
