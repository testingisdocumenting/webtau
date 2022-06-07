# Example

We have an app that exposes `create`, `read`, `update`, and `delete` operations for customer records. Records are being served 
under `/customers`.

Here is an example of a `CRUD` operations test.

```tabs
Groovy:
:include-file: scenarios/rest/springboot/customerCrud.groovy {commentsType: "inline"}

Java:
:include-file: com/example/tests/junit5/CustomerCrudJavaTest.java {commentsType: "inline"}
```

# Report

After your test executions a report will be produced.

:include-image: doc-artifacts/reports/report-crud-http-calls.png {fit: true, annotate: false}

Note: asserted values are being tracked and highlighted inside the report 
