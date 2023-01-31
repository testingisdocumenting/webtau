# Full Table

To query all data from a table use:

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseDslTest.groovy {
    entry: "query method on table should be optional during comparison",
    title: "validate whole table",
    bodyOnly: true,
    startLine: "query whole table start",
    endLine: "query whole table end",
    excludeStartEnd: true
}

# Custom Query

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseDslTest.groovy {
    entry: "query table with select statement and param",
    title: "validate parts of a table",
    bodyOnly: true,
    startLine: "query with where clause start",
    endLine: "query with where clause end",
    excludeStartEnd: true
}

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseDslTest.groovy {
    entry: "query table to match one row and assert against map",
    title: "validate parts of a table against map",
    bodyOnly: true,
    startLine: "query with where clause start",
    endLine: "query with where clause end",
    excludeStartEnd: true
}

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseDslTest.groovy {
    entry: "query table with select statement and array param",
    title: "use array as input",
    bodyOnly: true,
    startLine: "query with where clause start",
    endLine: "query with where clause end",
    excludeStartEnd: true
}

# Named Parameter Shortcut

If your query uses a single unique placeholder name, you can pass a regular value instead of a `java.util.Map`

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseDslTest.groovy {
    entry: "query table to match one row using single param shortcut and assert against map",
    title: "use regular value in case of one unique placeholder name",
    bodyOnly: true,
    startLine: "query with where clause start",
    endLine: "query with where clause end",
    excludeStartEnd: true
}

# Lazy Declaration

`query` doesn't query database at the call time. It defines a query to be used later.

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseDslTest.groovy {
    entry: "create query is lazy",
    title: "create query is lazy",
    bodyOnly: true,
    startLine: "query with where clause start",
    endLine: "query with where clause end",
    excludeStartEnd: true
}

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseDslTest.groovy {
    entry: "lazy count query",
    title: "lazy count query",
    bodyOnly: true,
    startLine: "query with where clause start",
    endLine: "query with where clause end",
    excludeStartEnd: true
}

# Single Value

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseDslTest.groovy {
    entry: "should query single value with params",
    title: "validate single value",
    bodyOnly: true,
    startLine: "query single value params start",
    endLine: "query single value params end",
    excludeStartEnd: true
}

# Wait On Result

Use `waitTo` on query result to continuously query database until condition is met or timeout is reached.

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseDslTest.groovy {
    entry: "wait for count to change",
    title: "wait for count to change",
    bodyOnly: true,
    startLine: "query with where clause start",
    endLine: "query with where clause end",
    excludeStartEnd: true
}


# Query Result Value

Value returned from `query` methods is an instance of `DbQuery` type.
No actual query is performed when `DbQuery` instance is created. It holds information about what query is, and what its parameters
and only performs query when validation is triggered.

Use `queryXXX` to access underlying value.

:include-groovy: org/testingisdocumenting/webtau/db/DatabaseDslTest.groovy {
    entry: "value returned from query is a special wrapper value",
    title: "access single value",
    bodyOnly: true,
    startLine: "single value access start",
    endLine: "single value access end",
    excludeStartEnd: true
}

Avoid: When you use `queryXXX` for assertions you may lose additional report information
