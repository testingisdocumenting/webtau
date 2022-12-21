# TestFactory

With the additional annotation `@TestFactory` you can use `TableData` as an easy-to-read source of
similar but independent tests where each row is treated as its own test, optionally with a descriptive label.

Here are some examples of parameterized tests with and without labels: 

```tabs
Groovy:
:include-groovy: com/example/tests/junit5/DynamicTestsGroovyTest.groovy {title: "parameterized tests without explicit label", entry: "individual tests use generated display labels"}
:include-groovy: com/example/tests/junit5/DynamicTestsGroovyTest.groovy {title: "parameterized tests with explicit label", entry: "individual tests label to clarify the use case"}

Java:
:include-java: com/example/tests/junit5/DynamicTestsJavaTest.java {title: "parameterized tests without explicit label", entry: "individualTestsUseGeneratedDisplayLabels"}
:include-java: com/example/tests/junit5/DynamicTestsJavaTest.java {title: "parameterized tests with explicit label", entry: "individualTestsLabelToClarifyUseCase"}
```
