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

package org.testgisdocumenting.webtau.graphql;

import graphql.ExecutionInput;
import graphql.ParseAndValidate;
import graphql.ParseAndValidateResult;
import graphql.language.OperationDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.utils.FileUtils;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class GraphQLSchema {
    private final boolean isSchemaDefined;
    private final Set<GraphQLOperation> schemaDeclaredOperations;

    public GraphQLSchema(String schemaUrl) {
        isSchemaDefined = !schemaUrl.isEmpty();
        schemaDeclaredOperations = new HashSet<>();

        if (isSchemaDefined) {
            SchemaParser schemaParser = new SchemaParser();
            Path schemaPath = Paths.get(URI.create(schemaUrl));
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

    public Optional<GraphQLOperation> findOperation(HttpRequestBody requestBody) {
        if (requestBody.isBinary()) {
            return Optional.empty();
        }

        Map<String, ?> request = JsonUtils.deserializeAsMap(requestBody.asString());
        String query = (String) request.get("query");

        ExecutionInput executionInput = ExecutionInput.newExecutionInput(query).build();
        ParseAndValidateResult parsingResult = ParseAndValidate.parse(executionInput);
        if (parsingResult.isFailure()) {
            return Optional.empty();
        }

        List<OperationDefinition> operations = parsingResult.getDocument().getDefinitionsOfType(OperationDefinition.class);
        Optional<OperationDefinition> operation = operations.stream().findFirst();

        return operation
                .map(op -> new GraphQLOperation(op.getName(), convertType(op.getOperation())))
                .filter(schemaDeclaredOperations::contains);
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
