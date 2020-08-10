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

import graphql.ExecutionInput;
import graphql.ParseAndValidate;
import graphql.ParseAndValidateResult;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.utils.FileUtils;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;

public class GraphQLSchema {
    private final boolean isSchemaDefined;
    private final Set<GraphQLOperation> schemaDeclaredOperations;

    public GraphQLSchema(String schemaPathStr) {
        isSchemaDefined = !schemaPathStr.isEmpty();
        schemaDeclaredOperations = new HashSet<>();

        if (isSchemaDefined) {
            SchemaParser schemaParser = new SchemaParser();
            Path schemaPath = Paths.get(schemaPathStr);
            TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(FileUtils.fileTextContent(schemaPath));

            RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring().build();

            SchemaGenerator schemaGenerator = new SchemaGenerator();
            graphql.schema.GraphQLSchema schema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

            BiConsumer<Optional<GraphQLObjectType>, GraphQLOperationType> registerTypes = (objectType, operationType) ->
                    objectType.ifPresent(type ->
                            type.getFieldDefinitions().forEach(definition -> schemaDeclaredOperations.add(
                                    new GraphQLOperation(definition.getName(), operationType)
                            )));
            registerTypes.accept(Optional.ofNullable(schema.getQueryType()), GraphQLOperationType.QUERY);
            registerTypes.accept(Optional.ofNullable(schema.getMutationType()), GraphQLOperationType.MUTATION);
            registerTypes.accept(Optional.ofNullable(schema.getSubscriptionType()), GraphQLOperationType.SUBSCRIPTION);
        }
    }

    public boolean isSchemaDefined() {
        return isSchemaDefined;
    }

    public Stream<GraphQLOperation> getSchemaDeclaredOperations() {
        return schemaDeclaredOperations.stream();
    }

    public Set<GraphQLOperation> findOperations(HttpRequestBody requestBody) {
        if (requestBody.isBinary()) {
            return emptySet();
        }

        Map<String, ?> request = JsonUtils.deserializeAsMap(requestBody.asString());
        String query = (String) request.get("query");
        String operationName = (String) request.get("operationName");

        ExecutionInput executionInput = ExecutionInput.newExecutionInput(query).build();
        ParseAndValidateResult parsingResult = ParseAndValidate.parse(executionInput);
        if (parsingResult.isFailure()) {
            return emptySet();
        }

        List<OperationDefinition> operations = parsingResult.getDocument().getDefinitionsOfType(OperationDefinition.class);
        if (operationName != null) {
            List<OperationDefinition> matchingOperations = operations.stream().filter(operationDefinition -> operationName.equals(operationDefinition.getName())).collect(Collectors.toList());
            if (matchingOperations.size() != 1) {
                // Either no matching operation or more than one, either way it's not valid GraphQL
                return emptySet();
            }

            Optional<OperationDefinition> matchingOperation = matchingOperations.stream().findFirst();
            return matchingOperation.map(GraphQLSchema::extractOperations).orElseGet(Collections::emptySet);
        } else {
            if (operations.size() > 1) {
                // This is not valid in GraphQL, if you have more than one operation, you need to specify a name
                return emptySet();
            }
            Optional<OperationDefinition> operation = operations.stream().findFirst();
            return operation.map(GraphQLSchema::extractOperations).orElseGet(Collections::emptySet);
        }
    }

    private static Set<GraphQLOperation> extractOperations(OperationDefinition operationDefinition) {
        List<Field> fields = operationDefinition.getSelectionSet().getSelectionsOfType(Field.class);
        GraphQLOperationType type = convertType(operationDefinition.getOperation());
        return fields.stream()
                .map(field -> new GraphQLOperation(field.getName(), type))
                .collect(Collectors.toSet());
    }

    private static GraphQLOperationType convertType(OperationDefinition.Operation op) {
        switch (op) {
            case MUTATION:
                return GraphQLOperationType.MUTATION;
            case SUBSCRIPTION:
                return GraphQLOperationType.SUBSCRIPTION;
            case QUERY:
            default:
                return GraphQLOperationType.QUERY;
        }
    }
}
