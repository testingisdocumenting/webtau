package org.testingisdocumenting.webtau.graphql.model.introspection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GraphQLData {
    private final GraphQLSchema schema;

    public GraphQLData(@JsonProperty("__schema") GraphQLSchema schema) {
        this.schema = schema;
    }

    public GraphQLSchema getSchema() {
        return schema;
    }
}
