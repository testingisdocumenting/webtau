# WebTau Runner

You can use webtau `http.`, `graphql.`, `browser.`, `cli.`, `db.` methods as in a junit 5 tests, but to enable reporting you need to use 
`@Webtau` annotation

```tabs
Groovy: :include-file: com/example/tests/junit5/CustomerCrudSeparatedGroovyTest.groovy {commentsType: "inline"}
Java: :include-file: com/example/tests/junit5/CustomerCrudSeparatedJavaTest.java {commentsType: "inline"}
```

# BeforeAll/AfterAll

Use `@BeforeAll` and `@AfterAll` to prepare and cleanup resources required for multiple test methods.

```tabs
Groovy: :include-file: com/example/tests/junit5/CustomerQueryGroovyTest.groovy {commentsType: "inline"}
Java: :include-file: com/example/tests/junit5/CustomerQueryJavaTest.java {commentsType: "inline"}
```

# Maven Import

:include-file: maven/junit5-dep.xml

# TestFactory

With the additional annotation `@TestFactory` you can use `TableData` as an easy-to-read source of 
similar but independent tests where each row is treated as its own test (comparable to JUnit 4's 
parameterized tests), optionally with a descriptive label. 
Here are examples of parameterized tests with and without labels, and how an IDE uses the label for display purposes:
:include-groovy: com/example/tests/junit5/DynamicTestsGroovyTest.groovy {title: "Parameterized tests without explicit label", entry: "individual tests use generated display labels"}

:include-groovy: com/example/tests/junit5/DynamicTestsGroovyTest.groovy {title: "Parameterized tests with explicit label", entry: "individual tests can use an optional display label to clarify the use case"}

:include-image: img/intellij-parameterized-tests.png