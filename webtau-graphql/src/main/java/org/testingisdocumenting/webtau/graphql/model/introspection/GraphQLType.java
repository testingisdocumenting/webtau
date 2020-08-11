package org.testingisdocumenting.webtau.graphql.model.introspection;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GraphQLType {
    private final List<GraphQLField> fields;

    public GraphQLType(@JsonProperty("fields") List<GraphQLField> fields) {
        this.fields = fields;
    }

    public List<GraphQLField> getFields() {
        return fields;
    }
}
