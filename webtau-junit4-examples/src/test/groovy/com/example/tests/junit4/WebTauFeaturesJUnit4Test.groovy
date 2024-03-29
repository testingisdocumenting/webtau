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

import graphql.schema.GraphQLSchema
import org.testingisdocumenting.webtau.featuretesting.WebTauGraphQLFeaturesTestData
import org.testingisdocumenting.webtau.featuretesting.WebTauRestFeaturesTestData
import org.testingisdocumenting.webtau.http.testserver.FixedResponsesHandler
import org.testingisdocumenting.webtau.http.testserver.GraphQLResponseHandler
import org.testingisdocumenting.webtau.http.testserver.TestServer
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Paths

class WebTauFeaturesJUnit4Test {
    private static final FixedResponsesHandler restHandler = new FixedResponsesHandler()
    private static final GraphQLSchema schema = WebTauGraphQLFeaturesTestData.getSchema(
        Paths.get("..", "webtau-feature-testing", "examples","scenarios", "graphql", "schema.graphql")
    )
    private static final GraphQLResponseHandler handler = new GraphQLResponseHandler(schema, restHandler)
    private static final TestServer testServer = new TestServer(handler)

    private static final JUnit4FeatureTestRunner testRunner = new JUnit4FeatureTestRunner()

    private static final TODO_BASE_URL = 'https://jsonplaceholder.typicode.com/'

    @BeforeClass
    static void startServer() {
        testServer.startRandomPort()
        WebTauRestFeaturesTestData.registerEndPoints(testServer, restHandler)
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
    void graphQLWeatherJavaTest() {
        testRunner.runAndValidate(GraphQLWeatherJavaIT, testServer.uri.toString())
    }

    @Test
    void graphQLWeatherGroovyTest() {
        testRunner.runAndValidate(GraphQLWeatherGroovyIT, testServer.uri.toString())
    }
}
