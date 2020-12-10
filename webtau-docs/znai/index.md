# WebTau 

WebTau (**Web** **T**est **au**tomation) - concise and expressive way to write end-to-end and unit tests.

:include-image: webtau-logo.png {width: 256, align: "left"}

Test your application across multiple layers:
* REST API
* GraphQL API
* Web UI
* CLI
* Database
* Business Logic (JVM only)

Use one layer to re-inforce tests on another. E.g. REST API layer to set up data for Web UI test, or database layer
to validate GraphQL API.

Tests can be written in any JVM language. Language specific syntactic sugar is available for `Groovy`.

Use powerful [REPL](REPL/experiments) mode to significantly speed up end-to-end tests development. 

# Quick Links

* [Quick Examples](getting-started/quick-examples)
* [Groovy Runner](getting-started/groovy-runner)
* [JUnit4](getting-started/junit4)
* [JUnit5](getting-started/junit5)

## Rest API

WebTau provides a succinct DSL for querying REST APIs and asserting on responses.

``````tabs

Groovy:
 ````columns

 left:
 :include-file: scenarios/rest/simpleGet.groovy {title: "REST API test (Groovy specific runner)"}
 :include-file: com/example/tests/junit4/WeatherGroovyIT.groovy {title: "REST API test (JUnit4)"} 

 right: 
 ```json {title: "Server Response"}
 {
   "temperature": 88
 }
```
````
Java:
 ````columns

 left:
:include-file:  com/example/tests/junit4/WeatherJavaIT.java {title: "REST API test (JUnit4 Java)"} 

 right: 
 ```json {title: "Server Response"}
 {
   "temperature": 88
 }
```
````

``````

## GraphQL API

Similar to REST APIs, WebTau also provides the ability to test GraphQL servers with its GraphQL DSL.

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

## Web UI

UI tests follow an identical pattern to API tests.

:include-file: scenarios/ui/searchWithPages.groovy {title: "Web UI test"}


