# Webtau 

Webtau (**Web** **T**est **au**tomation) - concise and expressive way to write end-to-end and unit tests.

:include-image: webtau-logo.png {width: 256, align: "left"}

Test your application across multiple layers:
* REST API
* GraphQL API
* Web UI
* CLI
* Database
* Business Logic (JVM only)

Use one layer to re-enforce tests on another. E.g. REST API layer to set up data for Web UI test, or database layer
to validate GraphQL API.

Tests can be written in any JVM language. Language specific syntactic sugar is available for `Groovy`.

Use powerful [REPL](REPL/experiments) mode to significantly speed up end-to-end tests development. 

* [REST API example](#rest-api-example)
* [GraphQL API example](#graphql-api-example)
* [Web UI example](#web-ui-example)
* [DB example](#db-example)
* [CLI example](#cli-example)

# Rest API Example 

Webtau provides a succinct DSL for exercising HTTP end points (e.g. REST APIs) and provides simplified way to 
assert responses.

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

# GraphQL API Example 

Similar to HTTP APIs, Webtau also provides the ability to test GraphQL servers with its GraphQL DSL.

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

# Web UI Example 

:include-file: scenarios/ui/searchWithPagesWaitTo.groovy/ {title: "Web UI test"}

:include-groovy: pages/SearchPage.groovy {title: "SearchPage.groovy"}

# DB Example

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseFacadeTest.groovy {
    entry: "use table data permute, above and guid to generate rows",
    title: "DB data setup example",
    bodyOnly: true,
    startLine: "def PRICES",
    endLine: "cell.above + 20",
    commentsType: "inline"
}

# CLI Example

:include-file: doc-artifacts/snippets/foreground-cli/withOutputValidation.groovy {
  title: "output validation"
}
