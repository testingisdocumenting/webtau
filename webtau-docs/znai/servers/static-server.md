# Serve Static Content

Use `:identifier: server.serve {validationPath: "scenarios/server/staticContent.groovy"}` to start a server on random port
that will host static content from the specified directory.

```tabs
Groovy:
:include-file: scenarios/server/staticContent.groovy {
  title: "create and start server",
  surroundedBy: "static-server-create",
}
:include-markdown: import-ref.md

Java:
:include-file: com/example/tests/junit5/StaticServerJavaTest.java {
  title: "create and start server",
  surroundedBy: "static-server-create"
}
:include-markdown: import-ref.md
```

:include-file: data/staticcontent/data.json {autoTitle: true}

```tabs
Groovy:
:include-file: scenarios/server/staticContent.groovy {
  title: "testing response from a static server",
  surroundedBy: "static-server-json",
}

Java:
:include-file: com/example/tests/junit5/StaticServerJavaTest.java {
  title: "testing response from a static server",
  surroundedBy: "static-server-json"
}
```

# Set Server As Base Url

Use `:identifier: setAsBaseUrl {validationPath: "com/example/tests/junit5/StaticServerJavaTest.java"}` to use the server's host and port as base url

```tabs
Groovy:
:include-file: scenarios/server/staticContent.groovy {
  title: "server as base url",
  surroundedBy: "set-base-url-example"
}

Java:
:include-file: com/example/tests/junit5/StaticServerJavaTest.java {
  title: "server as base url",
  surroundedBy: "set-base-url-example"}
```


# Host Html For Browser

Use static server to host html files and then open them using a browser.

:include-file: data/staticcontent/hello.html {autoTitle: true}

```tabs
Groovy:
:include-file: scenarios/server/staticContent.groovy {
  title: "open browser using static contet",
  surroundedBy: "static-server-html",
}

Java:
:include-file: com/example/tests/junit5/StaticServerJavaTest.java {
  title: "open browser using static contet",
  surroundedBy: "browser-example"
}
```

# Override Response

Use `:identifier: addOverride {validationPath: "scenarios/server/proxyServer.groovy"}` to modify response of a proxied server

```tabs
Groovy:
:include-file: scenarios/server/staticContent.groovy {
  title: "content override",
  surroundedBy: "override-example",
}

Java:
:include-file: com/example/tests/junit5/StaticServerJavaTest.java {
  title: "content override",
  surroundedBy: "override-example",
}
```

# Server Slowdown

Use `:identifier: markUnresponsive {validationPath: "scenarios/server/staticContent.groovy"}` to mark server as unresponsive

```tabs
Groovy:
:include-file: scenarios/server/staticContent.groovy {
  title: "mark as unresponsive",
  surroundedBy: "mark-unresponsive",
}

Java:
:include-file: com/example/tests/junit5/StaticServerJavaTest.java {
  title: "mark as unresponsive",
  surroundedBy: "mark-unresponsive"
}
```

# Server Break

Use `:identifier: markBroken {validationPath: "scenarios/server/staticContent.groovy"}` to mark server as broken

```tabs
Groovy:
:include-file: scenarios/server/staticContent.groovy {
  title: "mark as broken",
  surroundedBy: "mark-broken"
}

Java:
:include-file: com/example/tests/junit5/StaticServerJavaTest.java {
  title: "mark as broken",
  surroundedBy: "mark-broken"
}
```

# Server Fix

Use `:identifier: fix {validationPath: "scenarios/server/staticContent.groovy"}` to remove broken and/or slowdown state

:include-file: scenarios/server/staticContent.groovy {
  title: "fix server",
  surroundedBy: "mark-fix"
}

