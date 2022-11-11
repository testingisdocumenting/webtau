# Read List

:include-java-doc: org/testingisdocumenting/webtau/data/DataJson.java {entry: "list"}

:include-json: data/flat-list.json {paths: ["root[0].name", "root[1].payload.info"], title: "data/flat-list.json"}

:include-file: scenarios/data/readingData.groovy {title: "read json as list", surroundedBy: ["// read-json-list", "// validate-json-list"]}

`read` methods produce additional report to help with tests investigation

:include-cli-output: doc-artifacts/json-list-data-output.txt {title: "console output"}

# Read Map

:include-java-doc: org/testingisdocumenting/webtau/data/DataJson.java {entry: "map"}

:include-json: data/root-map.json {paths: ["root.payload.info"], title: "data/root-map.json"}

:include-file: scenarios/data/readingData.groovy {title: "read json as map", surroundedBy: ["// read-json-map"]}

