package org.testingisdocumenting.webtau.graphql.listener;

import org.testingisdocumenting.webtau.http.HttpHeader;

import java.util.List;
import java.util.Map;

public interface GraphQLListener {
    /**
     * called once right before first <code>execute</code> call
     */
    default void beforeFirstGraphQLQuery() {
    }

    /**
     * called before each graphql query
     */
    default void beforeGraphQLQuery(String query,
                                    Map<String, Object> variables,
                                    String operationName,
                                    HttpHeader requestHeader) {
    }

    /**
     * called after each graphql query
     */
    default void afterGraphQLQuery(String query,
                                   Map<String, Object> variables,
                                   String operationName,
                                   HttpHeader requestHeader,
                                   Map<String, Object> data,
                                   List<Object> errors) {
    }
}
