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
import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.testingisdocumenting.webtau.utils.CollectionUtils.notNullOrEmpty;

public class GraphQLRequest {
    public static HttpRequestBody body(String query, Map<String, Object> variables, String operationName) {
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
