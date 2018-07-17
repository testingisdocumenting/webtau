# Lazy Resource

One of the benefits of separating one CRUD `scenario` into multiple is to be able to run one test at a time. 
In order to do it we will use `createLazyResource`.

:include-file: examples/scenarios/rest/springboot/customerCrudSeparated.groovy {commentsType: "inline"}

Note: to run one scenario at a time use `sscenario` (additional `s` in front)

# Report

As you can see in the report below, each `CRUD` operation has its own entry. If you follow this pattern, then you
can filter tests by `create`, `update`, `read`, `delete` to streamline investigation.

:include-image: img/rest-crud-separated-report.png {fit: true}
