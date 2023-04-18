Before diving further into writing tests for your GraphQL server, please read through the HTTP testing documentation
starting with the [Data node page](HTTP/data-node) as much of the same core principles apply to GraphQL also.

The main GraphQL specific features are covered in the subsequent pages:
* [Queries and Mutations](GraphQL/queries-and-mutations)
* [Customized GraphQL URLs](GraphQL/customized-graphql-urls)
* [Report](GraphQL/report)
 
# Executing Queries and Mutations

WebTau follows GraphQL's [Serving over HTTP best practices](https://graphql.org/learn/serving-over-http/) when invoking
GraphQL servers over HTTP.

It therefore assumes the server responds to requests to `/graphql` so you do not need to specify that in the URL in your configuration.
Requests allow providing:
* a query/mutation string
* variables
* an operation name

WebTau will default to issuing `POST` requests according to the [best practices](https://graphql.org/learn/serving-over-http/#post-request)
and will expect a 200 status code and a response with a `data` or `errors` field.

The following example demonstrates most of these query features:
:include-file: scenarios/graphql/queryAndMutation.groovy {title: "GraphQL example with a query, a mutation and variables", commentsType: "inline"}

# Response Assertions

Response assertions follow a similar pattern to REST APIs.

For Groovy specifically, there are shortcuts for accessing data in the response directly as demonstrated in the example above.
You may access errors directly via `errors` or fields in the response directly with the field names, omitting the `data` field.
