WebTau provides two ways to assert values: `should` and `waitTo`. They work for business logic testing, HTTP, Browser, and other layers.
Methods accept a matcher as a second parameter:

```attention-note
WebTau provides console output for all the matching it does, regardless of whether it fails or passes.
```

# Should 

Use `should` to assert a value using a matcher. One of the most common matchers is `equal`.
In WebTau equal is a universal matcher that you will learn about [later](matchers/universal-compare).

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "should example",
    surroundedBy: "string-string-example",
    commentsType: "remove",
    noGap: true,
    noGapBorder: true
}
:include-cli-output: doc-artifacts/string-string-comparison-output.txt 

 Note: Groovy has a shortcut for `equal` matcher.

:include-markdown: import-ref.md

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "should example",
    surroundedBy: "string-string-example",
    commentsType: "remove",
    noGap: true,
    noGapBorder: true
} 
:include-cli-output: doc-artifacts/string-string-comparison-output.txt 

:include-markdown: import-ref.md
``` 


Use `shouldBe` alias to make a better flow with matchers like `greaterThan`:

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "shouldBe example",
    surroundedBy: "string-number-example",
    commentsType: "remove",
    noGap: true,
    noGapBorder: true
}
:include-cli-output: doc-artifacts/string-number-comparison-output.txt 

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "shouldBe example",
    surroundedBy: "string-number-example",
    commentsType: "remove",
    noGap: true,
    noGapBorder: true
} 
:include-cli-output: doc-artifacts/string-number-comparison-output.txt 
``` 

# WaitTo

Use `waitTo` to wait for a value to eventually match a matcher. 

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "waitTo example",
    surroundedBy: "wait-consume-message",
    noGap: true,
    noGapBorder: true
}
:include-cli-output: doc-artifacts/wait-message-output.txt 

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "waitTo example",
    surroundedBy: "wait-consume-message",
    noGap: true,
    noGapBorder: true
} 
:include-cli-output: doc-artifacts/wait-message-output.txt 
``` 


Use `waitToBe` alias to make a better flow with matchers like `greaterThan`:

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "waitToBe example",
    surroundedBy: "wait-number-records",
    noGap: true,
    noGapBorder: true
}
:include-cli-output: doc-artifacts/wait-tobe-output.txt 

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "waitToBe example",
    surroundedBy: "wait-number-records",
    noGap: true,
    noGapBorder: true
} 
:include-cli-output: doc-artifacts/wait-tobe-output.txt 
``` 

# Negative Matching

Both `should` and `waitTo` have negative forms:

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "shouldNot example",
    surroundedBy: "string-string-negative-example",
    noGap: true,
    noGapBorder: true
}
:include-cli-output: doc-artifacts/string-string-negative-comparison-output.txt 

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "shouldNot example",
    surroundedBy: "string-string-negative-example",
    noGap: true,
    noGapBorder: true
} 
:include-cli-output: doc-artifacts/string-string-negative-comparison-output.txt 
``` 

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "waitToNot example",
    surroundedBy: "wait-negative-consume-message",
    noGap: true,
    noGapBorder: true
}
:include-cli-output: doc-artifacts/wait-negative-message-output.txt 

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "waitToNot example",
    surroundedBy: "wait-negative-consume-message",
    noGap: true,
    noGapBorder: true
} 
:include-cli-output: doc-artifacts/wait-negative-message-output.txt 
``` 

# Failure Output

Above you saw how WebTau outputs matched information.
In case of failed assertion WebTau outputs additional information about the actual value.


```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "assertion",
    surroundedBy: "failed-list",
    noGap: true,
    noGapBorder: true
}
:include-cli-output: doc-artifacts/list-failure.txt 

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "assertion",
    surroundedBy: "failed-list",
    noGap: true,
    noGapBorder: true
} 
:include-cli-output: doc-artifacts/list-failure.txt 
``` 
