# Match Any Provided Value

Use `anyOf` matcher to match an expected value against any of the provided values.
[Universal Compare](matchers/universal-compare) rule is applicable.

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    surroundedBy: "any-of-matcher-standalone"
}
:include-markdown: import-ref.md

Java: 
:include-java: org/testingisdocumenting/webtau/MatchersTest.java {
    entry: "anyOfMatcherExample",
    bodyOnly: true
} 
:include-markdown: import-ref.md
```

# Mixing Values And Matchers

You can mix values and other matchers passed to `anyOf` 

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    surroundedBy: "any-of-matcher-combo"
}

Java: 
:include-java: org/testingisdocumenting/webtau/MatchersTest.java {
    entry: "anyOfMatcherWithOtherMatcherExample",
    bodyOnly: true
} 
```