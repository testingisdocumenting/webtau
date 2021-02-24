# Customizing the GraphQL Endpoint

By default, webtau assumes that all requests are made to a `/graphql` endpoint. The config value `graphqlEndpoint` can be set to use a non-standard endpoint.

Webtau also decorates the query with an (typically ignored) query parameter that contains the request's `operationName`, e.g. `/graphql?operation=myOperation`. This makes execution logs as well as webtau's step reports easier to read and debug, especially since the operation's name is in the request's payload and is usually not logged by request loggers.
If you want to turn this feature off, set the config value for `showGraphqlOperationAsQueryParam` to `false`.

In order to customize the graphQL URL in different ways, you need to implement a `GraphQLHttpConfiguration` and ensure it gets loaded at runtime via the Java ServiceLoader.

Here is an example in groovy:

:include-file: org/testingisdocumenting/webtau/graphql/CustomGraphQLHttpConfiguration.groovy {title: "Customized GraphQLHttpConfiguration adding the operation to the URL"} 
