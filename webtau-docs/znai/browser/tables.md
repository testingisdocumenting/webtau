---
identifier: {validationPath: "examples/scenarios/ui/tables.groovy"}
---

# Validating Table Data

Use `:identifier: browser.table` to define a table on your page that you want to interact with.
Once table is defined, you can validate it using `should` and `waitTo`.

```tabs
Groovy: 
:include-file: examples/scenarios/ui/tables.groovy {
  title: "table validation",
  surroundedBy: "table-data-validation"
}

Java:
:include-file: com/example/tests/junit5/BrowserTablesJavaTest.java {
  title: "table validation",
  surroundedBy: "table-data-validation"
}
```

# Extracting Table Data

Use `:identifier: extractTableData` to extract `TableData` for further processing, e.g. saving to a disk:

```tabs
Groovy: 
:include-file: examples/scenarios/ui/tables.groovy {
  title: "table data extraction",
  surroundedBy: "extract-single-table-data"
}

Java:
:include-file: com/example/tests/junit5/BrowserTablesJavaTest.java {
  title: "table data extraction",
  surroundedBy: "extract-single-table-data"
}
```

Use `:identifier: extractAndMergeTableData` to extract `TableData` from multiple tables matching the selector:

```tabs
Groovy: 
:include-file: examples/scenarios/ui/tables.groovy {
  title: "multiple tables data extraction",
  surroundedBy: "extract-all-table-data"
}

Java:
:include-file: com/example/tests/junit5/BrowserTablesJavaTest.java {
  title: "multiple tables data extraction",
  surroundedBy: "extract-all-table-data"
}
```

Note: Only first table columns will be used

# Supported Tables Flavor

WebTau supports standard HTML tables and [AG Grid](https://www.ag-grid.com).

There is a mechanism to register new parsers. If you are interested in contributing a parser or learn how to do it, please [create a ticket](https://github.com/testingisdocumenting/webtau/issues)
