# Runners 

To run WebTau tests you have a few options:
* [Standalone Groovy Runner](#groovy-runner)
* [JUnit5](#junit5)
* [JUnit4](#junit4)

# Groovy Runner

:include-markdown: installation-groovy-runner.md

```cli
webtau --example
```

Navigate into `todo` example

```cli
cd examples/todo
```

:include-file: examples/todo/todolist.groovy {title: "todolist.groovy"}

To run test

```cli {paramsToHighlight: "url"}
webtau todolist.groovy --url=https://jsonplaceholder.typicode.com 
```

:include-markdown: common/note-package-import.md

To integrate CLI run into maven build use:  

:include-file: maven/groovy-dep.xml {title: "WebTau Dependency"}

:include-file: maven/plugin-all-parameters.xml {title: "WebTau Plugin"}

[Learn more about Groovy Runner features](groovy-standalone-runner/introduction)

# JUnit5

```tabs
Groovy: 

:include-file: maven/groovy-dep.xml {title: "WebTau Groovy Dependency"}
:include-file: maven/junit5-dep.xml {title: "JUnit5 Dependency"}

:include-file: com/example/tests/junit5/JUnit5ExampleGroovyTest.groovy {title: "JUnit 5 example", commentsType: "inline"}}

Java: 

:include-file: maven/java-dep.xml {title: "WebTau Java Dependency"}
:include-file: maven/junit5-dep.xml {title: "JUnit5 Dependency"}

:include-file: com/example/tests/junit5/JUnit5ExampleJavaTest.java {title: "JUnit 5 example", commentsType: "inline"}

```
 
# JUnit4

```tabs
Groovy: 

:include-file: maven/groovy-dep.xml {title: "WebTau Groovy Dependency"}
:include-file: maven/junit4-dep.xml {title: "JUnit4 Dependency"}

:include-file: com/example/tests/junit4/JUnit4ExampleGroovyTest.groovy {title: "JUnit 4 example", commentsType: "inline"}

Java: 

:include-file: maven/java-dep.xml {title: "WebTau Java Dependency"}
:include-file: maven/junit4-dep.xml {title: "JUnit4 Dependency"}

:include-file: com/example/tests/junit4/JUnit4ExampleJavaTest.java {title: "JUnit 4 example", commentsType: "inline"}

```
 