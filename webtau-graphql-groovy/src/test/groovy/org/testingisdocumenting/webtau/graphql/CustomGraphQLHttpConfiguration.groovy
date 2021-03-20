package org.testingisdocumenting.webtau.graphql

import org.testingisdocumenting.webtau.graphql.config.GraphQLHttpConfiguration
import org.testingisdocumenting.webtau.graphql.model.GraphQLRequest

class CustomGraphQLHttpConfiguration implements GraphQLHttpConfiguration {
    // Note: our test server requires the graphql endpoint to start with "graphql"
    public static final CUSTOM_GRAPHQL_ENDPOINT = 'graphql-custom'

    @Override
    String requestUrl(String url, GraphQLRequest graphQLRequest) {
        if (null != graphQLRequest.operationName && !graphQLRequest.operationName.isEmpty()) {
            return "${CUSTOM_GRAPHQL_ENDPOINT}?operation=${graphQLRequest.operationName}"
        }
        return url
    }
}
