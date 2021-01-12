/*
 * Copyright 2021 webtau maintainers
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
                                   Map<String, Object> responseData,
                                   List<Object> errors) {
    }
}
