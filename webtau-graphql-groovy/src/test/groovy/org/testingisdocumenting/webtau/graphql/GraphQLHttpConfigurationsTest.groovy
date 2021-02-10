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
import org.testingisdocumenting.webtau.graphql.config.GraphQLHttpConfiguration
import org.testingisdocumenting.webtau.graphql.config.GraphQLHttpConfigurations
import org.testingisdocumenting.webtau.graphql.model.GraphQLRequest
import org.testingisdocumenting.webtau.http.HttpHeader
import org.testingisdocumenting.webtau.http.config.HttpConfiguration
import org.testingisdocumenting.webtau.http.config.HttpConfigurations

import static org.testingisdocumenting.webtau.graphql.GraphQL.graphql

class GraphQLHttpConfigurationsTest extends GraphQLTestBase {
    static final CUSTOM_GRAPHQL_ENDPOINT = 'graphql-custom'

    private HttpConfiguration urlVerifier = new HttpConfiguration() {
        List<String> urls = []

        @Override
        String fullUrl(String url) {
            urls << url
            return url
        }

        @Override
        HttpHeader fullHeader(String fullUrl, String passedUrl, HttpHeader given) {
            return given
        }
    }
    private withOperationAsQueryParam = new GraphQLHttpConfiguration() {
        @Override
        String requestUrl(String url, GraphQLRequest graphQLRequest) {
            if (null != graphQLRequest.operationName && !graphQLRequest.operationName.isEmpty()) {
                return "${CUSTOM_GRAPHQL_ENDPOINT}?operation=${graphQLRequest.operationName}"
            }
            return url
        }
    }

    @Before
    void addGraphQLUrlVerifier() {
        urlVerifier.urls = []
        HttpConfigurations.add(urlVerifier)
    }

    @After
    void cleanUp() {
        HttpConfigurations.remove(urlVerifier)
    }

    @Test
    void "default GraphQLHttpConfiguration uses standard graphql url"() {
        graphql.execute(QUERY_WITH_VARS)
        urlVerifier.urls.find {it.endsWith('graphql') }.shouldNot == null
    }

    @Test
    void "removed configurations are not invoked"() {
        cleanUp()
        graphql.execute(QUERY_WITH_VARS, VARS, OP_NAME)
        urlVerifier.urls.should == []
    }

    @Test
    void "can override graphql url with custom GraphQLHttpConfiguration"() {
        try {
            GraphQLHttpConfigurations.add(withOperationAsQueryParam)
            graphql.execute(QUERY_WITH_VARS, VARS, OP_NAME)
            urlVerifier.urls
                    .find {it.endsWith("/${CUSTOM_GRAPHQL_ENDPOINT}?operation=${OP_NAME}") }
                    .shouldNot == null
        } finally {
            GraphQLHttpConfigurations.remove(withOperationAsQueryParam)
        }
    }
}
