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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testingisdocumenting.webtau.http.validation.HttpValidationResult;
import org.testingisdocumenting.webtau.utils.JsonUtils;

public class GraphQLCoverage {
    private final GraphQLSchema schema;
    private final GraphQLCoveredQueries coveredQueries = new GraphQLCoveredQueries();
    private final AtomicBoolean hadQueries = new AtomicBoolean();

    public GraphQLCoverage(GraphQLSchema schema) {
        this.schema = schema;
    }

    public void recordQuery(HttpValidationResult validationResult) {
        if (!validationResult.getUrl().equals(GraphQL.GRAPHQL_URL) || !schema.isSchemaDefined()) {
            return;
        }

        hadQueries.set(true);

        Set<GraphQLQuery> graphQLQueries = schema.findQueries(validationResult);
        graphQLQueries.forEach(query -> coveredQueries.
            add(query, validationResult.getId(), validationResult.getElapsedTime(), isErrorResult(validationResult)));
    }

    private boolean isErrorResult(HttpValidationResult validationResult) {
      try {
        return JsonUtils.convertToTree(JsonUtils.deserialize(validationResult.getResponse().getTextContent()))
            .has("errors");
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }

    Stream<GraphQLQuery> nonCoveredQueries() {
        if (!hadQueries.get()) {
            return Stream.empty();
        }

        return schema.getSchemaDeclaredQueries().filter(o -> !coveredQueries.contains(o));
    }

    Stream<GraphQLQuery> coveredQueries() {
        if (!hadQueries.get()) {
            return Stream.empty();
        }

        return coveredQueries.coveredQueries();
    }

    Stream<GraphQLQuery> coveredSuccessBranches() {
        if (!hadQueries.get()) {
            return Stream.empty();
        }

        return coveredQueries.coveredSuccessBranches();
    }

    Stream<GraphQLQuery> nonCoveredSuccessBranches() {
        if (!hadQueries.get()) {
            return Stream.empty();
        }

        List<GraphQLQuery> coveredSuccessBranches = coveredQueries.coveredSuccessBranches().collect(Collectors.toList());
        return schema.getSchemaDeclaredQueries().filter(o -> coveredSuccessBranches.stream().noneMatch(graphQLQuery -> graphQLQuery.equals(o)));
    }

    Stream<GraphQLQuery> coveredErrorBranches() {
        if (!hadQueries.get()) {
            return Stream.empty();
        }

        return coveredQueries.coveredErrorBranches();
    }

    Stream<GraphQLQuery> nonCoveredErrorBranches() {
        if (!hadQueries.get()) {
            return Stream.empty();
        }

        List<GraphQLQuery> coveredErrorBranches = coveredQueries.coveredErrorBranches().collect(Collectors.toList());
        return schema.getSchemaDeclaredQueries().filter(o -> coveredErrorBranches.stream().noneMatch(graphQLQuery -> graphQLQuery.equals(o)));
    }

    Stream<GraphQLQuery> declaredQueries() {
        if (!hadQueries.get()) {
            return Stream.empty();
        }

        return schema.getSchemaDeclaredQueries();
    }

    Stream<Map.Entry<GraphQLQuery, Set<GraphQLCoveredQueries.Call>>> actualCalls() {
        if (!hadQueries.get()) {
            return Stream.empty();
        }

        return coveredQueries.getActualCalls();
    }
}
