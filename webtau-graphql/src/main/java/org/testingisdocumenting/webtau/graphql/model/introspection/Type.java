package org.testingisdocumenting.webtau.graphql.model.introspection;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Type {
    private final List<Field> fields;

    public Type(@JsonProperty("fields") List<Field> fields) {
        this.fields = fields;
    }

    public List<Field> getFields() {
        return fields;
    }
}
