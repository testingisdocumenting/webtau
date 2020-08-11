package org.testingisdocumenting.webtau.graphql;

import org.testingisdocumenting.webtau.graphql.model.introspection.GraphQLField;
import org.testingisdocumenting.webtau.graphql.model.introspection.GraphQLResponse;
import org.testingisdocumenting.webtau.graphql.model.introspection.GraphQLType;
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandlers;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import static org.testingisdocumenting.webtau.graphql.GraphQL.graphql;
import static org.testingisdocumenting.webtau.graphql.model.GraphQLRequest.INTROSPECTION_QUERY;

public class GraphQLSchemaLoader {
    public static Set<GraphQLQuery> fetchSchemaDeclaredQueries() {
        GraphQLResponse response = HttpValidationHandlers.without(
                GraphQLCoverageRecorder.class,
                () -> JsonUtils.convert(graphql.execute(INTROSPECTION_QUERY, (header, body) -> body), GraphQLResponse.class));

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
