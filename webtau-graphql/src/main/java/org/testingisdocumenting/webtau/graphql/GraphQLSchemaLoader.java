package org.testingisdocumenting.webtau.graphql;

import org.testingisdocumenting.webtau.graphql.model.GraphQLRequest;
import org.testingisdocumenting.webtau.graphql.model.introspection.GraphQLField;
import org.testingisdocumenting.webtau.graphql.model.introspection.GraphQLResponse;
import org.testingisdocumenting.webtau.graphql.model.introspection.GraphQLType;
import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.HttpResponse;
import org.testingisdocumenting.webtau.http.config.HttpConfigurations;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
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
        HttpResponse httpResponse = http.postToFullUrl(HttpConfigurations.fullUrl("/graphql"), HttpHeader.EMPTY, requestBody);
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
