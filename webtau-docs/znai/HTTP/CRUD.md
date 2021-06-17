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

# Implicit statusCode Check 

If you don't have an explicit `statusCode` validation it will be automatically validated based on the rules below 

| Method             | Expected Code |
| ------------------ |---------------|
| GET                | 200           |
| POST               | 201           |
| PUT                | 200           |
| PUT (no content)   | 204           |
| DELETE             | 200           |
| DELETE (no content)| 204           |
| PATCH              | 200           |
| PATCH (no content) | 204           |

# Report

After your test executions a report will be produced.

:include-image: doc-artifacts/reports/report-crud-http-calls.png {fit: true}

Note: asserted values are being tracked and highlighted inside the report 
