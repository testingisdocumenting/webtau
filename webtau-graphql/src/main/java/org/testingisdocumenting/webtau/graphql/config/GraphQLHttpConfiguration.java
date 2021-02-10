package org.testingisdocumenting.webtau.graphql.config;

import org.testingisdocumenting.webtau.graphql.model.GraphQLRequest;

public interface GraphQLHttpConfiguration {
    String requestUrl(String url, GraphQLRequest graphQLRequest);
}
