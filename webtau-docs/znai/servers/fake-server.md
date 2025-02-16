# Router Creation

Use `:identifier: server.router {validationPath: "scenarios/server/fakeRest.groovy"}` to defined end points responses.

```tabs
Groovy:
:include-file: scenarios/server/fakeRest.groovy {
  title: "fake server creation example",
  surroundedBy: "router-example"
}
:include-markdown: import-ref.md

Java:
:include-file: com/example/tests/junit5/FakeServerJavaTest.java {
  title: "fake server creation example",
  surroundedBy: "router-example"
}
:include-markdown: import-ref.md
```

# Fake Server Creation

Use `:identifier: server.fake {validationPath: "scenarios/server/fakeRest.groovy"}` and a router to create a server with controlled responses

```tabs
Groovy:
:include-file: scenarios/server/fakeRest.groovy {
  title: "fake server creation example",
  surroundedBy: ["server-create-example", "fake-response-check"],
  surroundedBySeparator: ""
}

Java:
:include-file: com/example/tests/junit5/FakeServerJavaTest.java {
  title: "fake server creation example",
  surroundedBy: ["server-create-example", "fake-response-check"],
  surroundedBySeparator: ""
}
```

Note: If you omit `serverId` parameter, an auto generated one will be used instead. It affects what you see in the generated report


