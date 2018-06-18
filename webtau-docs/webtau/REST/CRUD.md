# Example

We have an app that exposes create, read, update, and delete operations for customer records. Records are being served 
under `/customers`.

Here is an example of a `CRUD` operations test.

:include-file: examples/rest/springboot/customerCrud.groovy {commentsType: "inline"}

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

# Report

After your test executions a report will be produced.

:include-image: img/rest-report-http-calls.png {fit: true}

Note: asserted values are being tracked and highlighted inside the report 

# Spring Boot

WebTau is framework agnostic. However, to make a concrete example, the `/customer` `CRUD` endpoint
is created by using [Spring Boot](https://projects.spring.io/spring-boot/).

Three files are required to have a working REST endpoint with `CRUD` operations.

1. Domain object

    :include-file: com/example/demo/springboot/app/data/Customer.java {title: "Customer.java"}

2. Repository

    :include-file: com/example/demo/springboot/app/data/CustomerRepository.java {title: "CustomerRepository.java"}

3. Entry point

    :include-file: com/example/demo/springboot/app/SpringBootDemoApp.java {title: "SpringBootDemoApp.java"}
