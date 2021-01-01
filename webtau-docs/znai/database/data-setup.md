# Handcrafted TableData

The simplest way to set up a DB state is to use handcrafted [TableData](reference/table-data).

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseFacadeTest.groovy {
    entry: "should use data source provider for primary database",
    title: "data setup with hand crafted table data",
    bodyOnly: true,
    startLine: "def PRICES",
    endLine: "2000 }",
    commentsType: "inline"
}
 
# Semi-Auto Generated TableData

[TableData](reference/table-data) has features like [permute](reference/table-data#permutations) and 
[cell.guid](reference/table-data#guid) among others.
Using them can reduce the effort required to maintain data setup.

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseFacadeTest.groovy {
    entry: "use table data permute, above and guid to generate rows",
    title: "data setup with hand crafted table data",
    bodyOnly: true,
    startLine: "def PRICES",
    endLine: "cell.above + 20",
    commentsType: "inline"
}

Note: code above assumes `WebTauCore.*` static import or `WebTauGroovyDsl.*` static import

:include-file: org/testingisdocumenting/webtau/db/DatabaseFacadeTest.groovy  {includeRegexp: "import.*Core"}

:include-table: doc-artifacts/db-setup-permute-table.json { title: "PRICES" }

# External File TableData

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseFacadeTest.groovy {
    entry: "use csv read data",
    title: "data setup with CSV data",
    bodyOnly: true,
    startLine: "def PRICES",
    endLine: "data.csv.table"
}
 
:include-file: resources/prices-db.csv {title: "prices-db.csv"} 

:include-table: doc-artifacts/db-setup-csv-table.json { title: "PRICES" }

# Cleaning Tables

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseFacadeTest.groovy {
    entry: "delete with params",
    title: "delete data",
    bodyOnly: true,
    includeRegexp: "delete",
    commentsType: "inline"
}

