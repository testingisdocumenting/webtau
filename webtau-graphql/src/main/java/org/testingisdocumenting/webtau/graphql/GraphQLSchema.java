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

import com.google.common.base.Suppliers;
import graphql.ExecutionInput;
import graphql.ParseAndValidate;
import graphql.ParseAndValidateResult;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import org.testingisdocumenting.webtau.graphql.model.GraphQLRequest;
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;

public class GraphQLSchema {
    private final Supplier<Optional<Set<GraphQLQuery>>> schemaDeclaredQueriesSupplier;

    public GraphQLSchema() {
        this.schemaDeclaredQueriesSupplier = Suppliers.memoize(GraphQLSchemaLoader::fetchSchemaDeclaredQueries);
    }

    public GraphQLSchema(Set<GraphQLQuery> schemaDeclaredQueries) {
        this.schemaDeclaredQueriesSupplier = () -> Optional.of(schemaDeclaredQueries);
    }

    public boolean isSchemaDefined() {
        return schemaDeclaredQueriesSupplier.get().isPresent();
    }

    public Stream<GraphQLQuery> getSchemaDeclaredQueries() {
        return schemaDeclaredQueriesSupplier.get().map(Set::stream).orElseGet(Stream::empty);
    }

    public Set<GraphQLQuery> findQueries(HttpValidationResult validationResult) {
        Optional<GraphQLRequest> graphQLRequest = GraphQLRequest.fromHttpRequest(
                validationResult.getRequestMethod(), validationResult.getUrl(), validationResult.getRequestBody());
        return graphQLRequest.map(r -> findQueries(r.getQuery(), r.getOperationName())).orElseGet(Collections::emptySet);
    }

    Set<GraphQLQuery> findQueries(String query, String operationName) {
        ExecutionInput executionInput = ExecutionInput.newExecutionInput(query).build();
        ParseAndValidateResult parsingResult = ParseAndValidate.parse(executionInput);
        if (parsingResult.isFailure()) {
            return emptySet();
        }

        List<OperationDefinition> operations = parsingResult.getDocument().getDefinitionsOfType(OperationDefinition.class);
        if (operationName != null) {
            List<OperationDefinition> matchingOperations = operations.stream()
                    .filter(operationDefinition -> operationName.equals(operationDefinition.getName()))
                    .collect(Collectors.toList());
            if (matchingOperations.size() != 1) {
                // Either no matching operation or more than one, either way it's not valid GraphQL
                return emptySet();
            }

            Optional<OperationDefinition> matchingOperation = matchingOperations.stream().findFirst();
            return matchingOperation.map(GraphQLSchema::extractQueries).orElseGet(Collections::emptySet);
        } else {
            if (operations.size() > 1) {
                // This is not valid in GraphQL, if you have more than one operation, you need to specify a name
                return emptySet();
            }
            Optional<OperationDefinition> operation = operations.stream().findFirst();
            return operation.map(GraphQLSchema::extractQueries).orElseGet(Collections::emptySet);
        }
    }

    private static Set<GraphQLQuery> extractQueries(OperationDefinition operationDefinition) {
        List<Field> fields = operationDefinition.getSelectionSet().getSelectionsOfType(Field.class);
        GraphQLQueryType type = convertType(operationDefinition.getOperation());
        return fields.stream()
                .map(field -> new GraphQLQuery(field.getName(), type))
                .collect(Collectors.toSet());
    }

    private static GraphQLQueryType convertType(OperationDefinition.Operation op) {
        switch (op) {
            case MUTATION:
                return GraphQLQueryType.MUTATION;
            case SUBSCRIPTION:
                return GraphQLQueryType.SUBSCRIPTION;
            case QUERY:
            default:
                return GraphQLQueryType.QUERY;
        }
    }
}
