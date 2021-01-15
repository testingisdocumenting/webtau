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

import org.testingisdocumenting.webtau.graphql.model.GraphQLRequest;
import org.testingisdocumenting.webtau.graphql.model.GraphQLResponse;
import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.HttpResponse;
import org.testingisdocumenting.webtau.http.listener.HttpListener;
import org.testingisdocumenting.webtau.http.listener.HttpListeners;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.testingisdocumenting.webtau.utils.UrlUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GraphQLListeners {
    private static final List<WrappedGraphQLListener> listeners = ServiceLoaderUtils.load(GraphQLListener.class)
            .stream().map(WrappedGraphQLListener::new).collect(Collectors.toList());

    private GraphQLListeners() {
    }

    public static void beforeFirstGraphQLQuery() {
        listeners.forEach(l -> l.listener.beforeFirstGraphQLQuery());
    }

    public static void beforeGraphQLQuery(String query,
                                          Map<String, Object> variables,
                                          String operationName,
                                          HttpHeader requestHeader) {
        listeners.forEach(l -> l.listener.beforeGraphQLQuery(query, variables, operationName, requestHeader));
    }

    public static void add(GraphQLListener listener) {
        listeners.add(new WrappedGraphQLListener(listener));
    }

    public static void remove(GraphQLListener listener) {
        listeners.stream()
                .filter(l -> l.listener == listener)
                .findFirst()
                .ifPresent(l -> {
                    HttpListeners.remove(l);
                    listeners.remove(l);
                });
    }

    private static class WrappedGraphQLListener implements GraphQLListener, HttpListener {
        private final GraphQLListener listener;

        private WrappedGraphQLListener(GraphQLListener listener) {
            this.listener = listener;
            HttpListeners.add(this);
        }

        @Override
        public void beforeFirstGraphQLQuery() {
            listener.beforeFirstGraphQLQuery();
        }

        @Override
        public void beforeGraphQLQuery(String query, Map<String, Object> variables, String operationName, HttpHeader requestHeader) {
            listener.beforeGraphQLQuery(query, variables, operationName, requestHeader);
        }

        @Override
        public void afterHttpCall(String requestMethod,
                                  String passedUrl,
                                  String fullUrl,
                                  HttpHeader requestHeader,
                                  HttpRequestBody requestBody,
                                  HttpResponse httpResponse) {
            Optional<GraphQLRequest> graphQLRequest = GraphQLRequest.fromHttpRequest(requestMethod, UrlUtils.extractPath(fullUrl), requestBody);
            graphQLRequest.ifPresent(request ->
                    GraphQLResponse.from(httpResponse).ifPresent(response ->
                            listener.afterGraphQLQuery(
                                    request.getQuery(),
                                    request.getVariables(),
                                    request.getOperationName(),
                                    requestHeader,
                                    response.getData(),
                                    response.getErrors())));
        }
    }
}
