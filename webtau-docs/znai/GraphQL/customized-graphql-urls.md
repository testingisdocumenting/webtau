# Customizing the GraphQL Endpoint

By default, webtau assumes that all requests are made to a `/graphql` endpoint. 
In some cases it may make sense to customize this URL, e.g. to hit a non-standard endpoint or
to decorate the query with an ignored query parameter that contains the query's `operationName`. Execution logs as well as webtau's step reports will then become easier to read and debug, especially since the operation's name is in the request's payload and is usually not logged by request loggers.

In order to change the URL, you need to implement a `GraphQLHttpConfiguration` and ensure it gets loaded at runtime via the Java ServiceLoader.

Here is an example in groovy:

:include-file: org/testingisdocumenting/webtau/graphql/CustomGraphQLHttpConfiguration.groovy {title: "Customized GraphQLHttpConfiguration adding the operation to the URL"} 
