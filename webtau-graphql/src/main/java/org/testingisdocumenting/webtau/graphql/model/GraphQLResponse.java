package org.testingisdocumenting.webtau.graphql.model;

import org.checkerframework.checker.nullness.Opt;
import org.testingisdocumenting.webtau.http.HttpResponse;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GraphQLResponse {
    private final Map<String, Object> data;
    private final List<Object> errors;

    public GraphQLResponse(Map<String, Object> data, List<Object> errors) {
        this.data = data;
        this.errors = errors;
    }

    public static Optional<GraphQLResponse> from(HttpResponse httpResponse) {
        if (!httpResponse.isJson()) {
            return Optional.empty();
        }

        Object responseObj = JsonUtils.deserialize(httpResponse.getTextContent());
        if (!(responseObj instanceof Map)) {
            return Optional.empty();
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) responseObj;

        Object dataObj = response.get("data");
        if (dataObj != null && !(dataObj instanceof Map)) {
            return Optional.empty();
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) response.get("data");

        Object errorsObj = response.get("errors");
        if (errorsObj != null && !(errorsObj instanceof List)) {
            return Optional.empty();
        }
        @SuppressWarnings("unchecked")
        List<Object> errors = (List<Object>) response.get("errors");

        return Optional.of(new GraphQLResponse(data, errors));
    }

    public Map<String, Object> getData() {
        return data;
    }

    public List<Object> getErrors() {
        return errors;
    }
}
