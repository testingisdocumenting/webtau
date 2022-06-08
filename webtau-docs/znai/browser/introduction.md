WebTau `browser.` module lets you interact with a browser.  
It provides higher level abstractions to locate elements, encapsulate page implementation details and deal with
async nature of modern UIs.

```tabs
Groovy:

:include-file: scenarios/ui/searchWithPagesWaitTo.groovy {title: "Browser test (Groovy runner)"}
:include-groovy: pages/SearchPage.groovy {title: "SearchPage.groovy"}

:include-file: pages/Pages.groovy {
    title: "Pages.groovy",
    excludeRegexp: ["calculation", "form", "payments"]
}

Java:

:include-file: com/example/tests/junit5/WebSearchJavaTest.java {title: "Browser test (JUnit 5)"}
:include-file: com/example/tests/junit5/pages/SearchPage.java {title: "SearchPage.java"}

:include-file: com/example/tests/junit5/pages/Pages.java {
    title: "Pages.java"
}
```

