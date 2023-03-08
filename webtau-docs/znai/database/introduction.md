WebTau provides database access layer `db.` to help with data setup, validation and manipulation.
Use it in conjunction with other layers like `http.` to write more powerful tests.

* [Data Setup](database/data-setup)
* [Data Query](database/data-query)

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseTest.groovy {
    entry: "should insert table data into a table",
    title: "example of data setup",
    bodyOnly: true,
    startLine: "PRICES <<",
    endLine: "2000 }"
}

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseTest.groovy {
    entry: "query table with select statement and param",
    title: "validate parts of a table",
    bodyOnly: true,
    startLine: "query with where clause start",
    endLine: "query with where clause end",
    excludeStartEnd: true
}
