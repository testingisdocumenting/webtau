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
import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.HttpResponse;
import org.testingisdocumenting.webtau.http.config.HttpConfigurations;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.graphql.GraphQL.GRAPHQL_URL;
import static org.testingisdocumenting.webtau.http.Http.http;

public class GraphQLSchemaLoader {
    public static Set<GraphQLQuery> fetchSchemaDeclaredQueries() {
        HttpRequestBody requestBody = GraphQLRequest.body(IntrospectionQuery.INTROSPECTION_QUERY, null, null);
        String fullUrl = HttpConfigurations.fullUrl(GRAPHQL_URL);
        HttpHeader header = HttpConfigurations.fullHeader(fullUrl, GRAPHQL_URL, HttpHeader.EMPTY);
        HttpResponse httpResponse = http.postToFullUrl(fullUrl, header, requestBody);
        if (httpResponse.getStatusCode() != 200) {
            throw new AssertionError("Error introspecting GraphQL, status code was " + httpResponse.getStatusCode());
        }

        IntrospectionResultToSchema resultToSchema = new IntrospectionResultToSchema();
        Map<String, ?> response = JsonUtils.deserializeAsMap(httpResponse.getTextContent());
        if (response.containsKey("errors")) {
            throw new AssertionError("Error introspecting GraphQL, errors found: " + response.get("errors"));
        }

        if (!response.containsKey("data")) {
            throw new AssertionError("Error introspecting GraphQL, expecting a 'data' field but it was not present");
        }

        Object data = response.get("data");
        if (!(data instanceof Map)) {
            throw new AssertionError("Error introspecting GraphQL, expected 'data' to contain a JSON object" +
                    " but it contains a '" + data.getClass().getSimpleName() + "'");
        }

        @SuppressWarnings("unchecked") Document schemaDefinition = resultToSchema.createSchemaDefinition((Map<String, Object>) data);
        TypeDefinitionRegistry typeDefRegistry = new SchemaParser().buildRegistry(schemaDefinition);

        Set<GraphQLQuery> queries = new HashSet<>();
        Arrays.stream(GraphQLQueryType.values())
                .flatMap(type -> extractTypes(typeDefRegistry, type))
                .forEach(queries::add);

        return queries;
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
