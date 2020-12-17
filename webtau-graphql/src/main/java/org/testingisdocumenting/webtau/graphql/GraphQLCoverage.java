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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult;
import org.testingisdocumenting.webtau.utils.JsonUtils;

public class GraphQLCoverage {
    private final GraphQLSchema schema;
    private final GraphQLCoveredQueries coveredQueries = new GraphQLCoveredQueries();

    public GraphQLCoverage(GraphQLSchema schema) {
        this.schema = schema;
    }

    public void recordQuery(HttpValidationResult validationResult) {
        if (!schema.isSchemaDefined()
                || !validationResult.getRequestMethod().equals("POST")
                || !validationResult.getUrl().equals("/graphql")) {
            return;
        }

        Set<GraphQLQuery> graphQLQueries = schema.findQueries(validationResult.getRequestBody());
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
          return schema.getSchemaDeclaredQueries().filter(o -> !coveredQueries.contains(o));
    }

    Stream<GraphQLQuery> coveredQueries() {
        return coveredQueries.coveredQueries();
    }

    Stream<GraphQLQuery> coveredSuccessBranches() {
        return coveredQueries.coveredSuccessBranches();
    }

    Stream<GraphQLQuery> nonCoveredSuccessBranches() {
        List<GraphQLQuery> coveredSuccessBranches = coveredQueries.coveredSuccessBranches().collect(Collectors.toList());
        return schema.getSchemaDeclaredQueries().filter(o -> coveredSuccessBranches.stream().noneMatch(graphQLQuery -> graphQLQuery.equals(o)));
    }

    Stream<GraphQLQuery> coveredErrorBranches() {
        return coveredQueries.coveredErrorBranches();
    }

    Stream<GraphQLQuery> nonCoveredErrorBranches() {
        List<GraphQLQuery> coveredErrorBranches = coveredQueries.coveredErrorBranches().collect(Collectors.toList());;
        return schema.getSchemaDeclaredQueries().filter(o -> coveredErrorBranches.stream().noneMatch(graphQLQuery -> graphQLQuery.equals(o)));
    }

    Stream<GraphQLQuery> declaredQueries() {
        return schema.getSchemaDeclaredQueries();
    }

    Stream<Map.Entry<GraphQLQuery, Set<GraphQLCoveredQueries.Call>>> actualCalls() {
        return coveredQueries.getActualCalls();
    }
}
