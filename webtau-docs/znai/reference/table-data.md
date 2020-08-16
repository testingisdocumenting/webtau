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

# Permutations

Use `permute(v1, v2)` to automatically generate multiple rows.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableDataWithPermute", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataWithPermute", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

:include-table: table-with-permute.json

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

:include-table: table-after-replace.json

# Cell Above Value Reference

Use `cell.above` to refer to the previous row value

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableDataWithAboveRef", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataWithAboveRef", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

:include-table: table-with-cell-above.json

# Cell Above Math

Use `cell.above.plus|minus` to generate a derived value based on the previous row value 

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableDataWithAboveRefAndMath", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entry: "createTableDataWithAboveRefAndMath", bodyOnly: true, removeReturn: true, removeSemicolon: true}
```

:include-table: table-with-cell-above-math.json

Extract `cell.above.operation` to make your intentions clearer

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/data/table/TableDataGroovyTest.groovy {entry: "createTableDataWithAboveRefAndMathExtracted", bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/data/table/TableDataJavaTest.java {entries: ["createIncrementExample", "createTableDataWithAboveRefAndMathExtracted"], bodyOnly: true, removeReturn: true, removeSemicolon: true}
```