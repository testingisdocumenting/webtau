# Webtau 

Webtau (**Web** **T**est **au**tomation) - concise and expressive way to write end-to-end and unit tests.

:include-image: webtau-logo.png {width: 256, align: "left"}

Test your application across multiple layers:
* REST API
* GraphQL API
* Browser
* CLI
* Database
* Business Logic (JVM only)

Use one layer to re-enforce tests on another. E.g. REST API layer to set up data for Browser test, or database layer
to validate GraphQL API.

Use powerful [REPL](REPL/experiments) mode to significantly speed up end-to-end tests development.

:include-cli-output: cli-tests/browser-repl-select/out.txt {title: "trying css selectors"}

Use [Persona](persona/introduction) concept to test API Authorization and collaboration Web Apps like chats and editors. 

Capture test artifacts like API Responses, screenshots, command line output to automate your user facing documentation.

Leverage out of the box rich reporting

:include-image: doc-artifacts/reports/report-crud-separated-http-calls.png {fit: true}

Tests can be written in any JVM language. Language specific syntactic sugar is available for `Groovy`.

* [REST API example](#rest-api-example)
* [GraphQL API example](#graphql-api-example)
* [Browser example](#browser-example)
* [DB example](#db-example)
* [CLI example](#cli-example)

# Rest API Example 

Webtau provides a succinct DSL for exercising HTTP end points (e.g. REST APIs) and provides simplified way to 
assert responses.

``````tabs

Groovy:
 ````columns

 left:
 :include-file: scenarios/rest/simpleGet.groovy {title: "REST API test (Groovy runner)"}
 :include-file: com/example/tests/junit5/WeatherGroovyTest.groovy {title: "REST API test (JUnit5)"} 

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
:include-file:  com/example/tests/junit5/WeatherJavaTest.java {title: "REST API test (JUnit5 Java)"} 

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
 :include-file: scenarios/graphql/weatherQuery.groovy {title: "GraphQL API test (Groovy runner)"}
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

# Browser Example 

```tabs
Groovy:

:include-file: scenarios/ui/searchWithPagesWaitTo.groovy {title: "Browser test (Groovy runner)"}
:include-groovy: pages/SearchPage.groovy {title: "SearchPage.groovy"}

:include-file: pages/Pages.groovy {
    title: "Pages.groovy",
    excludeRegexp: ["calculation", "form", "payments"]
}

Java:

:include-file: com/example/tests/junit5/WebSearchJavaTest.java {title: "Browser test (JUnit 5)"}
:include-file: com/example/tests/junit5/pages/SearchPage.java {title: "SearchPage.java"}

:include-file: com/example/tests/junit5/pages/Pages.java {
    title: "Pages.java"
}
```

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
