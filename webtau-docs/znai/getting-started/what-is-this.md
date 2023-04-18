# WebTau 

WebTau (**Web** **T**est **au**tomation) - concise and expressive way to write end-to-end and unit tests.

:include-image: webtau-logo.png {scale: 0.5, align: "left"}

Test your application across multiple layers and use unique features:

* [REST API](#rest-api)
* [WebSocket](#websocket)
* [GraphQL API](#graphql-api)
* [Authorization Personas](#persona)
* [Browser](#browser)
* [Fake, Static And Proxy Servers](#fake-static-and-proxy-servers)
* [Database](#database)
* [CLI](#cli)
* [Business Logic (JVM only)](#business-logic-jvm)
* [REPL](#repl)
* [Reporting](#reporting)
* [Documentation Assistance](#documentation-assistance)

There are many modules, but you can use any module you need independently, or use all the modules at once with convenient single imports.

Note: Tests can be written in any JVM language. Language specific syntactic sugar is available for `Groovy`.

# Rest API 

:include-markdown: HTTP/introduction.md

[Read More](HTTP/CRUD-example)

# WebSocket

:include-markdown: web-socket/introduction.md

[Read More](web-socket/received-messages)

# GraphQL API 

:include-markdown: GraphQL/introduction.md

[Read More](GraphQL/queries-and-mutations)

# Persona

Use [Persona](persona/introduction) concept to test API Authorization and collaboration Web Apps like chats and editors.

```tabs
Groovy:
:include-file: scenarios/rest/headers/personaGet.groovy {
  title: "persona authorization",
  excludeRegexp: ["package", "import"]
}

:include-json: doc-artifacts/alice-statement/response.json {title: "Alice server response"}
:include-json: doc-artifacts/bob-statement/response.json {title: "Bob server response"}

Java:
:include-file: com/example/tests/junit5/PersonaHttpJavaTest.java {
  title: "persona authorization",
  excludeRegexp: ["package", "import", "http.doc"]
}

:include-json: doc-artifacts/alice-statement/response.json {title: "Alice server response"}
:include-json: doc-artifacts/bob-statement/response.json {title: "Bob server response"}
```

# Browser 

:include-markdown: browser/introduction.md

[Read More](browser/basic-configuration)

# Fake, Static And Proxy Servers

:include-markdown: servers/introduction.md

[Read More](servers/introduction)

# Database

WebTau `db` module streamlines databases data setup, assertion and waiting on.

[Read More](database/introduction)

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseTest.groovy {
    entry: "use table data permute, above and guid to generate rows",
    title: "DB data setup example",
    bodyOnly: true,
    startLine: "def PRICES",
    endLine: "cell.above + 20",
    commentsType: "inline"
}

# CLI

WebTau `cli` module helps with running and testing command line tools

[Read More](cli/introduction)

:include-file: doc-artifacts/snippets/foreground-cli/withOutputValidation.groovy {
  title: "output validation"
}

# Business Logic (JVM)

Powerful WebTau matchers help with complex data validation and provide rich output to help with failure investigation:

[Read More](matchers/introduction)

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    surroundedBy: "beans-table-example",
    commentsType: "inline"
}

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    surroundedBy: "beans-table-example",
    commentsType: "inline"
} 
``` 

:include-cli-output: doc-artifacts/beans-table-compare-output.txt {title: "beans mismatches highlighted"}

# REPL

Use powerful [REPL](REPL/experiments) mode to significantly speed up end-to-end tests development.
Build your tests one step at a time without losing time on restarts.

:include-cli-output: cli-tests/browser-repl-select/out.txt {title: "trying css selectors"}

# Reporting

Leverage out of the box [rich reporting](report/introduction).
Report captures everything you do. Single self-sufficient file that can be slacked or emailed. 
Permalinks let you share the exact failure problem with your colleagues.

:include-image: doc-artifacts/reports/report-crud-separated-http-calls.png {fit: true, border: true}

# Documentation Assistance

WebTau helps you to capture test artifacts like [API Responses](HTTP/documentation-artifacts), [screenshots](browser/documentation-artifacts),
command line output to automate your user facing documentation creation.

:include-flow-chart: HTTP/documentation-flow.json

Below is the example of API documentation with example of requests/response captured by a test:

:include-open-api: scenarios/rest/openapi/api-spec.json {operationId: "createEmployee" }

```columns
left: 
:include-json: doc-artifacts/employee-post/request.json { title: "request payload" }

right: 
:include-json: doc-artifacts/employee-post/response.json { 
  title: "response payload", 
  pathsFile: "doc-artifacts/employee-post/paths.json" 
}
```

:include-image: doc-artifacts/search.png {fit: true, annotate: true, title: "auto created screenshot and annotations"}
