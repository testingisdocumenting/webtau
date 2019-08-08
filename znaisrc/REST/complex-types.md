# Contain

Use `contain` matcher to test scenarios like search or list of recently created entries. 
This way you don't have to assume an existing state of your backend under test.
 
:include-json: doc-artifacts/list-match/response.json {title: "Response"}

Given the response, we want to make sure there is an entry with a specified `firstName` and `lastName`.
 
:include-file: examples/scenarios/rest/springboot/listContain.groovy

# List Of Objects

If you want to make sure that all the values in the list are what you need - use `TableData`.

:include-file: examples/scenarios/rest/springboot/listMatch.groovy

# Order Agnostic Match

Use `*key` column(s) if list order is not guaranteed

:include-file: examples/scenarios/rest/springboot/listMatchByKey.groovy
 
