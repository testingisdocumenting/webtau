package org.testingisdocumenting.webtau.graphql.model.introspection;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class Schema {
    private final Optional<Type> queryType;
    private final Optional<Type> mutationType;
    private final Optional<Type> subscriptionType;

    public Schema(@JsonProperty("queryType") Optional<Type> queryType,
                  @JsonProperty("mutationType") Optional<Type> mutationType,
                  @JsonProperty("subscriptionType") Optional<Type> subscriptionType) {
        this.queryType = queryType;
        this.mutationType = mutationType;
        this.subscriptionType = subscriptionType;
    }

    public Optional<Type> getQueryType() {
        return queryType;
    }

    public Optional<Type> getMutationType() {
        return mutationType;
    }

    public Optional<Type> getSubscriptionType() {
        return subscriptionType;
    }
}
