# Scenarios

You provide `REST endpoints` so users can execute various scenarios.
You need to test those scenarios and then document them.

To automate the process, let's capture executed scenarios and use them inside your documentation.

# Capturing Test Artifacts

To capture artifacts use `http.doc.capture`:

:include-file: scenarios/rest/simplePost.groovy {title: "test.groovy", commentsType: "inline"}

An `employee-get` directory will be created containing a number of test artifacts.

# Test Artifacts Location

By default, the directory will be created in the current working directory.
To change it add `docPath` to your `webtau.cfg` file.

:include-file: examples/scenarios/rest/docArtifacts.cfg {title: "webtau.cfg"}

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

The actual request URL is captured both as a full URL in `request.url.full.txt` and also just the path
in `request.url.path.txt`.

:include-file: doc-artifacts/end-point/request.url.path.txt {title: "request.url.path.txt"}

:include-file: doc-artifacts/end-point/request.url.full.txt {title: "request.url.full.txt"}

# Document REST calls

If you have user facing scenario tests, capture them and refer to them inside your documentation.
Set your documentation build pipeline like below.

:include-flow-chart: documentation-flow.json     

Combine REST requests and responses with Open API generated specs for complete documentation. 
