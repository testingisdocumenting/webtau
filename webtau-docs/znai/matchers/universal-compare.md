# Cross Types Comparison

In WebTau you can compare strings, dates, arrays, strings and numbers, dates and strings, arrays and sets and other numerous combinations 
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

# Reporting

Every comparison failed and successful generates console output with comparison details.

:include-cli-output: doc-artifacts/string-string-comparison.txt {title: "report console output"} 

Values have predefined names when they come from HTTP response or Web UI, etc. But regular values default to `value`.

Pass `name` as a second parameter to `actual` to set an explicit reporting name. 

```tabs
Groovy: 
:include-file: org/testingisdocumenting/webtau/MatchersGroovyTest.groovy {
    surroundedBy: "number-number-example",
    commentsType: "inline"
}

Java: 
:include-file: org/testingisdocumenting/webtau/MatchersTest.java {
    surroundedBy: "number-number-example",
    commentsType: "inline"
} 
``` 

:include-cli-output: doc-artifacts/number-number-comparison.txt {title: "explicit name report console output"}

:include-markdown: static-import.md

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

:include-cli-output: doc-artifacts/bean-map-compare-output.txt {title: "bean mismatches highlighted"}

# Dates

Universal compare lets you compare dates, local dates, strings, and times with timezones against each other. 

```tabs
Groovy: 
:include-groovy: org/testingisdocumenting/webtau/expectation/equality/handlers/DatesCompareToHandlerGroovyExamplesTest.groovy {
    title: "text and date",
    entry: [
        "actual local date string greater than expected local date instance",
        "actual zoned date time string greater than expected local date instance",
        "should compare local date against local date time"],
    entrySeparator: "",
    bodyOnly: true,
    commentsType: "inline"
}

Java:
:include-groovy: org/testingisdocumenting/webtau/expectation/equality/handlers/DatesCompareToHandlerJavaExamplesTest.java {
    title: "text and date",
    entry: [
        "actualLocalDateStringGreaterThanExpectedLocalDateInstance", 
        "actualZonedDateTimeStringGreaterThanExpectedLocalDateInstance",
        "shouldCompareLocalDateAgainstLocalDateTime"],
    entrySeparator: "",
    bodyOnly: true,
    commentsType: "inline"
}
 ``` 
