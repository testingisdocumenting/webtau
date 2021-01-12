/*
 * Copyright 2021 webtau maintainers
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
import org.junit.Before
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
    void afterGraphQLQuery(String query, Map<String, Object> variables, String operationName, HttpHeader requestHeader, Map<String, Object> responseData, List<Object> errors) {
        afterPayload = [
            query        : query,
            variables    : variables,
            operationName: operationName,
            requestHeader: requestHeader,
            data         : responseData,
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

    @Test
    void "after query callback should have correct response data"() {
        graphql.execute(ERROR_QUERY, [msg: "test error msg"])

        afterPayload.data.should == [error: null]
        afterPayload.errors[0].message.should == "Exception while fetching data (/error) : Error executing query: test error msg"
        afterPayload.errors.size().should == 1
    }

    @Test
    void "removed listeners are not invoked"() {
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
