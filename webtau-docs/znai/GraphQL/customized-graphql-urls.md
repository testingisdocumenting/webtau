# Customizing the GraphQL Endpoint

By default, Webtau assumes that all requests are made to a `/graphql` endpoint. 
In some cases it may make sense to customize this URL, e.g. to hit a non-standard endpoint or
to decorate the query with an ignored query parameter that contains the query's `operationName`. Execution logs as well as Webtau's step reports will then become easier to read and debug, especially since the operation's name is in the request's payload and is usually not logged by request loggers.

In order to change the URL, you need to implement a `GraphQLHttpConfiguration` and ensure it gets loaded at runtime via the Java ServiceLoader.

Here is an example for such a `GraphQLHttpConfiguration` in groovy:
```groovy
private withOperationAsQueryParam = new GraphQLHttpConfiguration() {
    @Override
    String requestUrl(String url, GraphQLRequest graphQLRequest) {
        if (null != graphQLRequest.operationName && !graphQLRequest.operationName.isEmpty()) {
            return "/my-custom-graphql-endpoint?operation=${graphQLRequest.operationName}"
        }
        return url
    }
}
```
