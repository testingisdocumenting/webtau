# Single Test Method

We have an app that exposes `create`, `read`, `update`, and `delete` operations for customer records. Records are being served 
under `/customers`.

Here is an example of a `CRUD` operations test.

```tabs
Groovy:
:include-file: scenarios/rest/springboot/customerCrud.groovy {commentsType: "inline"}

:include-markdown: import-ref.md

Java:
:include-file: com/example/tests/junit5/CustomerCrudJavaTest.java {commentsType: "inline"}

:include-markdown: import-ref.md
```

# Generated Report

After test runs, WebTau generates [HTML report](report/introduction):

:include-image: doc-artifacts/reports/report-crud-http-calls.png {fit: true, annotate: false, border: true}

Note: asserted values are being tracked and highlighted inside the report 

# Separate Test Methods

```tabs
Groovy:

One of the benefits of separating one CRUD `scenario` into multiple is to be able to run one test at a time. 
In order to make each test runnable independently we will use `createLazyResource`.

:include-file: scenarios/rest/springboot/customerCrudSeparated.groovy {commentsType: "inline", title: "CRUD separated"}

:include-file: scenarios/rest/springboot/Customer.groovy {commentsType: "inline", title: "Customer lazy resource"}

 Note: to run one scenario at a time use `sscenario` (additional `s` in front). [Read more](groovy-standalone-runner/selective-run)

Java:

One of the benefits of separating one CRUD `@Test` into multiple is to be able to run one test at a time. 
In order to make each test runnable independently we will leverage `BeforeAll`, `AfterAll`, and `TestMethodOrder`.

:include-file: com/example/tests/junit5/CustomerCrudSeparatedJavaTest.java {commentsType: "inline"}
```

# Separate Methods Report

Now report has separate entries for each `CRUD` operation. Makes it possible to filter tests by `create`, `update`, `read`, `delete` to streamline investigation.

:include-image: doc-artifacts/reports/report-crud-separated-http-calls.png {fit: true, border: true}
