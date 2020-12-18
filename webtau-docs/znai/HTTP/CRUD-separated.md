# Lazy Resource

One of the benefits of separating one CRUD `scenario` into multiple is to be able to run one test at a time. 
In order to do it we will use `createLazyResource`.

:include-file: scenarios/rest/springboot/customerCrudSeparated.groovy {commentsType: "inline", title: "CRUD separated"}

:include-file: scenarios/rest/springboot/Customer.groovy {commentsType: "inline", title: "Customer lazy resource"}

Note: to run one scenario at a time use `sscenario` (additional `s` in front). [Read more](groovy-standalone-runner/selective-run)

# Report

As you can see in the report below, each `CRUD` operation has its own entry. If you follow this pattern, then you
can filter tests by `create`, `update`, `read`, `delete` to streamline investigation.

:include-image: doc-artifacts/reports/report-crud-separated-http-calls.png {fit: true}
