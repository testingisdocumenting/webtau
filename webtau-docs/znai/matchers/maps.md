# Equality Comparison

When two maps are not equal, WebTau generates all the required info to investigate:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/MapMatchersGroovyExamplesTest.groovy {
  surroundedBy: "maps-equal-mismatch"
}
:include-markdown: import-ref.md

Java:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/MapMatchersJavaExamplesTest.java {
  surroundedBy: "maps-equal-mismatch"
}
:include-markdown: import-ref.md
```

:include-cli-output: doc-artifacts/maps-equal-console-output.txt {
  title: "console output"
}

# Contain

Use `contain` matcher to check if one map is a subset of another:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/MapMatchersGroovyExamplesTest.groovy {
  surroundedBy: "maps-contain-mismatch"
}
Java:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/MapMatchersJavaExamplesTest.java {
  surroundedBy: "maps-contain-mismatch"
}
```

:include-cli-output: doc-artifacts/maps-contain-console-output.txt {
  title: "console output"
}
