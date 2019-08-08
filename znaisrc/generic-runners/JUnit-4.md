# WebTau Runner

You can use webtau `http.` and `browser.` methods as in a junit 4 tests, but to enable reporting you need to use 
`@RunWith(WebTauRunner.class)`

```tabs
Groovy: :include-file: com/example/tests/junit4/CustomerCrudSingleGroovyTest.groovy
Java: :include-file: com/example/tests/junit4/CustomerCrudSingleJavaTest.java
```

# Maven Import

:include-file: maven/junit4-dep.xml
