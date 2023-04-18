WebTau `browser` module lets you interact with a browser.

High level abstractions streamline location, assertion and async logic.

WebTau leverages [Selenium WebDriver](https://www.selenium.dev) to do the heavy lifting.

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

:include-cli-output: doc-artifacts/com.example.tests.junit5.WebSearchJavaTest-console-output.txt  {
  title: "console output",
  surroundedBy: "searchByQuery"
}