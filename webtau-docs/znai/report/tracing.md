:include-markdown: import-ref.md

# Key Value

Use `trace` core method to render key-value pairs to a console and to generated web report.

```tabs
Groovy:
:include-file: scenarios/concept/trace.groovy {
  title: "trace key values",
  surroundedBy: ["trace-map", "trace-vararg"]
}

Java:
:include-file:  com/example/tests/junit5/StepTraceJavaTest.java {
  title: "trace key values",
  surroundedBy: ["trace-map", "trace-vararg"]
}
```

:include-cli-output: doc-artifacts/com.example.tests.junit5.StepTraceJavaTest-console-output.txt {
  title: "console output",
  startLine: "trace label",
  endLine: "v4"
}

# Object Properties

Use `trace` and `properties` combo to trace object properties:

:include-groovy: org/testingisdocumenting/webtau/data/converters/ObjectPropertiesTest.groovy {
  title: "trace object properties",
  entry: "nested simple bean",
  surroundedBy: ["data-prep", "data-trace"]
}

:include-cli-output: doc-artifacts/nested-simple-bean-trace-output.txt {
  title: "console output",
}

# Multiple Objects As Table

Use `trace` and `propertiesTable` combo to trace object properties:

:include-groovy: org/testingisdocumenting/webtau/data/converters/ObjectPropertiesTest.groovy {
  title: "table of objects properties",
  entry: "table of complex beans",
  surroundedBy: ["data-prep", "data-trace"], 
  surroundedBySeparator: ""
}

:include-cli-output: doc-artifacts/table-properties-trace-output.txt {
  title: "console output",
}




