---
identifier: {validationPath: "org/testingisdocumenting/webtau/expectation/code/ValueChangeCodeMatcherJavaTest.java"}
---

# Value Matcher

Use `change` matcher and `takeSnapshot` to assert that a value is change/not-changed or wait for the change:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/equality/SnapshotChangeValueMatcherGroovyTest.groovy {
  title: "value should change",
  surroundedBy: "value-should-change",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/snapshot-should-change-output.txt 
:include-markdown: import-ref.md

Java:
:include-file: org/testingisdocumenting/webtau/expectation/equality/SnapshotChangeValueMatcherJavaTest.java {
  title: "value should change",
  surroundedBy: "value-should-change",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/snapshot-should-change-output.txt 
:include-markdown: import-ref.md
```

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/equality/SnapshotChangeValueMatcherGroovyTest.groovy {
  title: "value wait to change",
  surroundedBy: "value-wait-to-change",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/snapshot-wait-to-change-output.txt 

Java:
:include-file: org/testingisdocumenting/webtau/expectation/equality/SnapshotChangeValueMatcherJavaTest.java {
  title: "value wait to change",
  surroundedBy: "value-wait-to-change",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/snapshot-wait-to-change-output.txt 
```

Note: Value must implement `SnapshotValueAware` interface to work with `change` matcher. Common WebTau abstractions like web page element and file content 
already implement them. Use `code` matcher below as an alternative if you can't have interface implemented.

# Code Change Matcher

Use `code` matchers to validate that a block of code changes a value:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/code/ValueChangeCodeMatcherGroovyTest.groovy {
  title: "single bean property example",
  surroundedBy: "change-single-property",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/javabean-id-change-fail-output.txt 

Java:
:include-file: org/testingisdocumenting/webtau/expectation/code/ValueChangeCodeMatcherJavaTest.java {
  title: "single bean property example",
  surroundedBy: "change-single-property",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/javabean-id-change-fail-output.txt 
```

Pass a java bean to `:identifier: change` to validate that at least one property is changed

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/code/ValueChangeCodeMatcherGroovyTest.groovy {
  title: "full bean property example",
  surroundedBy: "change-full-property",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/javabean-full-change-fail-output.txt

Java:
:include-file: org/testingisdocumenting/webtau/expectation/code/ValueChangeCodeMatcherJavaTest.java {
  title: "full bean property example",
  surroundedBy: "change-full-property",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/javabean-full-change-fail-output.txt
```

Use `shouldNot` to enforce that a value doesn't change


```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/code/ValueChangeCodeMatcherGroovyTest.groovy {
  title: "should not change bean properties",
  surroundedBy: "change-not-full-property",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/javabean-fail-not-to-change-output.txt 

Java:
:include-file: org/testingisdocumenting/webtau/expectation/code/ValueChangeCodeMatcherJavaTest.java {
  title: "should not change bean properties",
  surroundedBy: "change-not-full-property",
  noGap: true,
  noGapSeparator: true
}
:include-cli-output: doc-artifacts/javabean-fail-not-to-change-output.txt 
```

