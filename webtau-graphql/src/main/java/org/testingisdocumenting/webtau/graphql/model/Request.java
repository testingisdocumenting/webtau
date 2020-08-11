package org.testingisdocumenting.webtau.graphql.model;

import org.testingisdocumenting.webtau.http.json.JsonRequestBody;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.testingisdocumenting.webtau.utils.CollectionUtils.notNullOrEmpty;

public class Request {
    public static final String INTROSPECTION_QUERY = "{\n" +
            "    __schema {\n" +
            "        queryType {\n" +
            "            fields {\n" +
            "                name\n" +
            "            }\n" +
            "        }\n" +
            "        mutationType {\n" +
            "            fields {\n" +
            "                name\n" +
            "            }\n" +
            "        }\n" +
            "        subscriptionType {\n" +
            "            fields {\n" +
            "                name\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";

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
