# Should And Wait

WebTau provides two methods to assert values: `should` and `waitTo`. They work for business logic testing, HTTP, Browser, and other layers.

Methods accept a matcher as a second parameter: 

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "should example",
    surroundedBy: "string-string-example",
    commentsType: "remove"
}

:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    title: "wait example",
    surroundedBy: "wait-consume-message"
}

 Note: Groovy has a shortcut for `equal` matcher.

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "should example",
    surroundedBy: "string-string-example",
    commentsType: "remove"
} 

:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    title: "wait example",
    surroundedBy: "wait-consume-message"
} 
``` 

:include-cli-output: doc-artifacts/string-string-comparison.txt {title: "should console output"}

:include-cli-output: doc-artifacts/wait-message.txt {title: "wait console output"}

:include-markdown: static-import.md
