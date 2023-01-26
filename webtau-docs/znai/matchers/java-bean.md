WebTau provides [universal comparison](matchers/universal-compare) handlers to streamline Java Bean assertions.

# Java Bean And Maps

Use map and bean comparison to validate individual java bean properties.

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    surroundedBy: "bean-map-example",
    commentsType: "inline"
}

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    surroundedBy: "bean-map-example",
    commentsType: "inline"
} 
``` 

```columns
left: :include-file: org/testingisdocumenting/webtau/Account.java { surroundedBy: "account", title: "Account Java Bean" }
right: :include-file: org/testingisdocumenting/webtau/Address.java { surroundedBy: "address", title: "Address Java Bean" }
```

:include-cli-output: doc-artifacts/bean-map-compare-output.txt {title: "bean mismatches highlighted"}

# Collection of Java Beans And Table Data

Use `TableData` to assert a collection of beans 

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    surroundedBy: "beans-table-example",
    commentsType: "inline"
}

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    surroundedBy: "beans-table-example",
    commentsType: "inline"
} 
``` 

:include-cli-output: doc-artifacts/beans-table-compare-output.txt {title: "beans mismatches highlighted"}

:include-markdown: static-import.md
