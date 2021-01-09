package org.testingisdocumenting.webtau.graphql

import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.testingisdocumenting.webtau.graphql.listener.GraphQLListener
import org.testingisdocumenting.webtau.graphql.listener.GraphQLListeners
import org.testingisdocumenting.webtau.http.HttpHeader

import static org.testingisdocumenting.webtau.graphql.GraphQL.graphql

class GraphQLListenersTest extends GraphQLTestBase implements GraphQLListener {
    private static HttpHeader testHeader = HttpHeader.EMPTY.with("custom", "value")

    Map beforePayload
    Map afterPayload

    // Note that testing beforeFirstGraphQLQuery is not done here due to implementation leveraging singleton approach to
    // make sure callback is only called once

    @Override
    void beforeGraphQLQuery(String query, Map<String, Object> variables, String operationName, HttpHeader requestHeader) {
        beforePayload = [
            query        : query,
            variables    : variables,
            operationName: operationName,
            requestHeader: requestHeader
        ]
    }

    @Override
    void afterGraphQLQuery(String query, Map<String, Object> variables, String operationName, HttpHeader requestHeader, Map<String, Object> data, List<Object> errors) {
        afterPayload = [
            query        : query,
            variables    : variables,
            operationName: operationName,
            requestHeader: requestHeader,
            data         : data,
            errors       : errors
        ]
    }

    @Before
    void init() {
        GraphQLListeners.add(this)

        beforePayload = [:]
        afterPayload = [:]
    }

    @After
    void cleanup() {
        GraphQLListeners.remove(this)
    }

    @Test
    void "before and after query callbacks should have essential graphql call info"() {
        graphql.execute(MULTI_OP_QUERY_WITH_VARS, VARS, OP_NAME, testHeader)

        validateCommonCallBackPayload(beforePayload)
        validateCommonCallBackPayload(afterPayload)

        afterPayload.data.should == [
            'taskById': [
                id: 'a'
            ]
        ]
        afterPayload.errors.should == null
    }

    @Ignore
    @Test
    void "before and after query callbacks should have essential failing graphql call info"() {
        graphql.execute()

        validateCommonCallBackPayload(beforePayload)
        validateCommonCallBackPayload(afterPayload)

        afterPayload.data.should == null
        afterPayload.errors.should == []
    }

    @Test
    void "remove removes both graphql and http listeners"() {
        cleanup()

        graphql.execute(QUERY)

        beforePayload.should == [:]
        afterPayload.should == [:]
    }

    private static void validateCommonCallBackPayload(payload) {
        payload.query.should == MULTI_OP_QUERY_WITH_VARS
        payload.variables.should == VARS
        payload.operationName.should == OP_NAME
        payload.requestHeader["custom"].should == "value"
    }
}
