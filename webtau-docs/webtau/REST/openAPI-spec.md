# Validation

Webtau supports validation of responses against an [OpenAPI specification](https://www.openapis.org/).  This feature can
be enabled by specifying the `openApiSpecUrl` configuration option.  This should be the URL to the specification against 
which to validate.

:include-file: scenarios/rest/springboot/webtau-with-spec.cfg {title: "Configuration"}

# Current limitations

OpenAPI specification support is still in its early stage.  It is fully functional but there are a few limitations to be
aware of:
* webtau currently only supports OpenAPI specification v2
* specification matching is currently done based on the path and method
* any HTTP requests which do not match any operation in the specification will not fail tests but will produce a warning
on the console

# Validations report

The validation errors are reported in the same manner as assertion errors.  They are available in the output from the 
command line webtau runner:
```bash
> executing HTTP POST http://localhost:8080/customers
 {
  "id": 1,
  "firstName": "FN",
  "lastName": "LN",
  "_links": {
    "self": {
      "href": "http://localhost:8080/customers/1"
    },
    "customer": {
      "href": "http://localhost:8080/customers/1"
    }
  }
 }
X failed executing HTTP POST http://localhost:8080/customers : 
API spec validation failure: ERROR - Response status 201 not defined for path ''.: []
[x] failed
java.lang.AssertionError: 
API spec validation failure: ERROR - Response status 201 not defined for path ''.: []
	at scenarios.rest.springboot.customerCrud$_run_closure1.doCall(customerCrud.groovy:8)
	at scenarios.rest.springboot.customerCrud$_run_closure1.doCall(customerCrud.groovy)



Total: 1,  Passed: 0,  Skipped: 0,  Failed: 1,  Errored: 0
```

They are also available in the HTML report:

:include-image: img/rest-crud-openapi-spec-validation-error.png {fit: true}

# Validation Configuration

To ignore additional properties in responses set `openApiIgnoreAdditionalProperties` to `true`. 
As any other config value it can be done via command line, config file or system properties.

# Disable Validation

Use `com.twosigma.webtau.openapi.OpenApi` to locally disable OpenAPI validation.

:include-groovy: doc-artifacts/snippets/openapi/disableAll.groovy {title: "Disable request and response validation"}
:include-groovy: doc-artifacts/snippets/openapi/disableRequest.groovy {title: "Disable request validation"}
:include-groovy: doc-artifacts/snippets/openapi/disableResponse.groovy {title: "Disable response validation"}
