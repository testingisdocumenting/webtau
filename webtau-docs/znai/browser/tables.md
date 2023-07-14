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

# Supported Tables Flavor

WebTau supports standard HTML tables and [AG Grid](https://www.ag-grid.com).

There is a mechanism to register new parsers. If you are interested in contributing a parser or learn how to do it, please [create a ticket](https://github.com/testingisdocumenting/webtau/issues)
