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

import org.testingisdocumenting.webtau.graphql.model.GraphQLRequest;
import org.testingisdocumenting.webtau.graphql.model.introspection.GraphQLField;
import org.testingisdocumenting.webtau.graphql.model.introspection.GraphQLResponse;
import org.testingisdocumenting.webtau.graphql.model.introspection.GraphQLType;
import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.HttpResponse;
import org.testingisdocumenting.webtau.http.config.HttpConfigurations;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandlers;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import static org.testingisdocumenting.webtau.graphql.model.GraphQLRequest.INTROSPECTION_QUERY;
import static org.testingisdocumenting.webtau.http.Http.http;

public class GraphQLSchemaLoader {
    public static Set<GraphQLQuery> fetchSchemaDeclaredQueries() {
        HttpRequestBody requestBody = GraphQLRequest.body(INTROSPECTION_QUERY, null, null);

        HttpResponse httpResponse = HttpConfigurations.withEnabledConfigurations(() ->
                HttpValidationHandlers.withDisabledHandlers(() -> {
                    String fullUrl = HttpConfigurations.fullUrl("/graphql");
                    return http.postToFullUrl(
                            fullUrl,
                            HttpConfigurations.fullHeader(fullUrl, "/graphql", HttpHeader.EMPTY),
                            requestBody
                    );
                }));
        if (httpResponse.getStatusCode() != 200) {
            throw new AssertionError("Error introspecting GraphQL, status code was " + httpResponse.getStatusCode());
        }

        GraphQLResponse response = JsonUtils.deserializeAs(httpResponse.getTextContent(), GraphQLResponse.class);

        Set<GraphQLQuery> queries = new HashSet<>();
        BiConsumer<Optional<GraphQLType>, GraphQLQueryType> registerTypes = (type, queryType) ->
                type.ifPresent(t ->
                        t.getFields()
                                .stream()
                                .map(GraphQLField::getName)
                                .filter(name -> !name.startsWith("_"))
                                .forEach(name -> queries.add(new GraphQLQuery(name, queryType))));
        registerTypes.accept(response.getData().getSchema().getQueryType(), GraphQLQueryType.QUERY);
        registerTypes.accept(response.getData().getSchema().getMutationType(), GraphQLQueryType.MUTATION);
        registerTypes.accept(response.getData().getSchema().getSubscriptionType(), GraphQLQueryType.SUBSCRIPTION);

        return queries;
    }
}
