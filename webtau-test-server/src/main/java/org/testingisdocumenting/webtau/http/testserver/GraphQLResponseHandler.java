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

package org.testingisdocumenting.webtau.http.testserver;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/*
There is no "standard" for handling GraphQL over HTTP but GraphQL provides some best practices for this which we follow here:
https://graphql.org/learn/serving-over-http/
 */
public class GraphQLResponseHandler extends AbstractHandler {
    private final GraphQL graphQL;
    private final Optional<Handler> additionalHandler;
    private Optional<String> expectedAuthHeaderValue;

    public GraphQLResponseHandler(GraphQLSchema schema) {
        this(schema, null);
    }

    public GraphQLResponseHandler(GraphQLSchema schema, Handler additionalHandler) {
        this.graphQL = GraphQL.newGraphQL(schema).build();
        this.additionalHandler = Optional.ofNullable(additionalHandler);
        this.expectedAuthHeaderValue = Optional.empty();
    }

    @Override
    public void handle(String url, Request baseRequest, HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        if ("/graphql".equals(baseRequest.getOriginalURI())) {
            handleGraphQLPathRequest(request, response);
        } else if (additionalHandler.isPresent()) {
            additionalHandler.get().handle(url, baseRequest, request, response);
        } else {
            response.setStatus(404);
        }

        baseRequest.setHandled(true);
    }

    public <R> R withAuthEnabled(String expectedAuthHeaderValue, Supplier<R> code) {
        this.expectedAuthHeaderValue = Optional.of(expectedAuthHeaderValue);
        try {
            return code.get();
        } finally {
            this.expectedAuthHeaderValue = Optional.empty();
        }
    }

    private void handleGraphQLPathRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!isAuthorised(request)) {
            response.setStatus(401);
            return;
        }

        if ("GET".equals(request.getMethod())) {
            String query = request.getParameter("query");
            String operationName = request.getParameter("operationName");
            @SuppressWarnings("unchecked")
            Map<String, Object> variables = (Map<String, Object>) JsonUtils.deserializeAsMap(request.getParameter("variables"));

            handle(query, operationName, variables, response);
        } else if ("POST".equals(request.getMethod())) {
            // Don't currently support the query param for POST
            if ("application/json".equals(request.getContentType())) {
                Map<String, ?> requestBody = JsonUtils.deserializeAsMap(request.getReader().lines().collect(Collectors.joining()));

                String query = (String) requestBody.get("query");
                String operationName = (String) requestBody.get("operationName");
                @SuppressWarnings("unchecked")
                Map<String, Object> variables = (Map<String, Object>) requestBody.get("variables");

                handle(query, operationName, variables, response);
            } else if ("application/graphql".equals(request.getContentType())) {
                String query = request.getReader().lines().collect(Collectors.joining());
                handle(query, (String) null, null, response);
            } else {
                response.setStatus(415);
            }
        } else {
            response.setStatus(405);
        }
    }

    private boolean isAuthorised(HttpServletRequest request) {
        return expectedAuthHeaderValue
                .map(expectedVal -> expectedVal.equals(request.getHeader("Authorization")))
                .orElse(true);
    }

    private void handle(
            String query,
            String operationName,
            Map<String, Object> variables,
            HttpServletResponse response) throws IOException {
        ExecutionInput executionInput = ExecutionInput.newExecutionInput(query)
                .operationName(operationName)
                .variables(variables == null ? Collections.emptyMap() : variables)
                .build();
        ExecutionResult result = graphQL.execute(executionInput);
        Map<String, Object> responseBody = new HashMap<>();
        if (result.isDataPresent()) {
            responseBody.put("data", result.getData());
        }
        if (result.getErrors() != null && result.getErrors().size() > 0) {
            responseBody.put("errors", result.getErrors());
        }

        response.setStatus(200);
        response.setContentType("application/json");
        response.getOutputStream().write(JsonUtils.serializeToBytes(responseBody));
    }

    public static class Header {
        private final String name;
        private final String value;

        private Header(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
