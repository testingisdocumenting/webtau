Webtau `graphql.` module let you exercise and validate GraphQL API.
It provides a simplified way to access JSON response of an end-point and provides DSL to execute queries and mutations.

``````tabs

Groovy:
 ````columns

 left:
 :include-file: scenarios/graphql/weatherQuery.groovy {title: "GraphQL API test (Groovy specific runner)"}
 :include-file: com/example/tests/junit4/GraphQLWeatherGroovyIT.groovy {title: "GraphQL API test (JUnit4)"} 

 right: 
 ```json {title: "Server Response"}
{
  "data": {
    "weather": {
      "temperature": 88
    }
  }
}
```
````
Java:
 ````columns

 left:
:include-file:  com/example/tests/junit4/GraphQLWeatherJavaIT.java {title: "GraphQL API test (JUnit4 Java)"} 

 right: 
 ```json {title: "Server Response"}
{
  "data": {
    "weather": {
      "temperature": 88
    }
  }
}
```
````

``````


Before diving further into writing tests for your GraphQL server, please read through the HTTP testing documentation
starting with the [Data node page](HTTP/data-node) as much of the same core principles apply to GraphQL also.

The main GraphQL specific features are covered in the subsequent pages:
* [Queries and Mutations](GraphQL/queries-and-mutations)
* [Report](GraphQL/report)
