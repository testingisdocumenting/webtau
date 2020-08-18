# WebTau 

WebTau (**Web** **T**est **au**tomation) - concise and expressive way to create REST API, GraphQL API and Web UI tests.

Tests can be written in any JVM language. Language specific syntactic sugar is available for `Groovy`.

Tests can take the form of either scenario files, executed via a command line tool or Maven plugin, or JUnit tests.

:include-image: webtau-logo.png {width: 256, align: "left"}

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


