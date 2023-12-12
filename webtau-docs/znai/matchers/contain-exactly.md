---
identifier: {validationPath: "org/testingisdocumenting/webtau/expectation/contain/ContainExactlyMatcherJavaTest.java"}
---

Use `:identifier: containExactly` to match two collections of elements in any order. 

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/contain/ContainExactlyMatcherGroovyTest.groovy {
  surroundedBy: "possible-mismatches-example"
}
:include-markdown: import-ref.md
Java:
:include-file: org/testingisdocumenting/webtau/expectation/contain/ContainExactlyMatcherJavaTest.java {
  surroundedBy: "possible-mismatches-example"
}
:include-markdown: import-ref.md
```

Console output displays potential mismatches to help with investigation:

:include-cli-output: doc-artifacts/possible-mismatches-output.txt {
  title: "console output"
}


Note: If you have a clear key column(s) defined, consider using `TableData` as [expected values](matchers/java-beans-and-records#java-beans-equal-table-data)