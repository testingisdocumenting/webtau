:include-markdown: imports/single-dsl-import.md

# Server Specific Import

To only import `server` module use

```tabs
Groovy:
:include-file: com/example/tests/junit5/FakeServerJavaTest.java {title: "server import", include: "WebTauServerFacade.server", replace: [";", ""]} 
:include-file: maven/server-dep.xml {title: "maven dependency"}

Java:
:include-file: com/example/tests/junit5/FakeServerJavaTest.java {title: "server import", include: "WebTauServerFacade.server"} 
:include-file: maven/server-dep.xml {title: "maven dependency"}
```
