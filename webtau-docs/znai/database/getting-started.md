Webtau provides database access layer `db.` to help with data setup, validation and manipulation.
Use it in conjunction with other layers like `http.` to write more powerful tests.

* [Data Setup](database/data-setup)

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseFacadeTest.groovy {
    entry: "should insert table data into a table",
    title: "example of data setup",
    bodyOnly: true,
    startLine: "PRICES <<",
    endLine: "2000 }"
}
 