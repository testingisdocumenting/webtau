# Create

Use language specific DSL to create `TableData` instance:

```tabs
Groovy:
:include-groovy: com/twosigma/webtau/data/table/TableDataExtensionTest.groovy {entry: "createTableWithUnderscore", bodyOnly: true}

Java:
:include-groovy: com/twosigma/webtau/data/table/TableDataTest.groovy {entry: "createTableDataInOneGo", bodyOnly: true}

 Note: example above assumes `import static com.twosigma.webtau.Ddjt.*`. Additionally `Ddjt` has `___` defined of various lengths. 
```

Using `____` underscore is optional and is there for esthetics only

```tabs
Groovy:
:include-groovy: com/twosigma/webtau/data/table/TableDataExtensionTest.groovy {entry: "createTableWithoutUnderscore", bodyOnly: true}

Java:
:include-groovy: com/twosigma/webtau/data/table/TableDataTest.groovy {entry: "createTableDataSeparateValues", bodyOnly: true}
```
