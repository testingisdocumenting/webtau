# Equality Comparison

When two tables are not equal, WebTau generates all the required info to investigate:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/TableDataMatchersGroovyExamplesTest.groovy {
  surroundedBy: "table-equal-mismatch",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/table-equal-console-output.txt 
:include-markdown: import-ref.md

Java:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/TableDataMatchersJavaExamplesTest.java {
  surroundedBy: "table-equal-mismatch",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/table-equal-console-output.txt 
:include-markdown: import-ref.md
```

# Contain

Use `contain` matcher to check if one map is a subset of another:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/TableDataMatchersGroovyExamplesTest.groovy {
  surroundedBy: "table-contain-mismatch",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/table-contain-console-output.txt 

Java:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/TableDataMatchersJavaExamplesTest.java {
  surroundedBy: "table-contain-mismatch",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/table-contain-console-output.txt 
```

