# Full Table

To query all data from a table use:

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseFacadeTest.groovy {
    entry: "query method on table should be optional during comparison",
    title: "validate whole table",
    bodyOnly: true,
    startLine: "query whole table start",
    endLine: "query whole table end",
    excludeStartEnd: true
}

# Custom Query

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseFacadeTest.groovy {
    entry: "query table with select statement and param",
    title: "validate parts of a table",
    bodyOnly: true,
    startLine: "query with where clause start",
    endLine: "query with where clause end",
    excludeStartEnd: true
}

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseFacadeTest.groovy {
    entry: "query table to match one row and assert against map",
    title: "validate parts of a table against map",
    bodyOnly: true,
    startLine: "query with where clause start",
    endLine: "query with where clause end",
    excludeStartEnd: true
}

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseFacadeTest.groovy {
    entry: "query table with select statement and array param",
    title: "use array as input",
    bodyOnly: true,
    startLine: "query with where clause start",
    endLine: "query with where clause end",
    excludeStartEnd: true
}

# Single Value

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseFacadeTest.groovy {
    entry: "should query single value with params",
    title: "validate single value",
    bodyOnly: true,
    startLine: "query single value params start",
    endLine: "query single value params end",
    excludeStartEnd: true
}

# Query Result Value

Value returned from `query` methods is an instance of `DatabaseQueryResult` type. 
It keeps the context of where the value came from and preserves it during assertions to have additional information in 
a test report.

To access underlying value for business logic use


:include-groovy: org/testingisdocumenting/webtau/db/DatabaseFacadeTest.groovy {
    entry: "value returned from query is a special wrapper value",
    title: "access single value",
    bodyOnly: true,
    startLine: "single value access start",
    endLine: "single value access end",
    excludeStartEnd: true
}

