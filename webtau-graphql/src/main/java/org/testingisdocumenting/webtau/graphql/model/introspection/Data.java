package org.testingisdocumenting.webtau.graphql.model.introspection;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.testingisdocumenting.webtau.graphql.model.introspection.Schema;

public class Data {
    private final Schema schema;

    public Data(@JsonProperty("__schema") Schema schema) {
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }
}
