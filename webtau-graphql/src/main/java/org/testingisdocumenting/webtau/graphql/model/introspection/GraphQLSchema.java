package org.testingisdocumenting.webtau.graphql.model.introspection;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class GraphQLSchema {
    private final Optional<GraphQLType> queryType;
    private final Optional<GraphQLType> mutationType;
    private final Optional<GraphQLType> subscriptionType;

    public GraphQLSchema(@JsonProperty("queryType") Optional<GraphQLType> queryType,
                         @JsonProperty("mutationType") Optional<GraphQLType> mutationType,
                         @JsonProperty("subscriptionType") Optional<GraphQLType> subscriptionType) {
        this.queryType = queryType;
        this.mutationType = mutationType;
        this.subscriptionType = subscriptionType;
    }

    public Optional<GraphQLType> getQueryType() {
        return queryType;
    }

    public Optional<GraphQLType> getMutationType() {
        return mutationType;
    }

    public Optional<GraphQLType> getSubscriptionType() {
        return subscriptionType;
    }
}
