---
identifier: {validationPath: "scenarios/server/router.groovy"}
---

# Router Creation {style: "api"}

Use `:identifier: server.router` to defined or override end-point responses for servers.

```tabs
Groovy:
:include-file: scenarios/server/fakeRest.groovy {
  title: "router creation example",
  surroundedBy: "router-example"
}

Java:
:include-file: com/example/tests/junit5/FakeServerJavaTest.java {
  title: "router creation example",
  surroundedBy: "router-example"
}
```

# Request Properties {style: "api"}

## Path Parameters {style: "api"}

Use `:identifier: request.param` to access a path parameter value

```tabs
Groovy:
:include-file: scenarios/server/router.groovy {
  title: "path param access",
  surroundedBy: "path-param-example", 
  highlight: "request.param"
}

Java:
:include-file: com/example/tests/junit5/RouterPropertiesJavaTest.java {
  title: "path param access",
  surroundedBy: "path-param-example", 
  highlight: "request.param"
}
```

## Query Parameters {style: "api"}

Use `:identifier: request.queryParam` to access a path parameter value

```tabs
Groovy:
:include-file: scenarios/server/router.groovy {
  title: "path param access",
  surroundedBy: "query-param-example", 
  highlight: "request.queryParam"
}

Java:
:include-file: com/example/tests/junit5/RouterPropertiesJavaTest.java {
  title: "path param access",
  surroundedBy: "query-param-example", 
  highlight: "request.queryParam"
}
```
