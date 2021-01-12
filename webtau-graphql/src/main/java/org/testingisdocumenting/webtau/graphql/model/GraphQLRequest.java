/*
 * Copyright 2020 webtau maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.webtau.graphql.model;

import org.testingisdocumenting.webtau.http.json.JsonRequestBody;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.utils.JsonParseException;
import org.testingisdocumenting.webtau.utils.JsonUtils;
import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.testingisdocumenting.webtau.utils.CollectionUtils.notNullOrEmpty;

public class GraphQLRequest {
    private final String query;
    private final Map<String, Object> variables;
    private final String operationName;

    public GraphQLRequest(String query) {
        this(query, null, null);
    }

    public GraphQLRequest(String query, Map<String, Object> variables, String operationName) {
        this.query = query;
        this.variables = variables;
        this.operationName = operationName;
    }

    public static Optional<GraphQLRequest> fromHttpRequest(String method, String url, HttpRequestBody requestBody) {
        if (!"POST".equals(method) || !"/graphql".equals(url) || !(requestBody instanceof JsonRequestBody)) {
            return Optional.empty();
        }

        Map<String, ?> request;
        try {
            request = JsonUtils.deserializeAsMap(requestBody.asString());
        } catch (JsonParseException ignore) {
            // Ignoring as it's not a graphql request
            return Optional.empty();
        }

        if (!request.containsKey("query")) {
            // Ignoring as it's not a graphql request
            return Optional.empty();
        }

        Object queryObj = request.get("query");
        if (!(queryObj instanceof String)) {
            // Ignoring as it's not a graphql request
            return Optional.empty();
        }
        String query = (String) queryObj;

        Map<String, Object> variables = null;
        Object variablesObj = request.get("variables");
        if (variablesObj instanceof Map) {
            variables = (Map<String, Object>) variablesObj;
        } else if (variablesObj != null) {
            // Ignoring as it's not a graphql request
            return Optional.empty();
        }

        String operationName = null;
        Object operationNameObj = request.get("operationName");
        if (operationNameObj instanceof String) {
            operationName = (String) operationNameObj;
        } else if (operationNameObj != null) {
            // Ignoring as it's not a graphql request
            return Optional.empty();
        }

        return Optional.of(new GraphQLRequest(query, variables, operationName));
    }

    public String getQuery() {
        return query;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public String getOperationName() {
        return operationName;
    }

    public HttpRequestBody toHttpRequestBody() {
        Map<String, Object> request = new HashMap<>();
        request.put("query", query);

        if (notNullOrEmpty(variables)) {
            request.put("variables", variables);
        }

        if (StringUtils.notNullOrEmpty(operationName)) {
            request.put("operationName", operationName);
        }

        return new JsonRequestBody(request);
    }
}
