# Executing Queries and Mutations

Webtau follows GraphQL's [Serving over HTTP best practices](https://graphql.org/learn/serving-over-http/) when invoking
GraphQL servers over HTTP.

It therefore assumes the server responds to requests to `/graphql` so you do not need to specify that in the URL in your configuration.
Requests allow providing:
* a query/mutation string
* variables
* an operation name

Webtau will default to issuing `POST` requests according to the [best practices](https://graphql.org/learn/serving-over-http/#post-request)
and will expect a 200 status code and a response with a `data` or `errors` field.

The following example demonstrates most of these query features:
:include-file: scenarios/graphql/queryAndMutation.groovy {title: "GraphQL example with a query, a mutation and variables", commentsType: "inline"}

# Response Assertions

Response assertions follow a similar pattern to REST APIs.

For Groovy specifically, there are shortcuts for accessing data in the response directly as demonstrated in the example above.
You may access errors directly via `errors` or fields in the response directly with the field names, omitting the `data` field.
