WebTau provides two methods to assert values: `should` and `waitTo`. They work for business logic testing, HTTP, Browser, and other layers.
Methods accept a matcher as a second parameter:

WebTau provides console output of all the matches it does, regardless of whether it fails or passes.

# Should 

Use `should` to assert a value using a matcher. One of the most common matchers is `equal`.
In WebTau equal is a universal matcher that you will learn about [later](matchers/universal-compare).

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "should example",
    surroundedBy: "string-string-example",
    commentsType: "remove"
}

 Note: Groovy has a shortcut for `equal` matcher.

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "should example",
    surroundedBy: "string-string-example",
    commentsType: "remove"
} 
``` 

:include-cli-output: doc-artifacts/string-string-comparison.txt {title: "should console output"}

Use `shouldBe` alias to make a better flow with matchers like `greaterThan`:

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "shouldBe example",
    surroundedBy: "string-number-example",
    commentsType: "remove"
}

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "shouldBe example",
    surroundedBy: "string-number-example",
    commentsType: "remove"
} 
``` 

# WaitTo

Use `waitTo` to wait for a value to eventually match a matcher. 

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "waitTo example",
    surroundedBy: "wait-consume-message"
}

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "waitTo example",
    surroundedBy: "wait-consume-message"
} 
``` 

:include-cli-output: doc-artifacts/wait-message.txt {title: "wait console output"}

Use `waitToBe` alias to make a better flow with matchers like `greaterThan`:

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "waitToBe example",
    surroundedBy: "wait-number-records"
}

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "waitToBe example",
    surroundedBy: "wait-number-records"
} 
``` 

# Failure Output

Above you saw how WebTau outputs matched information.
In case of failed assertion WebTau outputs additional information about the actual value.


```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "assertion",
    surroundedBy: "failed-list"
}

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "assertion",
    surroundedBy: "failed-list"
} 
``` 

:include-cli-output: doc-artifacts/list-failure.txt {title: "failed assertion console output"}

:include-markdown: static-import.md
