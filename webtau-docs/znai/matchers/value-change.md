---
identifier: {validationPath: "org/testingisdocumenting/webtau/expectation/code/ChangeCodeMatcherJavaTest.java"}
---

# Code Change Matcher

Use `code` matchers to validate that a block of code changes a value:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/code/ChangeCodeMatcherGroovyTest.groovy {
  title: "single bean property example",
  surroundedBy: "change-single-property",
  noGap: true
}
:include-cli-output: doc-artifacts/javabean-id-change-fail-output.txt 
:include-markdown: import-ref.md

Java:
:include-file: org/testingisdocumenting/webtau/expectation/code/ChangeCodeMatcherJavaTest.java {
  title: "single bean property example",
  surroundedBy: "change-single-property",
  noGap: true
}
:include-cli-output: doc-artifacts/javabean-id-change-fail-output.txt 
:include-markdown: import-ref.md
```

Pass a java bean to `:identifier: change` to validate that at least one property is changed

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/code/ChangeCodeMatcherGroovyTest.groovy {
  title: "full bean property example",
  surroundedBy: "change-full-property",
  noGap: true
}
:include-cli-output: doc-artifacts/javabean-full-change-fail-output.txt

Java:
:include-file: org/testingisdocumenting/webtau/expectation/code/ChangeCodeMatcherJavaTest.java {
  title: "full bean property example",
  surroundedBy: "change-full-property",
  noGap: true
}
:include-cli-output: doc-artifacts/javabean-full-change-fail-output.txt
```

Use `shouldNot` to enforce that a value doesn't change


```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/code/ChangeCodeMatcherGroovyTest.groovy {
  title: "should not change bean properties",
  surroundedBy: "change-not-full-property",
  noGap: true
}
:include-cli-output: doc-artifacts/javabean-fail-not-to-change-output.txt 

Java:
:include-file: org/testingisdocumenting/webtau/expectation/code/ChangeCodeMatcherJavaTest.java {
  title: "should not change bean properties",
  surroundedBy: "change-not-full-property",
  noGap: true
}
:include-cli-output: doc-artifacts/javabean-fail-not-to-change-output.txt 
```

