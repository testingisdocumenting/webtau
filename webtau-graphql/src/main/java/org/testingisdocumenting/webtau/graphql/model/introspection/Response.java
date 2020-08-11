package org.testingisdocumenting.webtau.graphql.model.introspection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {
    private final Data data;

    public Response(@JsonProperty("data") Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }
}
