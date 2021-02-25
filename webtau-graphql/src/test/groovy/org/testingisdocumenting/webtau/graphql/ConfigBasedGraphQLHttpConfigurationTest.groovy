/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.graphql

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.graphql.model.GraphQLRequest

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg

class ConfigBasedGraphQLHttpConfigurationTest {
    static final OVERRIDDEN_ENDPOINT_NAME = "/overridden-endpoint"

    def customConfig = new ConfigBasedGraphQLHttpConfiguration()
    def requestWithoutOperation = new GraphQLRequest("some query")
    def requestWithOperation = new GraphQLRequest("some query", [:], "myOperation")

    @BeforeClass
    static void setup() {
        WebTauConfig.registerConfigHandlerAsLastHandler(new GraphQLConfig())
    }

    @AfterClass
    static void teardown() {
        WebTauConfig.resetConfigHandlers()
    }

    @Before
    void triggerConfig() {
        getCfg().triggerConfigHandlers()
    }

    @After
    void reset() {
        getCfg().reset()
    }

    @Test
    void "should use common graphql endpoint if not explicitly overridden"() {
        GraphQLConfig.graphQLEndpoint().should == GraphQL.GRAPHQL_URL
        customConfig.requestUrl("/graphql", requestWithoutOperation).should == GraphQL.GRAPHQL_URL
    }

    @Test
    void "should handle null requests"() {
        GraphQLConfig.graphQLEndpoint().should == GraphQL.GRAPHQL_URL
        customConfig.requestUrl("/graphql", null).should == GraphQL.GRAPHQL_URL
    }

    @Test
    void "should use overridden graphql endpoint from config"() {
        def configValue = getCfg().findConfigValue(GraphQLConfig.graphQLEndpoint.getKey())
        configValue.set('test', OVERRIDDEN_ENDPOINT_NAME)
        customConfig.requestUrl("/graphql", requestWithoutOperation).should == OVERRIDDEN_ENDPOINT_NAME
    }

    @Test
    void "should add operation name to query params by default"() {
        GraphQLConfig.graphQLShowOperationAsQueryParam().should == true
        customConfig.requestUrl("/graphql", requestWithOperation).should == "/graphql?operation=myOperation"
    }

    @Test
    void "should not add operation name to query params if turned off in config"() {
        def configValue = getCfg().findConfigValue(GraphQLConfig.graphQLShowOperationAsQueryParam.getKey())
        configValue.set('test', false)
        customConfig.requestUrl("/graphql", requestWithOperation).should == GraphQL.GRAPHQL_URL
    }

    @Test
    void "should use configured endpoint and show operation name"() {
        def configValue = getCfg().findConfigValue(GraphQLConfig.graphQLEndpoint.getKey())
        configValue.set('test', OVERRIDDEN_ENDPOINT_NAME)
        customConfig.requestUrl("/graphql", requestWithOperation).should == "/overridden-endpoint?operation=myOperation"
    }

}
