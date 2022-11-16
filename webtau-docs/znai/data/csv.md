# Table Data

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "table"}

:include-table: examples/data/table.csv {title: "data/table.csv"}

```tabs
Groovy:
:include-file: scenarios/data/readingData.groovy {title: "read table data from csv", surroundedBy: "// read"}
:include-markdown: import-ref.md

Java:
:include-file: org/testingisdocumenting/webtau/data/DataCsvJavaTest.java {title: "read table data from csv", surroundedBy: ["// read-table"]}
:include-markdown: import-ref.md
```

Note: by default numeric values are read as strings, to auto convert numeric values to actual numbers use `tableAutoConverted` method

`read` methods produce additional report to help with tests investigation

:include-cli-output: doc-artifacts/csv-table-data-output.txt {title: "console output"}

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "tableAutoConverted"}

```tabs
Groovy:
:include-file: doc-artifacts/snippets/readingData/csvTableAutoConverted.groovy {title: "read table data from csv with auto conversion"}

Java:
:include-file: org/testingisdocumenting/webtau/data/DataCsvJavaTest.java {title: "read table data from csv with auto conversion", surroundedBy: ["// read-table-auto-converted"]}
```

# List Of Map

:include-table: examples/data/table.csv {title: "data/table.csv"}

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "listOfMaps"}

```tabs
Groovy:
:include-file: doc-artifacts/snippets/readingData/listOfMaps.groovy {title: "read list of maps from csv"}

Java:
:include-file: org/testingisdocumenting/webtau/data/DataCsvJavaTest.java {title: "read list of maps from csv", surroundedBy: ["// read-list-of-maps"]}
```

Note: by default numeric values are read as strings, to auto convert numeric values to actual numbers use `tableAutoConverted` method

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "listOfMapsAutoConverted"}

```tabs
Groovy:
:include-file: doc-artifacts/snippets/readingData/listOfMapsAutoConverted.groovy {title: "read list of maps from csv with auto conversion"}

Java:
:include-file: org/testingisdocumenting/webtau/data/DataCsvJavaTest.java {title: "read list of maps from csv with auto conversion", surroundedBy: ["// read-list-of-maps-auto-converted"]}
```

# Specify Header

:include-file: examples/data/table-no-header.csv {title: "data/table-no-header.csv"}

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "listOfMapsAutoConverted(List,String)"}

```tabs
Groovy:
:include-file: doc-artifacts/snippets/readingData/listOfMapsAutoConvertedHeader.groovy {title: "read list of maps from csv with auto conversion"}

Java:
:include-file: org/testingisdocumenting/webtau/data/DataCsvJavaTest.java {title: "read list of maps from csv with auto conversion", surroundedBy: ["// read-list-of-maps-header"]}
```

# Write List

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "write"}

```tabs
Groovy:
:include-file: scenarios/data/writingData.groovy {surroundedBy: "// csv-list-data", title: "data"}
:include-file: scenarios/data/writingData.groovy {surroundedBy: "write-csv-list-maps", title: "write list of maps"}

Java:
:include-file: org/testingisdocumenting/webtau/data/DataCsvJavaTest.java {title: "data", surroundedBy: ["// list-data"]}
:include-file: org/testingisdocumenting/webtau/data/DataCsvJavaTest.java {title: "write list of maps", surroundedBy: ["write-csv-list-maps"]}
```

`write` methods produce additional information that helps with tests investigation

:include-cli-output: doc-artifacts/data-csv-write-list.txt {title: "console step reporter"}

:include-file: generated/from-list-maps.csv { autoTitle: true }

# Write Table Data

```tabs
Groovy:
:include-file: scenarios/data/writingData.groovy {surroundedBy: "// csv-table-data", title: "data"}
:include-file: scenarios/data/writingData.groovy {surroundedBy: "write-csv-table", title: "write table"}

Java:
:include-file: org/testingisdocumenting/webtau/data/DataCsvJavaTest.java {title: "data", surroundedBy: ["// table-data"]}
:include-file: org/testingisdocumenting/webtau/data/DataCsvJavaTest.java {title: "write table", surroundedBy: ["write-csv-table"]}
```

`write` methods produce additional information that helps with tests investigation

:include-cli-output: doc-artifacts/data-csv-write-table.txt {title: "console step reporter"}

:include-file: generated/from-table-data.csv { autoTitle: true }
