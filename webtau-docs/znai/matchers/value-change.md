---
identifier: {validationPath: "org/testingisdocumenting/webtau/expectation/code/ChangeCodeMatcherJavaTest.java"}
---

# Code Change Matcher

Use `code` matchers to validate that a block of code changes a value:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/code/ChangeCodeMatcherGroovyTest.groovy {
  title: "single bean property example",
  surroundedBy: "change-single-property"
}

Java:
:include-file: org/testingisdocumenting/webtau/expectation/code/ChangeCodeMatcherJavaTest.java {
  title: "single bean property example",
  surroundedBy: "change-single-property"
}
```

:include-cli-output: doc-artifacts/javabean-id-change-fail-output.txt { title: "console output" }

Pass a java bean to `:identifier: change` to validate that at least one property is changed

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/code/ChangeCodeMatcherGroovyTest.groovy {
  title: "full bean property example",
  surroundedBy: "change-full-property"
}

Java:
:include-file: org/testingisdocumenting/webtau/expectation/code/ChangeCodeMatcherJavaTest.java {
  title: "full bean property example",
  surroundedBy: "change-full-property"
}
```

:include-cli-output: doc-artifacts/javabean-full-change-fail-output.txt { title: "console output" }

