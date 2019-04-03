# Create

Use language specific DSL to create `TableData` instance:

```tabs
Groovy:
:include-groovy: com/twosigma/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableWithUnderscore", bodyOnly: true}

Java:
:include-java: com/twosigma/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataInOneGo", bodyOnly: true, removeReturn: true, removeSemicolon: true}

 Note: The example above assumes `import static com.twosigma.webtau.Ddjt.*`. Additionally `Ddjt` has header-separating 
lines defined using underscores `___` of various lengths, which you can optionally use for aesthetics. 
```

Using `____` underscore is optional and is there for aesthetics only

```tabs
Groovy:
:include-groovy: com/twosigma/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableWithoutUnderscore", bodyOnly: true}

Java:
:include-java: com/twosigma/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataSeparateValues", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

# Permutations

Use `permute(v1, v2)` to automatically generate multiple rows.

```tabs
Groovy:
:include-groovy: com/twosigma/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableDataWithPermute", bodyOnly: true}

Java:
:include-java: com/twosigma/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataWithPermute", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

:include-table: table-with-permute.json

# Cell Above Value Reference

Use `cell.above` to refer to the previous row value

```tabs
Groovy:
:include-groovy: com/twosigma/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableDataWithAboveRef", bodyOnly: true}

Java:
:include-java: com/twosigma/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataWithAboveRef", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

:include-table: table-with-cell-above.json

You can use underscores of various length as a shortcut for `cell.above`

```tabs
Groovy:
:include-groovy: com/twosigma/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableDataWithAboveRefShortcut", bodyOnly: true}

 Note: Unlike Java, Groovy can use underscores of any length as long as it is longer than 1 character.
   
Java:
:include-java: com/twosigma/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataWithAboveRefShortcut", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```
