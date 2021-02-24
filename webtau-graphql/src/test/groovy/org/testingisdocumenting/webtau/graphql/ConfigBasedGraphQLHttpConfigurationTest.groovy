package org.testingisdocumenting.webtau.graphql

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.graphql.model.GraphQLRequest

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg

class ConfigBasedGraphQLHttpConfigurationTest {
    static final OVERRIDDEN_ENDPOINT_NAME = "/overridden-endpoint"

    def customConfig = new ConfigBasedGraphQLHttpConfiguration()
    def requestWithoutOperation = new GraphQLRequest("some query")
    def requestWithOperation = new GraphQLRequest("some query", [:], "myOperation")

    @Before
    void setup() {
        WebTauConfig.registerConfigHandlerAsLastHandler(new GraphQLConfig())
        getCfg().triggerConfigHandlers()
    }

    @After
    void reset() {
        getCfg().reset()
    }

    @Test
    void "should use common graphql endpoint if not explicitly overridden"() {
        getCfg().get(GraphQLConfig.graphqlEndpoint.getKey()).should == GraphQL.GRAPHQL_URL
        customConfig.requestUrl("/graphql", requestWithoutOperation).should == GraphQL.GRAPHQL_URL
    }

    @Test
    void "should handle null requests"() {
        getCfg().get(GraphQLConfig.graphqlEndpoint.getKey()).should == GraphQL.GRAPHQL_URL
        customConfig.requestUrl("/graphql", null).should == GraphQL.GRAPHQL_URL
    }

    @Test
    void "should use overridden graphql endpoint from config"() {
        def configValue = getCfg().findConfigValue(GraphQLConfig.graphqlEndpoint.getKey())
        configValue.set('test', OVERRIDDEN_ENDPOINT_NAME)
        customConfig.requestUrl("/graphql", requestWithoutOperation).should == OVERRIDDEN_ENDPOINT_NAME
    }

    @Test
    void "should add operation name to query params by default"() {
        getCfg().get(GraphQLConfig.showGraphqlOperationAsQueryParam.getKey()).should == true
        customConfig.requestUrl("/graphql", requestWithOperation).should == "/graphql?operation=myOperation"
    }

    @Test
    void "should not add operation name to query params if turned off in config"() {
        def configValue = getCfg().findConfigValue(GraphQLConfig.showGraphqlOperationAsQueryParam.getKey())
        configValue.set('test', false)
        customConfig.requestUrl("/graphql", requestWithOperation).should == GraphQL.GRAPHQL_URL
    }

    @Test
    void "should use configured endpoint and show operation name"() {
        def configValue = getCfg().findConfigValue(GraphQLConfig.graphqlEndpoint.getKey())
        configValue.set('test', OVERRIDDEN_ENDPOINT_NAME)
        customConfig.requestUrl("/graphql", requestWithOperation).should == "/overridden-endpoint?operation=myOperation"
    }

}
