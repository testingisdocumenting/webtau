# Happy Paths

Use happy paths test scenarios to document your API. Capture requests performed and response received. Use it as part of your documentation.
Benefits:
* No manual copy-pasting of requests/responses
* Documentation is up-to-date
* Happy paths API is working as intended

# Capturing Test Artifacts

Use `http.doc.capture` to capture REST API artifacts 

```tabs
Groovy:
:include-file: scenarios/rest/simplePost.groovy {title: "happy path", commentsType: "inline"}

Java:
:include-file: com/example/tests/junit5/CustomerDocCaptureTest.java {title: "happy path", commentsType: "inline"}
```

An `employee-get` directory will be created containing a number of test artifacts.

# Documentation Pipeline

Documentation pipeline can look like

:include-flow-chart: documentation-flow.json

Example of using captured artifacts using [Znai](https://github.com/testingisdocumenting/znai)

`````markdown {title: "znai example"}
# Create Employee

:include-open-api: openapi/api-spec.json {operationId: "createEmployee" }

```columns
left: 
:include-json: doc-artifacts/employee-post/request.json { title: "request payload" }

right: 
:include-json: doc-artifacts/employee-post/response.json { 
  title: "response payload", 
  pathsFile: "doc-artifacts/employee-post/paths.json" 
}
```
`````

# Create Employee

:include-open-api: scenarios/rest/openapi/api-spec.json {operationId: "createEmployee" }

```columns
left: 
:include-json: doc-artifacts/employee-post/request.json { title: "request payload" }

right: 
:include-json: doc-artifacts/employee-post/response.json { 
  title: "response payload", 
  pathsFile: "doc-artifacts/employee-post/paths.json" 
}
```

# Test Artifacts Location

By default, the directory will be created in the current working directory.
To change it add `docPath` to your `webtau.cfg.groovy` file.

:include-file: scenarios/rest/docArtifacts.cfg.groovy {title: "webtau.cfg.groovy", excludeRegexp: "package"}

# Test Artifacts

A number of artifacts will be created depending on the exact call being captured.

## Request and response payloads

Request bodies are captured in either `request.json`, `request.pdf` or `request.data` depending on the type. 

:include-json: doc-artifacts/employee-post/request.json {title: "employee-post/request.json" }

Similarly, response bodies are captured in either `response.json`, `response.pdf` or `response.data`.

:include-json: doc-artifacts/employee-get/response.json {title: "employee-get/response.json" }

## Request and response headers

Just like payloads, request and response headers are captured in `request.header.txt` and `response.header.txt`
respectively. These files contain a header per line with the name and values colon separated.  The values
are redacted for any potentially sensitive headers.

:include-json: doc-artifacts/echo-body-and-header-redacted/request.header.txt {title: "redacted request.header.txt"}

## Response body assertions

Any assertions you perform on the response body in your scenarios are captured in a `paths.json` file.  This
contains an array with the list of paths within the body whose values were asserted.

:include-json: doc-artifacts/employee-get/paths.json {title: "employee-get/paths.json" }

## Request URLs

The actual request URL is captured in two forms into two different files:
* `request.fullurl.txt` - contains the full URL
* `request.url.txt` - contains only the part specified in the http call in the scenario

:include-file: doc-artifacts/url-capture/request.url.txt {title: "request.url.txt"}

:include-file: doc-artifacts/url-capture/request.fullurl.txt {title: "request.fullurl.txt"}
