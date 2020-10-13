package org.testingisdocumenting.webtau.graphql;

import org.junit.Test;

import static org.testingisdocumenting.webtau.graphql.GraphQL.graphql;

public class GraphQLJavaOverloadsTest extends GraphQLTestBase {
    @Test
    public void executeWithoutValidationSyntaxCheck() {
        graphql.execute(QUERY);
        graphql.execute(MULTI_OP_QUERY, OP_NAME);
        graphql.execute(QUERY_WITH_VARS, VARS);
        graphql.execute(MULTI_OP_QUERY_WITH_VARS, VARS, OP_NAME);

        testServer.getHandler().withAuthEnabled(AUTH_HEADER_VALUE, () -> {
            graphql.execute(QUERY, AUTH_HEADER);
            graphql.execute(MULTI_OP_QUERY, OP_NAME, AUTH_HEADER);
            graphql.execute(QUERY_WITH_VARS, VARS, AUTH_HEADER);
            graphql.execute(MULTI_OP_QUERY_WITH_VARS, VARS, OP_NAME, AUTH_HEADER);
        });
    }

    @Test
    public void executeWithoutReturnOverloads() {
        graphql.execute(QUERY, VALIDATOR);
        graphql.execute(MULTI_OP_QUERY, OP_NAME, VALIDATOR);
        graphql.execute(QUERY_WITH_VARS, VARS, VALIDATOR);
        graphql.execute(MULTI_OP_QUERY_WITH_VARS, VARS, OP_NAME, VALIDATOR);

        testServer.getHandler().withAuthEnabled(AUTH_HEADER_VALUE, () -> {
            graphql.execute(QUERY, AUTH_HEADER, VALIDATOR);
            graphql.execute(MULTI_OP_QUERY, OP_NAME, AUTH_HEADER, VALIDATOR);
            graphql.execute(QUERY_WITH_VARS, VARS, AUTH_HEADER, VALIDATOR);
            graphql.execute(MULTI_OP_QUERY_WITH_VARS, VARS, OP_NAME, AUTH_HEADER, VALIDATOR);
        });
    }

    @Test
    public void executeWithReturnOverloads() {
        String id = graphql.execute(QUERY, VALIDATOR_WITH_RETURN);
        ID_ASSERTION.accept(id);
        id = graphql.execute(MULTI_OP_QUERY, OP_NAME, VALIDATOR_WITH_RETURN);
        ID_ASSERTION.accept(id);
        id = graphql.execute(QUERY_WITH_VARS, VARS, VALIDATOR_WITH_RETURN);
        ID_ASSERTION.accept(id);
        id = graphql.execute(MULTI_OP_QUERY_WITH_VARS, VARS, OP_NAME, VALIDATOR_WITH_RETURN);
        ID_ASSERTION.accept(id);

        testServer.getHandler().withAuthEnabled(AUTH_HEADER_VALUE, () -> {
            String id2 = graphql.execute(QUERY, AUTH_HEADER, VALIDATOR_WITH_RETURN);
            ID_ASSERTION.accept(id2);
            id2 = graphql.execute(MULTI_OP_QUERY, OP_NAME, AUTH_HEADER, VALIDATOR_WITH_RETURN);
            ID_ASSERTION.accept(id2);
            id2 = graphql.execute(QUERY_WITH_VARS, VARS, AUTH_HEADER, VALIDATOR_WITH_RETURN);
            ID_ASSERTION.accept(id2);
            id2 = graphql.execute(MULTI_OP_QUERY_WITH_VARS, VARS, OP_NAME, AUTH_HEADER, VALIDATOR_WITH_RETURN);
            ID_ASSERTION.accept(id2);
        });
    }
}
