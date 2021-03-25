# CSV Read 

## Table Data 

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "table"}

:include-table: examples/data/table.csv {title: "data/table.csv"}

:include-file: doc-artifacts/snippets/readingData/csvTable.groovy {title: "read table data from csv"}

Note: by default numeric values are read as strings, to auto convert numeric values to actual numbers use `tableAutoConverted` method

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "tableAutoConverted"}

:include-file: doc-artifacts/snippets/readingData/csvTableAutoConverted.groovy {title: "read table data from csv with auto conversion"}

## List Of Map

:include-table: examples/data/table.csv {title: "data/table.csv"}

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "listOfMaps"}

:include-file: doc-artifacts/snippets/readingData/listOfMaps.groovy {title: "read list of maps from csv"}

Note: by default numeric values are read as strings, to auto convert numeric values to actual numbers use `tableAutoConverted` method

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "listOfMapsAutoConverted"}

:include-file: doc-artifacts/snippets/readingData/listOfMapsAutoConverted.groovy {title: "read list of maps from csv with auto conversion"}

# CSV Write

:include-java-doc: org/testingisdocumenting/webtau/data/DataCsv.java {entry: "write"}

:include-file: scenarios/data/writingData.groovy {includeRegexp: "data.csv.write", title: "write list of maps"}

:include-file: generated/from-list-maps.csv { title: "result" }

# JSON Read

## Read List 

:include-java-doc: org/testingisdocumenting/webtau/data/DataJson.java {entry: "list"}

:include-json: data/flat-list.json {paths: ["root[0].name", "root[1].payload.info"], title: "data/flat-list.json"}

:include-file: doc-artifacts/snippets/readingData/jsonList.groovy {title: "read json as list"}

## Read Map

:include-java-doc: org/testingisdocumenting/webtau/data/DataJson.java {entry: "map"}

:include-json: data/root-map.json {paths: ["root.payload.info"], title: "data/root-map.json"}

:include-file: doc-artifacts/snippets/readingData/jsonMap.groovy {title: "read json as map"}
