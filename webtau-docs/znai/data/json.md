# Read List

:include-java-doc: org/testingisdocumenting/webtau/data/DataJson.java {entry: "list"}

:include-json: data/flat-list.json {paths: ["root[0].name", "root[1].payload.info"], autoTitle: true}

```tabs
Groovy:
:include-file: scenarios/data/readingData.groovy {title: "read json as list", surroundedBy: ["// read-json-list", "// validate-json-list"]}
:include-markdown: import-ref.md

Java:
:include-file: org/testingisdocumenting/webtau/data/DataJsonJavaTest.java {title: "read json as list", surroundedBy: ["// read-json-list", "// validate-json-list"]}
:include-markdown: import-ref.md
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

# Write List

:include-java-doc: org/testingisdocumenting/webtau/data/DataJson.java {entry: "write"}

```tabs
Groovy:
:include-file: scenarios/data/writingData.groovy {surroundedBy: "// json-list-data", title: "data"}
:include-file: scenarios/data/writingData.groovy {surroundedBy: "write-json-list-maps", title: "write list of maps"}

Java:
:include-file: org/testingisdocumenting/webtau/data/DataJsonJavaTest.java {title: "data", surroundedBy: ["// list-data"]}
:include-file: org/testingisdocumenting/webtau/data/DataJsonJavaTest.java {title: "write list of maps", surroundedBy: ["write-json-list-maps"]}
```

`write` methods produce additional information that helps with tests investigation

:include-cli-output: doc-artifacts/data-json-write-list.txt {title: "console output"}

:include-file: generated/from-list-maps.json { autoTitle: true }

# Write Map

```tabs
Groovy:
:include-file: scenarios/data/writingData.groovy {surroundedBy: "// json-map-data", title: "data"}
:include-file: scenarios/data/writingData.groovy {surroundedBy: "write-json-map", title: "write map"}

Java:
:include-file: org/testingisdocumenting/webtau/data/DataJsonJavaTest.java {title: "data", surroundedBy: ["// map-data"]}
:include-file: org/testingisdocumenting/webtau/data/DataJsonJavaTest.java {title: "write list of maps", surroundedBy: ["write-json-map"]}
```

:include-file: generated/from-map.json { autoTitle: true }

# Write Table Data

```tabs
Groovy:
:include-file: scenarios/data/writingData.groovy {surroundedBy: "// json-table-data", title: "data"}
:include-file: scenarios/data/writingData.groovy {surroundedBy: "write-json-table", title: "write table data"}

Java:
:include-file: org/testingisdocumenting/webtau/data/DataJsonJavaTest.java {title: "data", surroundedBy: ["// table-data"]}
:include-file: org/testingisdocumenting/webtau/data/DataJsonJavaTest.java {title: "write table data", surroundedBy: ["write-json-table"]}
```

`write` methods produce additional information that helps with tests investigation

:include-cli-output: doc-artifacts/data-json-write-table.txt {title: "console output"}

:include-file: generated/from-table-data.json { autoTitle: true }
