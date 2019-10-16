# WebTau Runner

You can use webtau `http.` and `browser.` methods as in a junit 4 tests, but to enable reporting you need to use 
`@RunWith(WebTauRunner.class)`

```tabs
Groovy: :include-file: com/example/tests/junit4/CustomerCrudSingleGroovyTest.groovy {title: "CRUD test in a single method", commentsType: "inline"}
Java: :include-file: com/example/tests/junit4/CustomerCrudSingleJavaTest.java {title: "CRUD test in a single method", commentsType: "inline"}
```

# Before/After

Use `@Before`, `@After` standard `JUnit 4` annotations to implement init and cleanup logic for each test.
  
```tabs
Groovy: :include-file: com/example/tests/junit4/CustomerCrudBeforeAfterGroovyTest.groovy {title: "CRUD test with @Before and @After annotations", commentsType: "inline"}
Java: :include-file: com/example/tests/junit4/CustomerCrudBeforeAfterJavaTest.java {title: "CRUD test with @Before and @After annotations", commentsType: "inline"}
```

# Maven Import

:include-file: maven/junit4-dep.xml
