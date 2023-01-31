WebTau `db.` module can operate on JVM DataSource without providing JDBC URLs.
In the following sections, we will cover [Spring Boot](https://spring.io/projects/spring-boot) Repository Test.

# Database Setup

We need to configure WebTau `db.` module as well as Spring test one

:include-file: com/example/demo/springboot/app/data/CustomerRepositoryTest.java {
  title: "datasource injection and db module setup",
  surroundedBy: "repository-test-config",
  commentsType: "inline"
}

Here is how we define `tc` profile using [Test Containers JDBC support](https://www.testcontainers.org/modules/databases/jdbc/):

:include-file: src/test/resources/application-tc.properties {autoTitle: true, lang: "bash", commentsType: "inline"}

# Write Directly To DB 

Let's write data directly to database table, bypassing repository class, so we can isolate our test logic.

:include-java: com/example/demo/springboot/app/data/CustomerRepositoryTest.java {
  entry: "findById",
  commentsType: "inline"
}

# Read Directly From DB

Now let's write data using repository and validate that DB table actually contains the data.
We then will test a simple query by last name.

:include-java: com/example/demo/springboot/app/data/CustomerRepositoryTest.java {
  entry: "createEntriesAndFindByName",
  commentsType: "inline"
}

:include-java: com/example/demo/springboot/app/data/CustomerRepositoryTest.java {
  title: "create customers from TableData",
  entry: "createCustomers"
}

# Cleanup Between Tests

In between tests we will explicitly delete data from DB

:include-java: com/example/demo/springboot/app/data/CustomerRepositoryTest.java {
  title: "cleanup before each test",
  entry: "cleanupBeforeTest"
}

# Import And Dependency

:include-file: com/example/demo/springboot/app/data/CustomerRepositoryTest.java {
  title: "import",
  includeRegexp: "DatabaseDsl"
}

To include only Database module as your dependency use

:include-file: maven/data-dep.xml {title: "maven dependency"}

