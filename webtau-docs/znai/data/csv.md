# Table Data

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "table"}

:include-table: examples/data/table.csv {title: "data/table.csv"}

:include-file: scenarios/data/readingData.groovy {title: "read table data from csv", surroundedBy: "// read"}

Note: by default numeric values are read as strings, to auto convert numeric values to actual numbers use `tableAutoConverted` method

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "tableAutoConverted"}

:include-file: doc-artifacts/snippets/readingData/csvTableAutoConverted.groovy {title: "read table data from csv with auto conversion"}

# List Of Map

:include-table: examples/data/table.csv {title: "data/table.csv"}

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "listOfMaps"}

:include-file: doc-artifacts/snippets/readingData/listOfMaps.groovy {title: "read list of maps from csv"}

Note: by default numeric values are read as strings, to auto convert numeric values to actual numbers use `tableAutoConverted` method

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "listOfMapsAutoConverted"}

:include-file: doc-artifacts/snippets/readingData/listOfMapsAutoConverted.groovy {title: "read list of maps from csv with auto conversion"}

# Specify Header

:include-file: examples/data/table-no-header.csv {title: "data/table-no-header.csv"}

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "listOfMapsAutoConverted(List,String)"}

:include-file: doc-artifacts/snippets/readingData/listOfMapsAutoConvertedHeader.groovy {title: "read list of maps from csv with auto conversion"}

# Write List

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "write"}

:include-file: scenarios/data/writingData.groovy {surroundedBy: "// list-data", title: "data"}

:include-file: scenarios/data/writingData.groovy {surroundedBy: "write-csv-list-maps", title: "write list of maps"}

:include-file: generated/from-list-maps.csv { autoTitle: true }

# Write Table Data

:include-file: scenarios/data/writingData.groovy {surroundedBy: "// table-data", title: "data"}

:include-file: scenarios/data/writingData.groovy {surroundedBy: "write-csv-table", title: "write table"}

`write` command produce additional information that helps with tests investigation

:include-cli-output: doc-artifacts/data-csv-write-table.txt {title: "console step reporter"}

:include-file: generated/from-table-data.csv { autoTitle: true }
