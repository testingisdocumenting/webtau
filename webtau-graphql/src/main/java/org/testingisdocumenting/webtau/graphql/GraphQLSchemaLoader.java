package org.testingisdocumenting.webtau.graphql;

import org.testingisdocumenting.webtau.graphql.model.introspection.Field;
import org.testingisdocumenting.webtau.graphql.model.introspection.Response;
import org.testingisdocumenting.webtau.graphql.model.introspection.Type;
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandlers;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import static org.testingisdocumenting.webtau.graphql.GraphQL.graphql;
import static org.testingisdocumenting.webtau.graphql.model.Request.INTROSPECTION_QUERY;

public class GraphQLSchemaLoader {
    public static Set<GraphQLQuery> fetchSchemaDeclaredQueries() {
        Response response = HttpValidationHandlers.without(
                GraphQLCoverageRecorder.class,
                () -> JsonUtils.convert(graphql.execute(INTROSPECTION_QUERY, (header, body) -> body), Response.class));

        Set<GraphQLQuery> queries = new HashSet<>();
        BiConsumer<Optional<Type>, GraphQLQueryType> registerTypes = (type, queryType) ->
                type.ifPresent(t ->
                        t.getFields()
                                .stream()
                                .map(Field::getName)
                                .filter(name -> !name.startsWith("_"))
                                .forEach(name -> queries.add(new GraphQLQuery(name, queryType))));
        registerTypes.accept(response.getData().getSchema().getQueryType(), GraphQLQueryType.QUERY);
        registerTypes.accept(response.getData().getSchema().getMutationType(), GraphQLQueryType.MUTATION);
        registerTypes.accept(response.getData().getSchema().getSubscriptionType(), GraphQLQueryType.SUBSCRIPTION);

        return queries;
    }
}
