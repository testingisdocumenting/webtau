# Create

Use language specific DSL to create `TableData` instance:

```tabs
Groovy:
:include-groovy: com/twosigma/webtau/data/table/TableDataExtensionTest.groovy {entry: "createTableWithUnderscore", bodyOnly: true}

Java:
:include-groovy: com/twosigma/webtau/data/table/TableDataTest.groovy {entry: "createTableDataInOneGo", bodyOnly: true}

 Note: The example above assumes `import static com.twosigma.webtau.Ddjt.*`. Additionally `Ddjt` has header-separating 
lines defined using underscores `___` of various lengths, which you can optionally use for aesthetics. 
```

Using `____` underscore is optional and is there for aesthetics only

```tabs
Groovy:
:include-groovy: com/twosigma/webtau/data/table/TableDataExtensionTest.groovy {entry: "createTableWithoutUnderscore", bodyOnly: true}

Java:
:include-groovy: com/twosigma/webtau/data/table/TableDataTest.groovy {entry: "createTableDataSeparateValues", bodyOnly: true}
```

# Permutations

Use `permute(v1, v2)` to automatically generate multiple rows.

```tabs
Groovy:
:include-groovy: com/twosigma/webtau/data/table/TableDataExtensionTest.groovy {entry: "createTableDataWithPermute", bodyOnly: true}

Java:
:include-groovy: com/twosigma/webtau/data/table/TableDataTest.groovy {entry: "createTableDataWithPermute", bodyOnly: true}
```

:include-table: table-with-permute.json

# Previous Value Reference

Use `cell.previous` to refer to the previous row value

```tabs
Groovy:
:include-groovy: com/twosigma/webtau/data/table/TableDataExtensionTest.groovy {entry: "createTableDataWithPreviousRef", bodyOnly: true}

Java:
:include-groovy: com/twosigma/webtau/data/table/TableDataTest.groovy {entry: "createTableDataWithPreviousRef", bodyOnly: true}
```

:include-table: table-with-cell-previous.json