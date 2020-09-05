package org.testingisdocumenting.webtau.graphql

import org.junit.Test

import static org.testingisdocumenting.webtau.graphql.GraphQL.graphql

class GraphQLGroovyOverloadsTest extends GraphQLTestBase {
    @Test
    void "execute without validation syntax check"() {
        graphql.execute(QUERY)
        graphql.execute(MULTI_OP_QUERY, OP_NAME)
        graphql.execute(QUERY_WITH_VARS, VARS)
        graphql.execute(MULTI_OP_QUERY_WITH_VARS, VARS, OP_NAME)

        testServer.getHandler().withAuthEnabled(AUTH_HEADER_VALUE) {
            graphql.execute(QUERY, AUTH_HEADER)
            graphql.execute(MULTI_OP_QUERY, OP_NAME, AUTH_HEADER)
            graphql.execute(QUERY_WITH_VARS, VARS, AUTH_HEADER)
            graphql.execute(MULTI_OP_QUERY_WITH_VARS, VARS, OP_NAME, AUTH_HEADER)
        }
    }

    @Test
    void "execute without return overloads"() {
        graphql.execute(QUERY) {
            BODY_ASSERTION.accept(body)
        }

        graphql.execute(MULTI_OP_QUERY, OP_NAME) {
            BODY_ASSERTION.accept(body)
        }

        graphql.execute(QUERY_WITH_VARS, VARS) {
            BODY_ASSERTION.accept(body)
        }

        graphql.execute(MULTI_OP_QUERY_WITH_VARS, VARS, OP_NAME) {
            BODY_ASSERTION.accept(body)
        }

        testServer.getHandler().withAuthEnabled(AUTH_HEADER_VALUE) {
            graphql.execute(QUERY, AUTH_HEADER) {
                BODY_ASSERTION.accept(body)
            }

            graphql.execute(MULTI_OP_QUERY, OP_NAME, AUTH_HEADER) {
                BODY_ASSERTION.accept(body)
            }

            graphql.execute(QUERY_WITH_VARS, VARS, AUTH_HEADER) {
                BODY_ASSERTION.accept(body)
            }

            graphql.execute(MULTI_OP_QUERY_WITH_VARS, VARS, OP_NAME, AUTH_HEADER) {
                BODY_ASSERTION.accept(body)
            }
        }
    }

    @Test
    void "execute with return overloads"() {
        def id = graphql.execute(QUERY) {
            BODY_ASSERTION.accept(body)
            return taskById.id
        }
        ID_ASSERTION.accept(id)

        id = graphql.execute(MULTI_OP_QUERY, OP_NAME) {
            BODY_ASSERTION.accept(body)
            return taskById.id
        }
        ID_ASSERTION.accept(id)

        id = graphql.execute(QUERY_WITH_VARS, VARS) {
            BODY_ASSERTION.accept(body)
            return taskById.id
        }
        ID_ASSERTION.accept(id)

        id = graphql.execute(MULTI_OP_QUERY_WITH_VARS, VARS, OP_NAME) {
            BODY_ASSERTION.accept(body)
            return taskById.id
        }
        ID_ASSERTION.accept(id)

        testServer.getHandler().withAuthEnabled(AUTH_HEADER_VALUE) {
            id = graphql.execute(QUERY, AUTH_HEADER) {
                BODY_ASSERTION.accept(body)
                return taskById.id
            }
            ID_ASSERTION.accept(id)

            id = graphql.execute(MULTI_OP_QUERY, OP_NAME, AUTH_HEADER) {
                BODY_ASSERTION.accept(body)
                return taskById.id
            }
            ID_ASSERTION.accept(id)

            id = graphql.execute(QUERY_WITH_VARS, VARS, AUTH_HEADER) {
                BODY_ASSERTION.accept(body)
                return taskById.id
            }
            ID_ASSERTION.accept(id)

            id = graphql.execute(MULTI_OP_QUERY_WITH_VARS, VARS, OP_NAME, AUTH_HEADER) {
                BODY_ASSERTION.accept(body)
                return taskById.id
            }
            ID_ASSERTION.accept(id)
        }
    }
}
