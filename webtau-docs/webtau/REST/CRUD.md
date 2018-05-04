# Example

We have an app that lets to create, read, update, and delete customer records. Records are being served under `/customers`.

Here is an example of a `CRUD` operations test.

:include-file: examples/rest/springboot/customerCrud.groovy

# Report

After your test executions a report will be produced

:include-image: img/rest-report-http-calls.png {fit: true}

# Spring Boot

WebTau is framework agnostic. But to make it concrete `/customer` `CRUD` endpoint
is created by using [Spring Boot](https://projects.spring.io/spring-boot/)

Three files are required to have a working REST end point with `CRUD` operations.

1. Domain object

    :include-file: com/example/demo/springboot/app/data/Customer.java {title: "Customer.java"}

2. Repository

    :include-file: com/example/demo/springboot/app/data/CustomerRepository.java {title: "CustomerRepository.java"}

3. Entry point

    :include-file: com/example/demo/springboot/app/SpringBootDemoApp.java {title: "SpringBootDemoApp.java"}
