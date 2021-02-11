package org.testingisdocumenting.webtau.graphql

import org.testingisdocumenting.webtau.graphql.config.GraphQLHttpConfiguration
import org.testingisdocumenting.webtau.graphql.model.GraphQLRequest

class CustomGraphQLHttpConfiguration implements GraphQLHttpConfiguration {
    public static final CUSTOM_GRAPHQL_ENDPOINT = 'graphql-custom'

    @Override
    String requestUrl(String url, GraphQLRequest graphQLRequest) {
        if (null != graphQLRequest.operationName && !graphQLRequest.operationName.isEmpty()) {
            return "${CUSTOM_GRAPHQL_ENDPOINT}?operation=${graphQLRequest.operationName}"
        }
        return url
    }
}
