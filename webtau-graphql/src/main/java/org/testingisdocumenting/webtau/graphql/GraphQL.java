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
import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidatorWithReturn;

import java.util.Map;

import static org.testingisdocumenting.webtau.Matchers.equal;
import static org.testingisdocumenting.webtau.http.Http.http;

public class GraphQL {
    public static final GraphQL graphql = new GraphQL();

    static final String GRAPHQL_URL = "/graphql";
    private static final int SUCCESS_CODE = 200;

    private static GraphQLSchema schema;
    private static GraphQLCoverage coverage;

    static GraphQLCoverage getCoverage() {
        return coverage;
    }

    public static GraphQLSchema getSchema() {
        return schema;
    }

    static void reset() {
        schema = new GraphQLSchema(GraphQLConfig.isEnabled());
        coverage = new GraphQLCoverage(schema);
    }

    public <E> E execute(String query, HttpResponseValidatorWithReturn validator) {
        return execute(query, null, null, HttpHeader.EMPTY, validator);
    }

    public <E> E execute(String query, Map<String, Object> variables, HttpResponseValidatorWithReturn validator) {
        return execute(query, variables, null, HttpHeader.EMPTY, validator);
    }

    public <E> E execute(String query, Map<String, Object> variables, String operationName, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return http.post(GRAPHQL_URL, header, GraphQLRequest.body(query, variables, operationName), (headerDataNode, body) -> {
            headerDataNode.statusCode().should(equal(SUCCESS_CODE));
            return validator.validate(headerDataNode, body);
        });
    }
}
