package org.testingisdocumenting.webtau.graphql.model.introspection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GraphQLResponse {
    private final GraphQLData data;

    public GraphQLResponse(@JsonProperty("data") GraphQLData data) {
        this.data = data;
    }

    public GraphQLData getData() {
        return data;
    }
}
