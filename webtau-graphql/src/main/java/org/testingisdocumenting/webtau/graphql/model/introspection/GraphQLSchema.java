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

package org.testingisdocumenting.webtau.graphql.model.introspection;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class GraphQLSchema {
    private final Optional<GraphQLType> queryType;
    private final Optional<GraphQLType> mutationType;
    private final Optional<GraphQLType> subscriptionType;

    public GraphQLSchema(@JsonProperty("queryType") Optional<GraphQLType> queryType,
                         @JsonProperty("mutationType") Optional<GraphQLType> mutationType,
                         @JsonProperty("subscriptionType") Optional<GraphQLType> subscriptionType) {
        this.queryType = queryType;
        this.mutationType = mutationType;
        this.subscriptionType = subscriptionType;
    }

    public Optional<GraphQLType> getQueryType() {
        return queryType;
    }

    public Optional<GraphQLType> getMutationType() {
        return mutationType;
    }

    public Optional<GraphQLType> getSubscriptionType() {
        return subscriptionType;
    }
}
