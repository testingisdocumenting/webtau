WebTau provides [universal comparison](matchers/universal-compare) handlers to streamline Java Beans and Java Records assertions.

# Java Bean Equals Map

Use map and bean comparison to validate individual java bean properties.

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    surroundedBy: "bean-map-example",
    commentsType: "inline"
}
:include-markdown: import-ref.md

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    surroundedBy: "bean-map-example",
    commentsType: "inline"
} 
:include-markdown: import-ref.md
``` 

```columns
left: :include-file: org/testingisdocumenting/webtau/Account.java { surroundedBy: "account", title: "Account Java Bean" }
right: :include-file: org/testingisdocumenting/webtau/Address.java { surroundedBy: "address", title: "Address Java Bean" }
```

:include-cli-output: doc-artifacts/bean-map-compare-output.txt {title: "console output highlights bean mismatches"}

# Java Beans Contain Map

Use `contain(map)` to check if a collection of beans contains a bean with specified properties:

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    surroundedBy: "bean-map-contains-example",
    commentsType: "inline"
}

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    surroundedBy: "bean-map-contains-example",
    commentsType: "inline"
} 
``` 

:include-file: org/testingisdocumenting/webtau/Account.java { surroundedBy: "account", title: "Account Java Bean" }

:include-cli-output: doc-artifacts/list-of-beans-map-contain.txt {title: "list of beans should not contain output"}

# Java Beans Equal Table Data

Use `equal` with `TableData` to compare with a collection of java beans. All records must be present.

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

Note: Only specified properties will be matched

:include-cli-output: doc-artifacts/beans-table-compare-output.txt {title: "beans mismatches highlighted"}

# Java Beans Contain Table Data

Use `contain` with `TableData` to partially compare list of java beans.

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    surroundedBy: "beans-table-contain-example",
    commentsType: "inline"
}

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    surroundedBy: "beans-table-contain-example",
    commentsType: "inline"
} 
``` 

:include-cli-output: doc-artifacts/beans-table-contain-output.txt {title: "contain handler output"}

:include-markdown: static-import.md

# Java Records 

WebTau compares Java Records with maps and tables in the same way as Java Beans.

:include-file: org/testingisdocumenting/webtau/WishLitItem.java { surroundedBy: "record-definition", title: "WishListItem record" }

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    surroundedBy: "java-record-map-example",
    commentsType: "inline"
}

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    surroundedBy: "java-record-map-example",
    commentsType: "inline"
} 
```  

:include-cli-output: doc-artifacts/record-and-map-compare-output.txt {title: "console output highlights record mismatches"}

