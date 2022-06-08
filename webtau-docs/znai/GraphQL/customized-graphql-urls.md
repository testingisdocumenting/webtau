# Customizing the GraphQL Endpoint

By default, WebTau assumes that all requests are made to a `/graphql` endpoint. The config value `graphQLEndpoint` can be set to use a non-standard endpoint, e.g. `graphQLEndpoint=/api/graphql`.

WebTau also decorates the query with a (typically ignored) query parameter that contains the request's `operationName`, e.g. `/graphql?operation=myOperation`. This makes execution logs as well as WebTau's step reports easier to read and debug, especially since the operation's name is part of the request's payload and is usually not logged by request loggers.
If you want to turn this feature off, set the config value for `graphQLShowOperationAsQueryParam` to `false`.

In order to customize the graphQL URL in different ways, you need to implement a `GraphQLHttpConfiguration` and ensure it gets loaded at runtime via the Java ServiceLoader.

Here is an example in groovy:

:include-file: org/testingisdocumenting/webtau/graphql/CustomGraphQLHttpConfiguration.groovy {title: "Customized GraphQLHttpConfiguration adding the operation to the URL"} 
