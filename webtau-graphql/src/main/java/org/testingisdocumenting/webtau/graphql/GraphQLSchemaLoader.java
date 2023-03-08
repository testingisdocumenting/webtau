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

package org.testingisdocumenting.webtau.graphql;

import graphql.introspection.IntrospectionQuery;
import graphql.introspection.IntrospectionResultToSchema;
import graphql.language.Document;
import graphql.language.FieldDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.testingisdocumenting.webtau.graphql.model.GraphQLRequest;
import org.testingisdocumenting.webtau.graphql.model.GraphQLResponse;
import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.HttpResponse;
import org.testingisdocumenting.webtau.http.config.WebTauHttpConfigurations;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.graphql.GraphQL.GRAPHQL_URL;
import static org.testingisdocumenting.webtau.http.Http.http;

public class GraphQLSchemaLoader {
    public static Optional<Set<GraphQLQuery>> fetchSchemaDeclaredQueries() {
        WebTauStep step = WebTauStep.createStep(tokenizedMessage().action("fetching").id("graphQL").classifier("schema"),
                () -> tokenizedMessage().action("fetched").id("graphQL").classifier("schema"),
                GraphQLSchemaLoader::fetchSchemaDeclaredQueriesStep);

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    private static Optional<Set<GraphQLQuery>> fetchSchemaDeclaredQueriesStep() {
        HttpResponse httpResponse;
        try {
            httpResponse = sendIntrospectionQuery();
        } catch (Exception e) {
            return handleIntrospectionError("Error posting GraphQL introspection query", e);
        }
        if (httpResponse.getStatusCode() != 200) {
            return handleIntrospectionError("Error introspecting GraphQL, status code was " + httpResponse.getStatusCode());
        }

        return convertIntrospectionResponse(httpResponse);
    }

    private static HttpResponse sendIntrospectionQuery() {
        HttpRequestBody requestBody = new GraphQLRequest(IntrospectionQuery.INTROSPECTION_QUERY).toHttpRequestBody();
        String fullUrl = WebTauHttpConfigurations.fullUrl(GRAPHQL_URL);
        HttpHeader header = WebTauHttpConfigurations.fullHeader(fullUrl, GRAPHQL_URL, HttpHeader.EMPTY);

        return http.postToFullUrl(fullUrl, header, requestBody);
    }

    private static Optional<Set<GraphQLQuery>> convertIntrospectionResponse(HttpResponse httpResponse) {
        Optional<GraphQLResponse> graphQLResponse = GraphQLResponse.from(httpResponse);
        return graphQLResponse.map(response -> {
            if (response.getErrors() != null) {
                return handleIntrospectionError("Error introspecting GraphQL, errors found: " + response.getErrors());
            }

            if (response.getData() == null) {
                return handleIntrospectionError("Error introspecting GraphQL, expecting a 'data' field but it was not present");
            }

            IntrospectionResultToSchema resultToSchema = new IntrospectionResultToSchema();
            Document schemaDefinition = resultToSchema.createSchemaDefinition(response.getData());
            TypeDefinitionRegistry typeDefRegistry = new SchemaParser().buildRegistry(schemaDefinition);

            Set<GraphQLQuery> queries = new HashSet<>();
            Arrays.stream(GraphQLQueryType.values())
                    .flatMap(type -> extractTypes(typeDefRegistry, type))
                    .forEach(queries::add);
            return Optional.of(queries);
        }).orElseGet(() -> handleIntrospectionError("Error introspecting GraphQL, not a valid GraphQL response"));
    }

    private static Optional<Set<GraphQLQuery>> handleIntrospectionError(String msg) {
        return handleIntrospectionError(msg, null);
    }

    private static Optional<Set<GraphQLQuery>> handleIntrospectionError(String msg, Throwable cause) {
        if (GraphQLConfig.ignoreIntrospectionFailures()) {
            return Optional.empty();
        }

        if (cause == null) {
            throw new AssertionError(msg);
        } else {
            throw new AssertionError(msg, cause);
        }
    }

    private static Stream<GraphQLQuery> extractTypes(TypeDefinitionRegistry registry, GraphQLQueryType type) {
        String typeName = type.name().charAt(0) + type.name().substring(1).toLowerCase();
        return registry.getType(typeName)
                .filter(def -> def instanceof ObjectTypeDefinition)
                .map(def -> {
                    ObjectTypeDefinition objectTypeDef = (ObjectTypeDefinition) def;
                    List<FieldDefinition> fieldDefinitions = objectTypeDef.getFieldDefinitions();
                    return fieldDefinitions.stream()
                            .map(fieldDef -> new GraphQLQuery(fieldDef.getName(), type));
                })
                .orElseGet(Stream::empty);
    }
}
