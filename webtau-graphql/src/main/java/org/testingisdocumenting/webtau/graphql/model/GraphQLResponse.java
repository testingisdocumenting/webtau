/*
 * Copyright 2021 webtau maintainers
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
