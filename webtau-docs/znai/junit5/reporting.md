# WebTau Report

You can use WebTau `http.`, `graphql.`, `browser.`, `cli.`, `db.` modules without any additional setup, but to include a test into generated report and to get extra console output you need to use
`@WebTau` annotation or enable global extension (see below)

```tabs
Groovy: :include-file: com/example/tests/junit5/CustomerCrudSeparatedGroovyTest.groovy {commentsType: "inline"}
Java: :include-file: com/example/tests/junit5/CustomerCrudSeparatedJavaTest.java {commentsType: "inline"}
```

# Extra Console Output

WebTau produces extra console output when you enable reporting either via annotation or globally (see below).

:include-cli-output: doc-artifacts/com.example.tests.junit5.CustomerQueryJavaTest-console-output.txt {title: "console output", readMore: true, readMoreVisibleLines: 20}


# Automatic Extension

To enable report for all the tests without annotating each of them, use JUnit5 [automatic extension registration](https://junit.org/junit5/docs/current/user-guide/#extensions-registration-automatic)

:include-file: src/test/resources/META-INF/services/org.junit.jupiter.api.extension.Extension {autoTitle: true}

:include-file: src/test/resources/junit-platform.properties {autoTitle: true}

# BeforeAll/AfterAll Reporting

`@BeforeAll` and `@AfterAll` are separate entries in the generated report

```tabs
Groovy: :include-file: com/example/tests/junit5/CustomerQueryGroovyTest.groovy {commentsType: "inline"}
Java: :include-file: com/example/tests/junit5/CustomerQueryJavaTest.java {commentsType: "inline"}
```

:include-image: doc-artifacts/reports/junit5-report-afterall.png {title: "web report", fit: true, border: true}