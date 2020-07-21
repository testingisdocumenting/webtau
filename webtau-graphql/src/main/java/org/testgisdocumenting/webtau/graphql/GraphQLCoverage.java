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

import org.testingisdocumenting.webtau.http.validation.HttpValidationResult;

import java.util.Optional;
import java.util.stream.Stream;

public class GraphQLCoverage {
    private final GraphQLSchema schema;
    private final GraphQLCoveredOperations coveredOperations = new GraphQLCoveredOperations();

    public GraphQLCoverage(GraphQLSchema schema) {
        this.schema = schema;
    }

    public void recordOperation(HttpValidationResult validationResult) {
        if (!schema.isSchemaDefined() || !validationResult.getRequestMethod().equals("POST")) {
            return;
        }

        Optional<GraphQLOperation> graphQLOperation = schema.findOperation(validationResult.getRequestBody());
        graphQLOperation.ifPresent(operation -> coveredOperations.add(operation, validationResult.getResponseStatusCode(), validationResult.getId()));
    }

    Stream<GraphQLOperation> nonCoveredOperations() {
        return schema.getSchemaDeclaredOperations().filter(o -> !coveredOperations.contains(o));
    }

    Stream<GraphQLOperation> coveredOperations() {
        return coveredOperations.coveredOperations();
    }
}
