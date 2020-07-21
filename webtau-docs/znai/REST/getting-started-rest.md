:include-markdown: installation-groovy-runner.md

# Minimal Groovy Setup

Generate webtau examples 

:include-cli-command: webtau --example

Navigate into `todo` example

:include-cli-command: cd examples/todo

:include-file: examples/todo/todolist.groovy {title: "todolist.groovy"}

To run test

:include-cli-command: webtau todolist.groovy --url=https://jsonplaceholder.typicode.com {paramsToHighlight: "url"}

:include-markdown: common/note-package-import.md

## Groovy Config File

Url parameter can be moved to a `webtau.cfg` file.

:include-file: examples/todo/webtau.cfg {title: "webtau.cfg"}

[Specify multiple environments](configuration/environments) to streamline test execution.

# Minimal JUnit Setup

```tabs
Groovy: :include-file: maven/groovy-dep.xml {title: "Maven Dependency"}
Java: :include-file: maven/java-dep.xml {title: "Maven Dependency"}
```

```tabs
Groovy:
:include-file: com/example/tests/junit4/TodoListGroovyIT.groovy {title: "JUnit 4 example"}
 
Java:
:include-file: com/example/tests/junit4/TodoListJavaIT.java {title: "JUnit 4 example"}
```

## Junit Config File

Add `webtau.properties` to test class path

:include-file: src/test/resources/webtau.properties {title: "webtau.properties"}
