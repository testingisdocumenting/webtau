# Cross Types Comparison


In Webtau you can compare strings, dates, arrays, strings and numbers, dates and strings, arrays and sets and other numerous combinations 
using the same set of matchers. 

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    surroundedBy: ["string-string-example", "string-number-example"], surroundedBySeparator: "",
    commentsType: "inline"
}

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    surroundedBy: ["string-string-example", "string-number-example"], surroundedBySeparator: "",
    commentsType: "inline"
} 
``` 

# Static Import

Examples assume `:identifier: WebTauCore {validationPath: "org/testingisdocumenting/webtau/MatchersTest.java"}` static import.
The class contains useful utility methods and matchers

:include-file: org/testingisdocumenting/webtau/MatchersTest.java { surroundedBy: "import-dsl" }

# Java Beans And Maps

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

:include-file: org/testingisdocumenting/webtau/Account.java { surroundedBy: "account", title: "Account Bean" }


