# Reporting

WebTau in its core captures test actions and assertions.
Everything you do, every match that is **passed** or failed is being recorded.

Information is available in console output, so you don't need to sprinkle `println` statements everywhere.
And all the captured information is available as self-contained rich HTML report. 

Single report HTML file that you can email, slack or host on an FS and open with no servers required.

```tabs
Groovy:
:include-file: scenarios/data/readingData.groovy {title: "read table data from csv", surroundedBy: "// read"}

Java:
:include-file: org/testingisdocumenting/webtau/data/DataCsvJavaTest.java {title: "read table data from csv", surroundedBy: ["// read-table"]}
```

:include-cli-output: doc-artifacts/csv-table-data-output.txt {title: "console output"}

# High Level API

WebTau provides high level API to interact with HTTP, Browser, Database, File System. 
It is easy to use, requires minimum setup and every action is reported.

You can use WebTau to only test a single layer of your product, but you can als combine layers in a single test and setup data using HTTP layer and validate using a Browser layer or a DB layer.
All is with a single configuration, consistent set of matchers and rich reporting.

# Syntax Sugar

WebTau provides DSL to make common testing operations succinct. Syntax sugar is available for Java and Groovy and 
since WebTau core is Java, additional syntax sugar can be added to languages like Kotlin and Scala.  

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableDataWithAboveRefAndMath", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataWithAboveRefAndMath", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

# REPL

Writing end-to-end tests is hard and there are a lot of excuses not write one. 
End-to-end test feedback loop is usually long and slow. WebTau provides [REPL](REPL/experiments) mode to help you 
experiment with API and write a test in incremental fashion. 

:include-cli-output: cli-tests/http-repl-output/out.txt {title: "exploring http.get"}

# Documentation Artifacts

Your users deserve useful and up-to date documentation. Manually maintaining documentation is hard and expensive.
WebTau helps you to capture test artifacts during the tests, for example
* Screenshots (with annotations)
* API request/response examples
* CLI params and sample output

Captured artifacts are agnostic to the documentation system. However, you can take a look at [Znai](https://github.com/testingisdocumenting/znai) documentation system 
that was designed with documentation maintenance in mind.

```columns
left: 
:include-json: doc-artifacts/employee-post/request.json { title: "captured request payload" }

right: 
:include-json: doc-artifacts/employee-post/response.json { 
  title: "captured response payload", 
  pathsFile: "doc-artifacts/employee-post/paths.json" 
}
```

1. Enter search query
2. Results are displayed in a grid

:include-image: doc-artifacts/search.png {border: true, fit: true, annotate: true, title: "automatically captured screenshot with annotations"}