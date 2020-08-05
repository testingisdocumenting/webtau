package org.testingisdocumenting.webtau.http.testserver;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
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
import java.util.stream.Collectors;

/*
There is no "standard" for handling GraphQL over HTTP but GraphQL provides some best practices for this which we follow here:
https://graphql.org/learn/serving-over-http/
 */
public class GraphQLResponseHandler extends AbstractHandler {
    private final GraphQL graphQL;

    public GraphQLResponseHandler(GraphQLSchema schema) {
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    @Override
    public void handle(String url, Request baseRequest, HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        if (!"/graphql".equals(baseRequest.getOriginalURI())) {
            response.setStatus(404);
        } else if ("GET".equals(request.getMethod())) {
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

        baseRequest.setHandled(true);
    }

    private void handle(String query, String operationName, Map<String, Object> variables, HttpServletResponse response) throws IOException {
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
}
