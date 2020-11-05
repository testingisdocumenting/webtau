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

import org.testingisdocumenting.webtau.data.traceable.CheckLevel;
import org.testingisdocumenting.webtau.graphql.model.GraphQLRequest;
import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidator;
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidatorIgnoringReturn;
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidatorWithReturn;

import java.util.Map;

import static org.testingisdocumenting.webtau.Matchers.equal;
import static org.testingisdocumenting.webtau.http.Http.http;

public class GraphQL {
    public static final GraphQL graphql = new GraphQL();

    static final String GRAPHQL_URL = "/graphql";
    private static final HttpResponseValidatorWithReturn EMPTY_RESPONSE_VALIDATOR = (header, body) -> null;
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
        schema = new GraphQLSchema();
        coverage = new GraphQLCoverage(schema);
    }

    public void execute(String query) {
        execute(query, EMPTY_RESPONSE_VALIDATOR);
    }

    public void execute(String query, HttpResponseValidator validator) {
        execute(query, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E execute(String query, HttpResponseValidatorWithReturn validator) {
        return execute(query, null, null, HttpHeader.EMPTY, validator);
    }

    public void execute(String query, HttpHeader header) {
        execute(query, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public void execute(String query, HttpHeader header, HttpResponseValidator validator) {
        execute(query, null, null, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E execute(String query, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return execute(query, null, null, header, validator);
    }

    public void execute(String query, String operationName) {
        execute(query, operationName, EMPTY_RESPONSE_VALIDATOR);
    }

    public void execute(String query, String operationName, HttpResponseValidator validator) {
        execute(query, null, operationName, HttpHeader.EMPTY, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E execute(String query, String operationName, HttpResponseValidatorWithReturn validator) {
        return execute(query, null, operationName, HttpHeader.EMPTY, validator);
    }

    public void execute(String query, String operationName, HttpHeader header) {
        execute(query, operationName, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public void execute(String query, String operationName, HttpHeader header, HttpResponseValidator validator) {
        execute(query, null, operationName, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E execute(String query, String operationName, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return execute(query, null, operationName, header, validator);
    }

    public void execute(String query, Map<String, Object> variables) {
        execute(query, variables, EMPTY_RESPONSE_VALIDATOR);
    }

    public void execute(String query, Map<String, Object> variables, HttpResponseValidator validator) {
        execute(query, variables, null, HttpHeader.EMPTY, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E execute(String query, Map<String, Object> variables, HttpResponseValidatorWithReturn validator) {
        return execute(query, variables, null, HttpHeader.EMPTY, validator);
    }

    public void execute(String query, Map<String, Object> variables, HttpHeader header) {
        execute(query, variables, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public void execute(String query, Map<String, Object> variables, HttpHeader header, HttpResponseValidator validator) {
        execute(query, variables, null, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E execute(String query, Map<String, Object> variables, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return execute(query, variables, null, header, validator);
    }

    public void execute(String query, Map<String, Object> variables, String operationName) {
        execute(query, variables, operationName, EMPTY_RESPONSE_VALIDATOR);
    }

    public void execute(String query, Map<String, Object> variables, String operationName, HttpResponseValidator validator) {
        execute(query, variables, operationName, HttpHeader.EMPTY, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E execute(String query, Map<String, Object> variables, String operationName, HttpResponseValidatorWithReturn validator) {
        return execute(query, variables, operationName, HttpHeader.EMPTY, validator);
    }

    public void execute(String query, Map<String, Object> variables, String operationName, HttpHeader header) {
        execute(query, variables, operationName, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public void execute(String query, Map<String, Object> variables, String operationName, HttpHeader header, HttpResponseValidator validator) {
        execute(query, variables, operationName, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E execute(String query, Map<String, Object> variables, String operationName, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return http.post(GRAPHQL_URL, header, GraphQLRequest.body(query, variables, operationName), (headerDataNode, body) -> {
            Object validatorReturnValue = validator.validate(headerDataNode, body);

            if (headerDataNode.statusCode().getTraceableValue().getCheckLevel() == CheckLevel.None) {
                headerDataNode.statusCode().should(equal(SUCCESS_CODE));
            }

            return validatorReturnValue;
        });
    }
}
