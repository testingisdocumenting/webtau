# Module Import

To use `data.` module you need to import `:identifier: WebTauDsl {validationPath: "com/example/tests/junit5/JUnit5ExampleJavaTest.java"}` all-in module
or import `:identifier: Data {validationPath: "org/testingisdocumenting/webtau/data/DataJsonJavaTest.java"}` module only

:include-file: com/example/tests/junit5/JUnit5ExampleJavaTest.java {title: "all-in import", includeRegexp: "WebTauDsl", commentsType: "remove"}
:include-file: org/testingisdocumenting/webtau/data/DataJsonJavaTest.java {title: "data only import", includeRegexp: "Data\.data"}

# Read List

:include-java-doc: org/testingisdocumenting/webtau/data/DataJson.java {entry: "list"}

:include-json: data/flat-list.json {paths: ["root[0].name", "root[1].payload.info"], autoTitle: true}

```tabs
Groovy:
:include-file: scenarios/data/readingData.groovy {title: "read json as list", surroundedBy: ["// read-json-list", "// validate-json-list"]}

Java:
:include-file: org/testingisdocumenting/webtau/data/DataJsonJavaTest.java {title: "read json as list", surroundedBy: ["// read-json-list", "// validate-json-list"]}
```

`read` methods produce additional report to help with tests investigation

:include-cli-output: doc-artifacts/json-list-data-output.txt {title: "console output"}

# Read Map

:include-java-doc: org/testingisdocumenting/webtau/data/DataJson.java {entry: "map"}

:include-json: data/root-map.json {paths: ["root.payload.info"], autoTitle: true}

```tabs
Groovy:
:include-file: scenarios/data/readingData.groovy {title: "read json as map", surroundedBy: ["// read-json-map"]}

Java:
:include-file: org/testingisdocumenting/webtau/data/DataJsonJavaTest.java {title: "read json as map", surroundedBy: ["// read-json-map"]}
```

# Read TableData

:include-java-doc: org/testingisdocumenting/webtau/data/DataJson.java {entry: "table"}

:include-json: data/flat-list.json {autoTitle: true}

```tabs
Groovy:
:include-file: scenarios/data/readingData.groovy {title: "read json as table", surroundedBy: ["// read-json-table"]}

Java:
:include-file: org/testingisdocumenting/webtau/data/DataJsonJavaTest.java {title: "read json as table", surroundedBy: ["// read-json-table"]}
```

