# Create

Use language specific DSL to create `TableData` instance:

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableWithUnderscore", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataInOneGo", bodyOnly: true, removeReturn: true, removeSemicolon: true}

 Note: The example above assumes `import static org.testingisdocumenting.webtau.WebTauCore.*` or `import static org.testingisdocumenting.webtau.WebTauDsl.*`.
Additionally `WebTauCore` has header-separating 
lines defined using underscores `___` of various lengths, which you can optionally use for aesthetics. 
```

Using `____` underscore is optional and is there for aesthetics only

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableWithoutUnderscore", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataSeparateValues", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

# Key Columns

Use `*` in front of a column to specify it as a key column

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableWithKeyColumns", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "createTableWithKeyColumns", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

To access a value by key column

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "findByKeyAndValidate", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "findByKeyAndValidate", bodyOnly: true}
```

To change key columns of an existing table

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "changeKeyColumns", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "changeKeyColumns", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

Note: `withNewKeyColumns` creates new table and validates new key column uniqueness

# Permutations

Use `permute(v1, v2)` to automatically generate multiple rows.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableDataWithPermute", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataWithPermute", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

:include-table: doc-artifacts/table-with-permute.json

# GUID

Use `cell.guid` to automatically generate unique ids.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableDataWithPermuteAndGuid", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataWithPermuteAndGuid", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

:include-table: doc-artifacts/table-with-permute-and-guid.json

# Replace

Use `table.replace(before, after)` to replace values in a table. 

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableWithUnderscore", bodyOnly: true}

:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "replaceValue", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataInOneGo", bodyOnly: true, removeReturn: true, removeSemicolon: true}

:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "replaceValue", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

:include-table: doc-artifacts/table-after-replace.json

# Cell Above Value Reference

Use `cell.above` to refer to the previous row value

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableDataWithAboveRef", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataWithAboveRef", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

:include-table: doc-artifacts/table-with-cell-above.json

# Cell Above Math

Use `cell.above.plus|minus` to generate a derived value based on the previous row value 

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableDataWithAboveRefAndMath", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataWithAboveRefAndMath", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

:include-table: doc-artifacts/table-with-cell-above-math.json

Extract `cell.above.operation` to make your intentions clearer

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableDataWithAboveRefAndMathExtracted", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entries: ["createIncrementExample", "createTableDataWithAboveRefAndMathExtracted"], bodyOnly: true, removeReturn: true, removeSemicolon: true}
```